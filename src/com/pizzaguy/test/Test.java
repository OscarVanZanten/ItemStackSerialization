package com.pizzaguy.test;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.pizzaguy.serialization.ItemStackSerialization;
import com.pizzaguy.serialization.builder.ByteArrayBuilder;

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
            byte[] data = ItemStackSerialization.serializeArray(e.getPlayer().getInventory().getContents());
            System.out.println(new String(data));
            Inventory inv = Bukkit.createInventory(null, 45);
            inv.setContents(ItemStackSerialization.deserializeArray(data));
            e.getPlayer().openInventory(inv);
        }
    }

}
