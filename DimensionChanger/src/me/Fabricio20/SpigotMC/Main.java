package me.Fabricio20.SpigotMC;

import me.Fabricio20.SpigotMC.Config.SimpleConfig;
import me.Fabricio20.SpigotMC.Config.SimpleConfigManager;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	public static Main theClass;
	
	public SimpleConfigManager manager;
	public SimpleConfig config;
	
	@Override
	public void onEnable() {
		theClass = this;
		manager = new SimpleConfigManager(this);
		config = manager.getNewConfig("config.yml", new String[]{"-------------------", "Dimension Changer!", "-------------------"});
		config.saveConfig();
		if(!config.contains("Permission")) {
			config.set("Permission", "DimensionChanger.Change");
		}
		if(!config.contains("NoPerm")) {
			config.set("NoPerm", "&cYou dont have enough perms to use this sir!");
		}
		config.saveConfig();
		getCommand("dc").setExecutor(new DimensionCommand());
		getServer().getPluginManager().registerEvents(new Listeners(), this);
	}
	
	@Override
	public void onDisable() {
		theClass = null;
	}
	
}
