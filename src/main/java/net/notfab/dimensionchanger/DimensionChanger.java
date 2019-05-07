package net.notfab.dimensionchanger;

import lombok.Getter;
import net.notfab.dimensionchanger.listener.Listeners;
import net.notfab.spigot.simpleconfig.SimpleConfig;
import net.notfab.spigot.simpleconfig.SimpleConfigManager;
import net.notfab.spigot.simpleconfig.spigot.SpigotConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private SimpleConfig setupConfig() {
        File file = new File("config.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
                InputStream stream = getClass().getResourceAsStream("/config.yml");
                this.simpleConfigManager.copyResource(stream, file);
            } catch (IOException e) {
                getLogger().severe(e.getMessage());
            }
        }
        return this.simpleConfigManager.getNewConfig("config.yml");
    }

}