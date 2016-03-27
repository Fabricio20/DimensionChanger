package me.Fabricio20.SpigotMC;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class Listeners implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if(Main.theClass.config.contains("Dimensions." + e.getPlayer().getWorld().getName())) {
			Integer dimension = Main.theClass.config.getInt("Dimensions." + e.getPlayer().getWorld().getName());
			DimensionUtils.setDimension(e.getPlayer(), dimension);
		}
	}
	
	@EventHandler
	public void onWorld(PlayerChangedWorldEvent e) {
		if(Main.theClass.config.contains("Dimensions." + e.getPlayer().getWorld().getName())) {
			Integer dimension = Main.theClass.config.getInt("Dimensions." + e.getPlayer().getWorld().getName());
			DimensionUtils.setDimension(e.getPlayer(), dimension);
		}
	}
	
}
