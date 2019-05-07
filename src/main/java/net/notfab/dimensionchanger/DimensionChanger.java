package net.notfab.dimensionchanger;

import lombok.Getter;
import net.notfab.dimensionchanger.listener.Listeners;
import net.notfab.spigot.simpleconfig.SimpleConfig;
import net.notfab.spigot.simpleconfig.SimpleConfigManager;
import net.notfab.spigot.simpleconfig.spigot.SpigotConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class DimensionChanger extends JavaPlugin {

    @Getter
    private static DimensionChanger Instance;

    @Getter
    private SimpleConfigManager simpleConfigManager;
    @Getter
    private NMSHandler nmsHandler;
    @Getter
    private Listeners listeners;
    @Getter
    private SimpleConfig simpleConfig;

    @Override
    public void onEnable() {
        Instance = this;
        this.simpleConfigManager = new SpigotConfigManager(this);
        this.nmsHandler = new NMSHandler(getLogger());
        this.simpleConfig = this.setupConfig();
        this.listeners = new Listeners(this.simpleConfig, this.nmsHandler);
        getCommand("dc").setExecutor(new DimensionCommand(this.simpleConfig, this.nmsHandler));
        getServer().getPluginManager().registerEvents(this.listeners, this);
    }

    @Override
    public void onDisable() {
        this.listeners.onDisable();
    }

    /**
     * Setups the config files.
     *
     * @return SimpleConfig.
     */
    private SimpleConfig setupConfig() {
        File file = new File(this.getDataFolder(), "config.yml");
        if (!file.exists()) {
            this.simpleConfigManager.copyResource(getClass().getResourceAsStream("/config.yml"), file);
        }
        return this.simpleConfigManager.getNewConfig("config.yml");
    }

}