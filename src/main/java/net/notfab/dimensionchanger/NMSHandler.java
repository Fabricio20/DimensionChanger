package net.notfab.dimensionchanger;

import net.notfab.dimensionchanger.entities.NMSException;
import net.notfab.dimensionchanger.util.ReflectHelper;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Logger;

@SuppressWarnings("unchecked")
public class NMSHandler {

    private final String NMS_VERSION;
    private final int RADII;
    private Logger logger;

    NMSHandler(Logger logger) {
        this.logger = logger;
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        this.NMS_VERSION = packageName.substring(packageName.lastIndexOf('.') + 1);
        this.RADII = 16; // TODO: Config File
    }

    /**
     * Sets the player's dimension. A lot of things can fail.
     *
     * @param player    Player that will receive the changes.
     * @param dimension Dimension to set to.
     * @return If the dimension was set.
     */
    public boolean setDimension(Player player, int dimension) {
        Object packet = this.createPacket(player, dimension);
        if (packet == null) {
            return false;
        }
        boolean sent = this.sendPacket(player, packet);
        if (sent) {
            this.refreshChunks(player, this.RADII);
        }
        return sent;
    }

    /**
     * Creates a respawn packet with the appropriate dimension.
     *
     * @param player    Who will receive the packet
     * @param dimension The dimension.
     * @return Packet instance or null.
     */
    private Object createPacket(Player player, int dimension) {
        World world = player.getWorld();
        try {
            // Find packet constructor (hasn't changed since forever)
            // new PacketPlayOutRespawn(int dimension, EnumDifficulty diff, WorldType type, EnumGamemode gamemode)
            Constructor<?> constructor = ReflectHelper
                    .findConstructor(getPacketPlayOutRespawn(), Integer.class, getEnumDifficulty(), getClassWorldType(), getEnumGamemode());

            Object difficulty = this.castAsEnumDifficulty(this.getDifficulty(world));
            Object gamemode = this.castAsEnumGamemode(this.getGamemode(player));
            Object worldType = castAsWorldType(world.getWorldType().getName().toLowerCase());

            return ReflectHelper.instantiate(constructor, dimension, difficulty, worldType, gamemode);
        } catch (NMSException nmsException) {
            logger.severe(nmsException.getMessage());
            return null;
        }
    }

    /**
     * Sends a packet to a player.
     *
     * @param player Who to send the packet to.
     * @param packet The packet.
     * @return If the packet was sent.
     */
    private boolean sendPacket(Player player, Object packet) {
        try {
            // Find handle
            Method getHandle = ReflectHelper.findMethod("getHandle", player.getClass());
            Object handle = ReflectHelper.invoke(getHandle, player);

            // Find connection
            Field connectionField = ReflectHelper.findField("playerConnection", handle.getClass());
            Object playerConnection = ReflectHelper.getField(connectionField, handle);

            // Send packet
            Method sendPacket = ReflectHelper.findMethod("sendPacket", playerConnection.getClass(), getPacketSuperclass());
            ReflectHelper.invoke(sendPacket, playerConnection, packet);
            return true;
        } catch (NMSException ex) {
            logger.severe(ex.getMessage());
            return false;
        }
    }

    /**
     * Refreshes the chunks around the player.
     *
     * @param player - Player to refresh.
     */
    private void refreshChunks(Player player, int radii) {
        Chunk chunk = player.getWorld().getChunkAt(player.getLocation());
        for (int x = -radii; x < radii; x++) {
            for (int z = -radii; z < radii; z++) {
                player.getWorld().refreshChunk(chunk.getX() + x, chunk.getZ() + z);
            }
        }
    }

    /* ----------------- Helper fail-safes ----------------- */

    /**
     * Fail-safe way to get the difficulty.
     *
     * @param world World.
     * @return int of world difficulty.
     */
    private int getDifficulty(World world) {
        switch (world.getDifficulty()) {
            case PEACEFUL:
                return 0;
            case EASY:
                return 1;
            case NORMAL:
                return 2;
            case HARD:
                return 3;
            default: {
                try {
                    Method getValue = ReflectHelper.findMethod("getValue", world.getDifficulty().getClass());
                    return (int) ReflectHelper.invoke(getValue, world.getDifficulty());
                } catch (NMSException | ClassCastException ex) {
                    return world.getDifficulty().ordinal();
                }
            }
        }
    }

    /**
     * Resolves the gamemode int of the player.
     *
     * @param player The player.
     * @return Gamemode int or ADVENTURE in case everything fails.
     */
    private int getGamemode(Player player) {
        switch (player.getGameMode()) {
            case SURVIVAL:
                return 0;
            case CREATIVE:
                return 1;
            case ADVENTURE:
                return 2;
            case SPECTATOR:
                return 3;
            default: {
                try {
                    Method getValue = ReflectHelper.findMethod("getValue", player.getGameMode().getClass());
                    return (int) ReflectHelper.invoke(getValue, player.getGameMode());
                } catch (NMSException | ClassCastException ex) {
                    // Adventure in case getValue fails.
                    // Spigot ordinal is broken in this specific class.
                    return 2;
                }
            }
        }
    }

    /* ----------------- Reflection Point of no Return ----------------- */

    private Class<?> getPacketPlayOutRespawn() throws NMSException {
        return ReflectHelper.getClass("net.minecraft.server." + this.NMS_VERSION + ".PacketPlayOutRespawn");
    }

    private Class<? extends Enum> getEnumDifficulty() throws NMSException {
        return ReflectHelper.getEnum("net.minecraft.server." + this.NMS_VERSION + ".EnumDifficulty");
    }

    private Class<? extends Enum> getEnumGamemode() throws NMSException {
        return ReflectHelper.getEnum("net.minecraft.server." + this.NMS_VERSION + ".EnumGamemode");
    }

    private Class<? extends Enum> getClassWorldType() throws NMSException {
        return ReflectHelper.getEnum("net.minecraft.server." + this.NMS_VERSION + ".WorldType");
    }

    private Class<?> getPacketSuperclass() throws NMSException {
        return ReflectHelper.getClass("net.minecraft.server." + this.NMS_VERSION + ".Packet");
    }

    private Object castAsEnumDifficulty(int diff) throws NMSException {
        Method method = ReflectHelper.findMethod("getById", int.class);
        if (method == null)
            return null;
        return ReflectHelper.invoke(method, null, diff);
    }

    private Object castAsEnumGamemode(int gamemode) throws NMSException {
        Method method = ReflectHelper.findMethod("getById", int.class);
        if (method == null)
            return null;
        return ReflectHelper.invoke(method, null, gamemode);
    }

    private Object castAsWorldType(String worldType) throws NMSException {
        Method method = ReflectHelper.findMethod("getType", String.class);
        if (method == null)
            return null;
        return ReflectHelper.invoke(method, null, worldType);
    }

}