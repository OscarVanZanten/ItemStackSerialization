package com.pizzaguy.serialization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;

public class ItemStackSerialization {
    // headers
    private static final byte[] HEADER = "IS".getBytes();
    // basic values
    private static final byte[] MATERIAL = "MA".getBytes();
    private static final byte[] AMOUNT = "AM".getBytes();
    private static final byte[] DURABILITY = "DU".getBytes();
    private static final byte[] MATERIALDATA = "MD".getBytes();
    // item meta / nbt tag values
    private static final byte[] ENCHANTS = "EN".getBytes();
    private static final byte[] DISPLAYNAME = "DN".getBytes();
    private static final byte[] LORE = "LO".getBytes();
    private static final byte[] ITEMFLAGS = "IF".getBytes();
    private static final byte[] BANNERMETA = "BA".getBytes();
    private static final byte[] BOOKMETA = "BO".getBytes();
    private static final byte[] ENCHANTMENTSTORAGEMETA = "EM".getBytes();
    private static final byte[] FIREWORKMETA = "FW".getBytes();
    private static final byte[] LEATHERARMORMETA = "LA".getBytes();
    private static final byte[] MAPMETA = "MM".getBytes();
    private static final byte[] POTIONMETA = "PM".getBytes();
    private static final byte[] SKULLMETA = "SM".getBytes();

    @SuppressWarnings("deprecation")
    public static byte[] serialize(ItemStack item) {
        if (item == null)
            return HEADER;
        //// Visible values ////
        // material //
        final byte[] material = serializeMaterial(item.getType());
        // amount //
        final byte[] amount = serializeAmount(item.getAmount());
        // durability //
        final byte[] durability = serializeDurability(item.getDurability());
        // material data //
        final byte[] materialData = serializeMaterialData(item.getData().getData());
        // enchants //
        final byte[] enchants = serializeEnchantments(item.getEnchantments());
        //// base meta data ////
        ItemMeta meta = item.getItemMeta();
        // display name //
        final byte[] displayname = meta.hasDisplayName() ? serializeDisplayName(meta.getDisplayName()) : null;
        // lore //
        final byte[] lore = meta.hasLore() ? serializeLore(meta.getLore()) : null;
        // item flags //
        final byte[] itemflags = serializeItemFlags(item.getItemMeta().getItemFlags());
        //// specified meta data ////
        byte[] metaData = null;
        if (meta instanceof BannerMeta)
            metaData = serializeBannerMeta((BannerMeta) meta);
        else if (meta instanceof BookMeta)
            metaData = serializeBookMeta((BookMeta) meta);
        else if (meta instanceof EnchantmentStorageMeta)
            metaData = serializeEnchantmentStorageMeta((EnchantmentStorageMeta) meta);
        else if (meta instanceof FireworkMeta)
            metaData = serializeFireworkMeta((FireworkMeta) meta);
        else if (meta instanceof LeatherArmorMeta)
            metaData = serializeLeatherArmorMeta((LeatherArmorMeta) meta);
        else if (meta instanceof MapMeta)
            metaData = serializeMapMeta((MapMeta) meta);
        else if (meta instanceof PotionMeta)
            metaData = serializePotionMeta((PotionMeta) meta);
        else if (meta instanceof SkullMeta)
            metaData = serializeSkullMeta(item, (SkullMeta) meta);
        // build final byte array
        final byte[] data = new ByteArrayBuilder(HEADER).add(material).add(amount).add(durability).add(materialData)
                .add(enchants).add(displayname).add(lore).add(itemflags).add(metaData).Build();
        return data;
    }

    public static byte[] serializeArray(ItemStack[] items) {
        ByteArrayBuilder builder = new ByteArrayBuilder(new byte[0]);
        for (ItemStack item : items) {
            builder.add(serialize(item));
        }
        return builder.Build();
    }

