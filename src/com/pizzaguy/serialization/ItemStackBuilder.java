package com.pizzaguy.serialization;

import java.util.List;
import java.util.Map;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
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

import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class ItemStackBuilder {
    // standart values
    private ItemStack item;
    private Material material = Material.AIR;
    private int amount;
    private short durability;
    private byte data;
    // standart meta
    private String name;
    private Map<Enchantment, Integer> enchants;
    private List<String> lore;
    private List<ItemFlag> flags;
    // banner meta
    private DyeColor base;
    private List<Pattern> patterns;
    // book meta
    private String author;
    private String title;
    private List<String> pages;
    // stored enchantment meta
    private Map<Enchantment, Integer> storedEnchants;
    // Fireworks meta
    private int power;
    private List<FireworkEffect> effects;
    // leather dye color meta
    private Color color;
    // map meta
    boolean scaling;
    // potion meta
    private List<PotionEffect> potions;
    // skull meta
    private NBTTagCompound tag;

    public ItemStackBuilder() {
        item = new ItemStack(Material.AIR);
    }

    @SuppressWarnings("deprecation")
    public ItemStack build() {
        item = new ItemStack(material, amount);
        item.getData().setData(data);
        item.setDurability(durability);

        if (item.getItemMeta() instanceof BannerMeta) {
            BannerMeta meta = (BannerMeta) item.getItemMeta();
            if (base != null)
                meta.setBaseColor(base);
            if (patterns != null)
                meta.setPatterns(patterns);
            item.setItemMeta(meta);
        } else if (item.getItemMeta() instanceof BookMeta) {
            BookMeta meta = (BookMeta) item.getItemMeta();
            if (author != null)
                meta.setAuthor(author);
            if (title != null)
                meta.setTitle(title);
            if (pages != null)
                meta.setPages(pages);
            item.setItemMeta(meta);
        } else if (item.getItemMeta() instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
            if (storedEnchants != null)
                for (Enchantment ench : storedEnchants.keySet())
                    meta.addStoredEnchant(ench, storedEnchants.get(ench).intValue(), true);
            item.setItemMeta(meta);
        } else if (item.getItemMeta() instanceof FireworkMeta) {
            FireworkMeta meta = (FireworkMeta) item.getItemMeta();
            if (effects != null)
                meta.addEffects(effects);
            meta.setPower(power);
            item.setItemMeta(meta);
        } else if (item.getItemMeta() instanceof LeatherArmorMeta) {
            LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
            if (color != null)
                meta.setColor(color);
            item.setItemMeta(meta);
        } else if (item.getItemMeta() instanceof MapMeta) {
            MapMeta meta = (MapMeta) item.getItemMeta();
            meta.setScaling(scaling);
            item.setItemMeta(meta);
        } else if (item.getItemMeta() instanceof PotionMeta) {
            PotionMeta meta = (PotionMeta) item.getItemMeta();
            if (potions != null)
                for (PotionEffect effect : potions)
                    meta.addCustomEffect(effect, true);
            item.setItemMeta(meta);
        } else if (item.getItemMeta() instanceof SkullMeta) {
            if (tag != null){
                net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(item);
                stack.setTag(tag);
                item = CraftItemStack.asBukkitCopy(stack);
            }
        }
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        for (Enchantment enchant : enchants.keySet())
            meta.addEnchant(enchant, enchants.get(enchant), true);
        meta.setLore(lore);
        for (ItemFlag flag : flags)
            meta.addItemFlags(flag);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStackBuilder setType(Material material) {
        this.material = material;
        return this;
    }

    public ItemStackBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemStackBuilder setDurability(short durability) {
        this.durability = durability;
        return this;
    }

    public ItemStackBuilder setData(byte data) {
        this.data = data;
        return this;
    }

    public ItemStackBuilder setDisplayName(String name) {
        this.name = name;
        return this;
    }

    public ItemStackBuilder addEnchantments(Map<Enchantment, Integer> enchants) {
        this.enchants = enchants;
        return this;
    }

    public ItemStackBuilder setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemStackBuilder setFlags(List<ItemFlag> flags) {
        this.flags = flags;
        return this;
    }

    public ItemStackBuilder setBannerData(DyeColor base, List<Pattern> patterns) {
        this.base = base;
        this.patterns = patterns;
        return this;
    }

    public ItemStackBuilder setBookData(String author, String title, List<String> pages) {
        this.author = author;
        this.title = title;
        this.pages = pages;
        return this;
    }

    public ItemStackBuilder setStoredEnchantments(Map<Enchantment, Integer> storedEnchants) {
        this.storedEnchants = storedEnchants;
        return this;
    }

    public ItemStackBuilder setFireworkData(int power, List<FireworkEffect> effects) {
        this.power = power;
        this.effects = effects;
        return this;
    }

    public ItemStackBuilder setLeatherArmorColor(Color color) {
        this.color = color;
        return this;
    }

    public ItemStackBuilder setMapScaling(boolean scaling) {
        this.scaling = scaling;
        return this;
    }

    public ItemStackBuilder setCustomPotions(List<PotionEffect> potions) {
        this.potions = potions;
        return this;
    }

    public ItemStackBuilder setSkullData(NBTTagCompound tag) {
        this.tag = tag;
        return this;
    }
}
