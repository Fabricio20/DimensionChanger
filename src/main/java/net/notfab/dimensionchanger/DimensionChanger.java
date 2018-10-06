package net.notfab.dimensionchanger;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class DimensionChanger extends JavaPlugin {

    @Getter private static DimensionChanger Instance;

    @Override
    public void onEnable() {
        Instance = this;
        //manager = new SimpleConfigManager(this);
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
        getServer().getPluginManager().registerEvents(new SessionListener(), this);
    }

    @Override
    public void onDisable() {
        Instance = null;
    }

}