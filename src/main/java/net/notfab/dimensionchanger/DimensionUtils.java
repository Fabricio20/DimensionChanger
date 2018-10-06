package net.notfab.dimensionchanger;

import net.notfab.dimensionchanger.nms.ReflectionUtils;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class DimensionUtils {

    public static void setDimension(Player player, Integer dimension) {
        try {
            ReflectionUtils.sendPacket(player, ReflectionUtils.getCraftPlayer(player), dimension);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public static void setDimension(World world, Integer dimension) {
        world.getPlayers().forEach(player -> {
            try {
                ReflectionUtils.sendPacket(player, ReflectionUtils.getCraftPlayer(player), dimension);
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });
    }

}
