package net.notfab.dimensionchanger.nms;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReflectionUtils {

    public static String getServerVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().substring(23);
    }

    public static Object getCraftPlayer(Player player) throws ClassNotFoundException {
        Class clazz = Class.forName("net.minecraft.server." + getServerVersion() + ".entity.CraftPlayer");
        return clazz.cast(player);
    }

    public static Constructor<?> getPacket() throws ClassNotFoundException, NoSuchMethodException {
        Class clazz = Class.forName("net.minecraft.server." + getServerVersion() + ".PacketPlayOutRespawn");
        return clazz
                .getDeclaredConstructor(int.class, getEnumDifficulty(), getWorldType(), getEnumGameMode());
    }

    public static Class<?> getWorldType() throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + getServerVersion() + ".WorldType");
    }

    public static Object getWorldTypeNormal() {
        Class<?> clazz;
        try {
            clazz = getWorldType();
            return clazz.getEnumConstants()[0];
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> getEnumDifficulty() throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + getServerVersion() + ".EnumDifficulty");
    }

    public static Object getEnumDifficulty(int diff) {
        Class<?> clazz;
        try {
            clazz = getEnumDifficulty();
            return clazz.getDeclaredMethod("getById", int.class).invoke(clazz, diff);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> getEnumGameMode() throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + getServerVersion() + ".WorldSettings.EnumGamemode");
    }

    public static Object getEnumGameMode(int gamemode) {
        Class<?> clazz;
        try {
            clazz = getEnumDifficulty();
            return clazz.getDeclaredMethod("getById", int.class).invoke(clazz, gamemode);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void sendPacket(Player player, Object craftPlayer, Integer dimension) {
        try {
            Constructor constructor = getPacket();
            Object packet = constructor.newInstance(dimension, getEnumDifficulty(player.getLocation()
                            .getWorld().getDifficulty().getValue()), getWorldTypeNormal(),
                    getEnumGameMode(player.getGameMode().getValue()));
            Object handle = craftPlayer.getClass().getDeclaredMethod("getHandle").invoke(craftPlayer);
            Object playerConnection = handle.getClass().getDeclaredField("playerConnection").get(handle);
            playerConnection.getClass().getDeclaredMethod("sendPacket", packet.getClass()).invoke(playerConnection, packet);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException
                | InstantiationException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        // -
        Chunk chunk = player.getWorld().getChunkAt(player.getLocation());
        for (int x = -10; x < 10; x++) {
            for (int z = -10; z < 10; z++) {
                player.getWorld().refreshChunk(chunk.getX() + x, chunk.getZ() + z);
            }
        }
    }

}