    public static ItemStack deserialize(byte[] src) {
        ItemStackBuilder imb = new ItemStackBuilder();
        for (int i = 0; i < src.length - 1; i++) {
            byte[] d = SerializationReader.readBytes(i, src, 2);
            String id = new String(d);
            if (id.equals(new String(MATERIAL)))
                imb.setType(deserializeMaterial(i, src));
            else if (id.equals(new String(AMOUNT)))
                imb.setAmount(deserializeAmount(i, src));
            else if (id.equals(new String(DURABILITY)))
                imb.setDurability(deserializeDurability(i, src));
            else if (id.equals(new String(MATERIALDATA)))
                imb.setData(deserializeMaterialData(i, src));
            else if (id.equals(new String(ENCHANTS)))
                imb.addEnchantments(deserializeEnchantments(i, src));
            else if (id.equals(new String(DISPLAYNAME)))
                imb.setDisplayName(deserizlizeDisplayName(i, src));
            else if (id.equals(new String(LORE)))
                imb.setLore(deserializeLore(i, src));
            else if (id.equals(new String(ITEMFLAGS)))
                imb.setFlags(deserializeItemFlags(i, src));
            else if (id.equals(new String(BANNERMETA)))
                imb.setBannerData(deserializeBaseColor(i, src), deserializePatterns(i + 2, src));
            else if (id.equals(new String(BOOKMETA))) {
                String author = deserializeAuthor(i, src);
                String title = deserializeTitle(i + (author.length() + 4), src);
                List<String> pages = deserializePages(i + (title.length() + 4) + (author.length() + 2), src);
                imb.setBookData(author, title, pages);
            } else if (id.equals(new String(ENCHANTMENTSTORAGEMETA)))
                imb.setStoredEnchantments(deserializeStoredEnchants(i, src));
            else if (id.equals(new String(FIREWORKMETA)))
                imb.setFireworkData(deserializePower(i, src), deserializeFireworkEffects(i + 4, src));
            else if (id.equals(new String(LEATHERARMORMETA)))
                imb.setLeatherArmorColor(deserializeLeatherArmorColor(i, src));
            else if (id.equals(new String(MAPMETA)))
                imb.setMapScaling(deserializeMapScaling(i, src));
            else if (id.equals(new String(POTIONMETA)))
                imb.setCustomPotions(deserializeCustomPotions(i, src));
            else if (id.equals(new String(SKULLMETA)))
                imb.setSkullData(deserializeSkullData(i, src));
        }
        return imb.build();
    }

    public static ItemStack[] deserializeArray(byte[] src) {
        List<ItemStack> items = new ArrayList<ItemStack>();
        byte[][] itemdata = ByteArrayBuilder.split(src, HEADER);
        for (byte[] data : itemdata) {
            ItemStack item = deserialize(data);
            items.add(item);
        }
        return (ItemStack[]) items.toArray(new ItemStack[items.size()]);
    }

    @SuppressWarnings("deprecation")
    private static final byte[] serializeMaterial(Material material) {
        byte[] data = new byte[4];
        int pointer = SerializationWriter.writeBytes(0, data, MATERIAL);
        pointer = SerializationWriter.writeBytes(pointer, data, (short) material.getId());
        return data;
    }

    private static final byte[] serializeAmount(int amount) {
        byte[] data = new byte[4];
        int pointer = SerializationWriter.writeBytes(0, data, AMOUNT);
        pointer = SerializationWriter.writeBytes(pointer, data, (short) amount);
        return data;
    }

    private static final byte[] serializeDurability(short durability) {
        byte[] data = new byte[4];
        int pointer = SerializationWriter.writeBytes(0, data, DURABILITY);
        pointer = SerializationWriter.writeBytes(pointer, data, (short) durability);
        return data;
    }

    private static final byte[] serializeMaterialData(byte materialData) {
        byte[] data = new byte[3];
        int pointer = SerializationWriter.writeBytes(0, data, MATERIALDATA);
        pointer = SerializationWriter.writeBytes(pointer, data, materialData);
        return data;
    }

    @SuppressWarnings("deprecation")
    private static final byte[] serializeEnchantments(Map<Enchantment, Integer> enchants) {
        byte[] data = new byte[4 + (enchants.size() * 3)];
        int pointer = SerializationWriter.writeBytes(0, data, ENCHANTS);
        pointer = SerializationWriter.writeBytes(pointer, data, (short) enchants.size());
        for (Enchantment enchant : enchants.keySet()) {
            pointer = SerializationWriter.writeBytes(pointer, data, (byte) enchant.getId());
            pointer = SerializationWriter.writeBytes(pointer, data, (short) enchants.get(enchant).shortValue());
        }
        return data;
    }

