/*
 * Copyright (C) SainttX <http://sainttx.com>
 * Copyright (C) contributors
 *
 * This file is part of Auctions.
 *
 * Auctions is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Auctions is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Auctions.  If not, see <http://www.gnu.org/licenses/>.
 */
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
    private Material material;
    private boolean materialSet;
    private int amount;
    private boolean amountSet;
    private short durability;
    private boolean durabilitySet;
    private byte data;
    private boolean dataSet;
    // standart meta
    private String name;
    private boolean nameSet;
    private Map<Enchantment, Integer> enchants;
    private boolean enchantsSet;
    private List<String> lore;
    private boolean loreSet;
    private List<ItemFlag> flags;
    private boolean flagsSet;
    // banner meta
    private boolean bannerSet;
    private DyeColor base;
    private List<Pattern> patterns;
    // book meta
    private boolean bookSet;
    private String author;
    private String title;
    private List<String> pages;
    // stored enchantment meta
    private boolean storedEnchantsSet;
    private Map<Enchantment, Integer> storedEnchants;
    // Fireworks meta
    private boolean fireworkSet;
    private int power;
    private List<FireworkEffect> effects;
    // leather dye color meta
    private boolean leatherSet;
    private Color color;
    // map meta
    private boolean mapSet;
    boolean scaling;
    // potion meta
    private boolean potionSet;
    private List<PotionEffect> potions;
    // skull meta
    private boolean skullSet;
    private NBTTagCompound tag;

    public ItemStackBuilder() {
        item = new ItemStack(Material.AIR);
    }

    @SuppressWarnings("deprecation")
    public ItemStack build() {
        if (material == null)
            return null;
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
            if (tag != null) {
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
        this.materialSet = true;
        return this;
    }

    public ItemStackBuilder setAmount(int amount) {
        this.amount = amount;
        this.amountSet = true;
        return this;
    }

    public ItemStackBuilder setDurability(short durability) {
        this.durability = durability;
        this.durabilitySet = true;
        return this;
    }

    public ItemStackBuilder setData(byte data) {
        this.data = data;
        this.dataSet = true;
        return this;
    }

    public ItemStackBuilder setDisplayName(String name) {
        this.name = name;
        this.nameSet = true;
        return this;
    }

    public ItemStackBuilder addEnchantments(Map<Enchantment, Integer> enchants) {
        this.enchants = enchants;
        this.enchantsSet = true;
        return this;
    }

    public ItemStackBuilder setLore(List<String> lore) {
        this.lore = lore;
        this.loreSet = true;
        return this;
    }

    public ItemStackBuilder setFlags(List<ItemFlag> flags) {
        this.flags = flags;
        this.flagsSet = true;
        return this;
    }

    public ItemStackBuilder setBannerData(DyeColor base, List<Pattern> patterns) {
        this.base = base;
        this.patterns = patterns;
        this.bannerSet = true;
        return this;
    }

    public ItemStackBuilder setBookData(String author, String title, List<String> pages) {
        this.author = author;
        this.title = title;
        this.pages = pages;
        this.bookSet = true;
        return this;
    }

    public ItemStackBuilder setStoredEnchantments(Map<Enchantment, Integer> storedEnchants) {
        this.storedEnchants = storedEnchants;
        this.storedEnchantsSet =true;
        return this;
    }

    public ItemStackBuilder setFireworkData(int power, List<FireworkEffect> effects) {
        this.power = power;
        this.effects = effects;
        this.fireworkSet =true;
        return this;
    }

    public ItemStackBuilder setLeatherArmorColor(Color color) {
        this.color = color;
        this.leatherSet = true;
        return this;
    }

    public ItemStackBuilder setMapScaling(boolean scaling) {
        this.scaling = scaling;
        this.mapSet = true;
        return this;
    }

    public ItemStackBuilder setCustomPotions(List<PotionEffect> potions) {
        this.potions = potions;
        this.potionSet = true;
        return this;
    }

    public ItemStackBuilder setSkullData(NBTTagCompound tag) {
        this.tag = tag;
        this.skullSet = true;
        return this;
    }

    public boolean isMaterialSet() {
        return materialSet;
    }

    public boolean isAmountSet() {
        return amountSet;
    }

    public boolean isDurabilitySet() {
        return durabilitySet;
    }

    public boolean isNameSet() {
        return nameSet;
    }

    public boolean isEnchantsSet() {
        return enchantsSet;
    }

    public boolean isLoreSet() {
        return loreSet;
    }

    public boolean isFlagsSet() {
        return flagsSet;
    }

    public boolean isBannerSet() {
        return bannerSet;
    }

    public boolean isBookSet() {
        return bookSet;
    }

    public boolean isStoredEnchantsSet() {
        return storedEnchantsSet;
    }

    public boolean isFireworkSet() {
        return fireworkSet;
    }

    public boolean isLeatherSet() {
        return leatherSet;
    }

    public boolean isMapSet() {
        return mapSet;
    }

    public boolean isPotionSet() {
        return potionSet;
    }

    public boolean isSkullSet() {
        return skullSet;
    }

    /**
     * @return the dataSet
     */
    public boolean isDataSet() {
        return dataSet;
    }
}
