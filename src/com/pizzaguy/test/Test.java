package com.pizzaguy.test;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.pizzaguy.serialization.ItemStackSerialization;
import com.pizzaguy.serialization.builder.ByteArrayBuilder;

public class Test extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        //register events
        Bukkit.getPluginManager().registerEvents(this, this);

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        //serialize and deserialize items to the first slot in inventory
        byte[] data = ItemStackSerialization.serialize(e.getItem());
        System.out.println(new String(data));
        e.getPlayer().getInventory().setItem(0, ItemStackSerialization.deserialize(data));
    }

}