    private static final byte[] serializeDisplayName(String name) {
        byte[] data = new byte[4 + name.length()];
        int pointer = SerializationWriter.writeBytes(0, data, DISPLAYNAME);
        pointer = SerializationWriter.writeBytes(pointer, data, name);
        return data;
    }

    private static final byte[] serializeLore(List<String> lore) {
        int size = LORE.length + Short.BYTES;
        for (String line : lore)
            size += Short.BYTES + line.length();
        byte[] data = new byte[size];
        int pointer = SerializationWriter.writeBytes(0, data, LORE);
        pointer = SerializationWriter.writeBytes(pointer, data, (short) lore.size());
        for (String line : lore)
            pointer = SerializationWriter.writeBytes(pointer, data, line);
        return data;
    }

    private static final byte[] serializeItemFlags(Set<ItemFlag> set) {
        byte[] data = new byte[4 + set.size() * 2];
        int pointer = SerializationWriter.writeBytes(0, data, ITEMFLAGS);
        pointer = SerializationWriter.writeBytes(pointer, data, (short) set.size());
        for (ItemFlag flag : set)
            pointer = SerializationWriter.writeBytes(pointer, data, (short) flag.ordinal());
        return data;
    }

    private static final byte[] serializeBannerMeta(BannerMeta bMeta) {
        byte[] data = new byte[BANNERMETA.length + Short.BYTES * 2 + bMeta.getPatterns().size() * 4];
        int pointer = SerializationWriter.writeBytes(0, data, BANNERMETA);
        if (bMeta.getBaseColor() != null)
            pointer = SerializationWriter.writeBytes(pointer, data, (short) bMeta.getBaseColor().ordinal());
        else
            pointer = SerializationWriter.writeBytes(pointer, data, (short) -1);
        pointer = SerializationWriter.writeBytes(pointer, data, (short) bMeta.getPatterns().size());
        for (Pattern pattern : bMeta.getPatterns()) {
            pointer = SerializationWriter.writeBytes(pointer, data, (short) pattern.getColor().ordinal());
            pointer = SerializationWriter.writeBytes(pointer, data, (short) pattern.getPattern().ordinal());
        }
        return data;
    }

    private static final byte[] serializeBookMeta(BookMeta bMeta) {
        int size = 2;
        if (bMeta.hasAuthor())
            size += bMeta.getAuthor().length();
        size += 2;
        if (bMeta.hasTitle())
            size += bMeta.getTitle().length();
        size += 2;
        if (bMeta.hasPages())
            size += 2;
        for (String page : bMeta.getPages())
            size += page.length() + 2;

        byte[] data = new byte[size];
        int pointer = SerializationWriter.writeBytes(0, data, BOOKMETA);
        if (bMeta.hasAuthor())
            pointer = SerializationWriter.writeBytes(pointer, data, bMeta.getAuthor());
        else
            pointer = SerializationWriter.writeBytes(pointer, data, "");
        if (bMeta.hasTitle())
            pointer = SerializationWriter.writeBytes(pointer, data, bMeta.getTitle());
        else
            pointer = SerializationWriter.writeBytes(pointer, data, "");
        if (bMeta.hasPages()) {
            pointer = SerializationWriter.writeBytes(pointer, data, (short) bMeta.getPageCount());
            for (String page : bMeta.getPages())
                pointer = SerializationWriter.writeBytes(pointer, data, page);
        } else
            pointer = SerializationWriter.writeBytes(pointer, data, (short) 0);
        return data;
    }

    @SuppressWarnings("deprecation")
    private static byte[] serializeEnchantmentStorageMeta(EnchantmentStorageMeta meta) {
        byte[] data = new byte[4 + (meta.getStoredEnchants().size() * 4)];
        int pointer = SerializationWriter.writeBytes(0, data, ENCHANTMENTSTORAGEMETA);
        pointer = SerializationWriter.writeBytes(pointer, data, (short) meta.getStoredEnchants().size());
        for (Enchantment ench : meta.getStoredEnchants().keySet()) {
            pointer = SerializationWriter.writeBytes(pointer, data, (short) ench.getId());
            pointer = SerializationWriter.writeBytes(pointer, data, (short) meta.getStoredEnchantLevel(ench));
        }
        meta.getStoredEnchants();
        return data;
    }

