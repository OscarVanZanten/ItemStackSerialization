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

public class Test extends JavaPlugin implements Listener{

	public static void main(String[] args) {
		ItemStack item = new ItemStack(Material.FIREWORK);
		FireworkMeta meta = (FireworkMeta) item.getItemMeta();
		meta.addEffect(FireworkEffect.builder().flicker(false).trail(true).withColor(Color.RED).withFade(Color.BLUE).build());
		byte[] data = ItemStackSerialization.serialize(item);
		System.out.println(new String(data));
	}
	
	@Override
	public void onEnable(){
		Bukkit.getPluginManager().registerEvents(this, this);
		
		
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		byte[] data = ItemStackSerialization.serialize(e.getItem());
		System.out.println(new String(data));
		ItemStack item = ItemStackSerialization.deserialize(data);
		e.getPlayer().getInventory().setItem(e.getPlayer().getInventory().getHeldItemSlot() + 1, item);
	}

}
