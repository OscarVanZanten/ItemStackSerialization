package com.pizzaguy.test;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import com.pizzaguy.serialization.ItemStackSerialization;
import com.pizzaguy.serialization.builder.ByteArrayBuilder;

import net.minecraft.server.v1_9_R1.NBTTagCompound;

public class Test extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // register events
        Bukkit.getPluginManager().registerEvents(this, this);

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        // serialize and deserialize items to the first slot in inventory
        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
            byte[] data = ItemStackSerialization.serialize(e.getItem());
            e.getPlayer().getInventory().setItem(0, ItemStackSerialization.deserialize(data));
        }
    }
}