    @SuppressWarnings("unused")
    private static byte[] serializeFireworkMeta(FireworkMeta meta) {
        int size = 6;
        for (FireworkEffect effect : meta.getEffects()) {
            size += 8;
            for (Color color : effect.getColors())
                size += Integer.BYTES;
            for (Color color : effect.getFadeColors())
                size += Integer.BYTES;
        }
        byte[] data = new byte[size];
        int pointer = SerializationWriter.writeBytes(0, data, FIREWORKMETA);
        pointer = SerializationWriter.writeBytes(pointer, data, (short) meta.getPower());
        pointer = SerializationWriter.writeBytes(pointer, data, (short) meta.getEffects().size());
        for (FireworkEffect effect : meta.getEffects()) {
            pointer = SerializationWriter.writeBytes(pointer, data, (short) effect.getType().ordinal());
            pointer = SerializationWriter.writeBytes(pointer, data, effect.hasTrail());
            pointer = SerializationWriter.writeBytes(pointer, data, effect.hasFlicker());
            pointer = SerializationWriter.writeBytes(pointer, data, (short) effect.getColors().size());
            for (Color color : effect.getColors())
                pointer = SerializationWriter.writeBytes(pointer, data, color.asRGB());
            pointer = SerializationWriter.writeBytes(pointer, data, (short) effect.getFadeColors().size());
            for (Color color : effect.getFadeColors())
                pointer = SerializationWriter.writeBytes(pointer, data, color.asRGB());
        }
        return data;
    }

    private static byte[] serializeLeatherArmorMeta(LeatherArmorMeta meta) {
        byte[] data = new byte[6];
        int pointer = SerializationWriter.writeBytes(0, data, LEATHERARMORMETA);
        pointer = SerializationWriter.writeBytes(pointer, data, meta.getColor().asRGB());
        return data;
    }

    private static byte[] serializeMapMeta(MapMeta meta) {
        byte[] data = new byte[3];
        int pointer = SerializationWriter.writeBytes(0, data, MAPMETA);
        pointer = SerializationWriter.writeBytes(pointer, data, meta.isScaling());
        return data;
    }

    @SuppressWarnings("deprecation")
    private static byte[] serializePotionMeta(PotionMeta meta) {
        byte[] data = new byte[POTIONMETA.length + Short.BYTES
                + (meta.getCustomEffects().size() * (Short.BYTES * 3 + 2))];
        int pointer = SerializationWriter.writeBytes(0, data, POTIONMETA);
        pointer = SerializationWriter.writeBytes(pointer, data, (short) meta.getCustomEffects().size());
        for (PotionEffect effect : meta.getCustomEffects()) {
            pointer = SerializationWriter.writeBytes(pointer, data, (short) effect.getType().getId());
            pointer = SerializationWriter.writeBytes(pointer, data, (short) effect.getAmplifier());
            pointer = SerializationWriter.writeBytes(pointer, data, (short) effect.getDuration());
            pointer = SerializationWriter.writeBytes(pointer, data, effect.isAmbient());
            pointer = SerializationWriter.writeBytes(pointer, data, effect.hasParticles());
        }
        return data;
    }

    private static byte[] serializeSkullMeta(ItemStack item, SkullMeta meta) {
        // NBT tag, because skulls are badly supported
        net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = stack.hasTag() ? stack.getTag() : new NBTTagCompound();

        String id = tag.getCompound("SkullOwner").getString("Id").equals("") ? null
                : tag.getCompound("SkullOwner").getString("Id");
        String name = tag.getCompound("SkullOwner").getString("Name").equals("") ? null
                : tag.getCompound("SkullOwner").getString("Name");
        String texture = tag.getCompound("SkullOwner").getCompound("Properties").getList("textures", 10).get(0)
                .getString("Value").equals("") ? null
                        : tag.getCompound("SkullOwner").getCompound("Properties").getList("textures", 10).get(0)
                                .getString("Value");
        byte[] data = meta.hasOwner() && id != null && name != null && texture != null
                ? new byte[SKULLMETA.length + 1 + Short.BYTES * 3 + id.length() + name.length() + texture.length()]
                : new byte[SKULLMETA.length + 1];
        int pointer = SerializationWriter.writeBytes(0, data, SKULLMETA);
        pointer = SerializationWriter.writeBytes(pointer, data,
                (meta.hasOwner() && id != null && name != null && texture != null));
        if (meta.hasOwner() && id != null && name != null && texture != null) {
            pointer = SerializationWriter.writeBytes(pointer, data, id);
            pointer = SerializationWriter.writeBytes(pointer, data, name);
            pointer = SerializationWriter.writeBytes(pointer, data, texture);
        }
        return data;
    }

