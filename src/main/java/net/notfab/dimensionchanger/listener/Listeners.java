package net.notfab.dimensionchanger.listener;

import net.notfab.dimensionchanger.NMSHandler;
import net.notfab.spigot.simpleconfig.SimpleConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class Listeners implements Listener {

    private final SimpleConfig config;
    private final NMSHandler nmsHandler;

    public Listeners(SimpleConfig config, NMSHandler nmsHandler) {
        this.config = config;
        this.nmsHandler = nmsHandler;
    }

    /**
     * Unregister the listeners.
     */
    public void onDisable() {
        PlayerJoinEvent.getHandlerList().unregister(this);
        PlayerChangedWorldEvent.getHandlerList().unregister(this);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        int dimension = config.getInt("Dimensions." + event.getPlayer().getWorld().getName());
        if (dimension >= 0) {
            this.nmsHandler.setDimension(event.getPlayer(), dimension);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorld(PlayerChangedWorldEvent event) {
        int dimension = config.getInt("Dimensions." + event.getPlayer().getWorld().getName());
        if (dimension >= 0) {
            this.nmsHandler.setDimension(event.getPlayer(), dimension);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRespawn(PlayerRespawnEvent event) {
        int dimension = config.getInt("Dimensions." + event.getPlayer().getWorld().getName());
        if (dimension >= 0) {
            this.nmsHandler.setDimension(event.getPlayer(), dimension);
        }
    }

}
