package com.pizzaguy.serialization;

import java.util.List;
import java.util.Map;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackBuilder {

	private ItemStack item;

	public ItemStackBuilder() {
		item = new ItemStack(Material.AIR);
	}

	public ItemStack build() {
		return item;
	}

	public ItemStackBuilder setType(Material material) {
		item.setType(material);
		return this;
	}

	public ItemStackBuilder setAmount(int amount) {
		item.setAmount(amount);
		return this;
	}

	public ItemStackBuilder setDurability(short durability) {
		item.setDurability(durability);
		return this;

	}

	public ItemStackBuilder setData(byte data) {
		item.getData().setData(data);
		return this;
	}

	public ItemStackBuilder setDisplayName(String name) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return this;
	}

	public ItemStackBuilder addEnchantments(Map<Enchantment, Integer> enchants) {
		item.addEnchantments(enchants);
		return this;
	}

	public ItemStackBuilder setLore(List<String> lore) {
		ItemMeta meta = item.getItemMeta();
		meta.setLore(lore);
		item.setItemMeta(meta);
		return this;
	}

	public ItemStackBuilder setFlags(List<ItemFlag> flags) {
		ItemMeta meta = item.getItemMeta();
		if (flags.size() != 0)
			for (ItemFlag flag : flags)
				meta.addItemFlags(flag);
		item.setItemMeta(meta);
		return this;
	}

	public ItemStackBuilder setBannerData(DyeColor base, List<Pattern> patterns) {
		if (item.getItemMeta() instanceof BannerMeta) {
			BannerMeta meta = (BannerMeta) item.getItemMeta();
			meta.setBaseColor(base);
			meta.setPatterns(patterns);
			item.setItemMeta(meta);
		}
		return this;
	}

	public ItemStackBuilder setBookData(String author, String title, List<String> pages) {
		if (item.getItemMeta() instanceof BookMeta) {
			BookMeta meta = (BookMeta) item.getItemMeta();
			meta.setAuthor(author);
			meta.setTitle(title);
			meta.setPages(pages);
			item.setItemMeta(meta);
		}
		return this;
	}

	public ItemStackBuilder setStoredEnchantments(Map<Enchantment, Integer> storedEnchants) {
		if (item.getItemMeta() instanceof EnchantmentStorageMeta) {
			EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
			for (Enchantment ench : storedEnchants.keySet())
				meta.addStoredEnchant(ench, storedEnchants.get(ench).intValue(), true);
			item.setItemMeta(meta);
		}
		return this;
	}
}