    @SuppressWarnings("deprecation")
    private static Material deserializeMaterial(int i, byte[] src) {
        short itemId = SerializationReader.readShort(i += MATERIAL.length, src);
        return Material.getMaterial(itemId);
    }

    private static int deserializeAmount(int i, byte[] src) {
        return SerializationReader.readShort(i += AMOUNT.length, src);
    }

    private static short deserializeDurability(int i, byte[] src) {
        return SerializationReader.readShort(i += DURABILITY.length, src);
    }

    private static byte deserializeMaterialData(int i, byte[] src) {
        return Byte.valueOf((byte) SerializationReader.readByte(i += MATERIALDATA.length, src));
    }

    @SuppressWarnings("deprecation")
    private static Map<Enchantment, Integer> deserializeEnchantments(int i, byte[] src) {
        Map<Enchantment, Integer> enchants = new HashMap<Enchantment, Integer>();
        short length = SerializationReader.readShort(i += ENCHANTS.length, src);
        for (int x = 0; x < length; x++) {
            int enchantId = SerializationReader.readByte(i += Short.BYTES, src);
            short enchantLvl = SerializationReader.readShort(++i, src);
            enchants.put(Enchantment.getById(enchantId), new Integer(enchantLvl));
        }
        return enchants;
    }

    private static String deserizlizeDisplayName(int i, byte[] src) {
        i += DISPLAYNAME.length;
        return SerializationReader.readString(i, src);
    }

    private static List<String> deserializeLore(int i, byte[] src) {
        List<String> lore = new ArrayList<String>();
        short length = SerializationReader.readShort(i += LORE.length, src);
        for (int x = 0; x < length; x++) {
            String line = SerializationReader.readString(i += Short.BYTES, src);
            lore.add(line);
            i += line.length();
        }
        return lore;
    }

    private static List<ItemFlag> deserializeItemFlags(int i, byte[] src) {
        List<ItemFlag> flags = new ArrayList<ItemFlag>();
        short length = SerializationReader.readShort(i += ITEMFLAGS.length, src);
        for (int x = 0; x < length; x++) {
            short flagId = SerializationReader.readShort(i += Short.BYTES, src);
            flags.add(ItemFlag.values()[flagId]);
        }
        return flags;
    }

    private static DyeColor deserializeBaseColor(int i, byte[] src) {
        DyeColor baseColor = null;
        short baseID = SerializationReader.readShort(i += BANNERMETA.length, src);
        if (baseID != -1)
            baseColor = DyeColor.values()[baseID];
        return baseColor;
    }

    private static List<Pattern> deserializePatterns(int i, byte[] src) {
        List<Pattern> patterns = new ArrayList<Pattern>();
        short length = SerializationReader.readShort(i += Short.BYTES, src);
        for (int x = 0; x < length; x++) {
            short color = SerializationReader.readShort(i += Short.BYTES, src);
            short pattern = SerializationReader.readShort(i += Short.BYTES, src);
            patterns.add(new Pattern(DyeColor.values()[color], PatternType.values()[pattern]));
        }
        return patterns;
    }

    private static String deserializeAuthor(int i, byte[] src) {
        return SerializationReader.readString(i += BOOKMETA.length, src);
    }

    private static String deserializeTitle(int i, byte[] src) {
        return SerializationReader.readString(i, src);
    }

    private static List<String> deserializePages(int i, byte[] src) {
        List<String> pages = new ArrayList<String>();
        short length = SerializationReader.readShort(i, src);
        for (int x = 0; x < length; x++) {
            String current = SerializationReader.readString(i += Short.BYTES, src);
            pages.add(current);
            i += current.length();
        }
        return pages;
    }

    @SuppressWarnings("deprecation")
    private static Map<Enchantment, Integer> deserializeStoredEnchants(int i, byte[] src) {
        Map<Enchantment, Integer> enchants = new HashMap<Enchantment, Integer>();
        short length = SerializationReader.readShort(i += Short.BYTES, src);
        for (int x = 0; x < length; x++) {
            short enchantID = SerializationReader.readShort(i += Short.BYTES, src);
            short level = SerializationReader.readShort(i += 2, src);
            enchants.put(Enchantment.getById(enchantID), (int) level);
        }
        return enchants;
    }

    private static int deserializePower(int i, byte[] src) {
        return SerializationReader.readShort(i + 2, src);
    }

    private static List<FireworkEffect> deserializeFireworkEffects(int i, byte[] src) {
        List<FireworkEffect> effects = new ArrayList<FireworkEffect>();
        short length = SerializationReader.readShort(i, src);
        i += 2;
        for (int x = 0; x < length; x++) {
            Type effectID = Type.values()[SerializationReader.readShort(i, src)];
            boolean trail = SerializationReader.readBoolean(i += 2, src);
            boolean flicker = SerializationReader.readBoolean(++i, src);
            List<Color> colors = new ArrayList<Color>();
            List<Color> fadeColors = new ArrayList<Color>();
            short lengthColor = SerializationReader.readShort(++i, src);
            i += 2;
            for (int z = 0; z < lengthColor; z++) {
                colors.add(Color.fromRGB(SerializationReader.readInt(i, src)));
                i += 4;
            }
            short lengthFadeColor = SerializationReader.readShort(i, src);
            i += 2;
            for (int z = 0; z < lengthFadeColor; z++) {
                fadeColors.add(Color.fromRGB(SerializationReader.readInt(i, src)));
                i += 4;
            }
            effects.add(FireworkEffect.builder().flicker(flicker).trail(trail).withColor(colors).withFade(fadeColors)
                    .with(effectID).build());
        }
        return effects;
    }

    private static Color deserializeLeatherArmorColor(int i, byte[] src) {
        return Color.fromRGB(SerializationReader.readInt(i + LEATHERARMORMETA.length, src));
    }

    private static boolean deserializeMapScaling(int i, byte[] src) {
        return SerializationReader.readBoolean(i + MAPMETA.length, src);
    }

    @SuppressWarnings("deprecation")
    private static List<PotionEffect> deserializeCustomPotions(int i, byte[] src) {
        List<PotionEffect> effects = new ArrayList<PotionEffect>();
        short length = SerializationReader.readShort(i += Short.BYTES, src);
        for (int x = 0; x < length; x++) {
            short effectID = SerializationReader.readShort(i += Short.BYTES, src);
            short amplifier = SerializationReader.readShort(i += Short.BYTES, src);
            short duration = SerializationReader.readShort(i += Short.BYTES, src);
            boolean ambiant = SerializationReader.readBoolean(i += Short.BYTES, src);
            boolean particles = SerializationReader.readBoolean(++i, src);
            effects.add(new PotionEffect(PotionEffectType.getById(effectID), duration, amplifier, ambiant, particles));
        }
        return effects;
    }

    private static NBTTagCompound deserializeSkullData(int i, byte[] src) {
        if (!SerializationReader.readBoolean(i += 2, src))
            return null;
        String id = SerializationReader.readString(++i, src);
        String name = SerializationReader.readString(i += id.length() + 2, src);
        String texture = SerializationReader.readString(i += name.length() + 2, src);
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList textures = new NBTTagList();
        textures.add(new NBTTagCompound());
        textures.get(0).setString("Value", texture); // set this to the
        NBTTagCompound properties = new NBTTagCompound();
        properties.set("textures", textures);
        NBTTagCompound skullowner = new NBTTagCompound();
        skullowner.setString("Id", id);
        skullowner.setString("Name", name);
        skullowner.set("Properties", properties);

        tag.set("SkullOwner", skullowner);
        return tag;
    }

}
