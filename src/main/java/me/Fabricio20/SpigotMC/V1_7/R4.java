package me.Fabricio20.SpigotMC.V1_7;

import net.minecraft.server.v1_7_R4.EnumDifficulty;
import net.minecraft.server.v1_7_R4.EnumGamemode;
import net.minecraft.server.v1_7_R4.PacketPlayOutRespawn;
import net.minecraft.server.v1_7_R4.WorldType;

import org.bukkit.Chunk;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class R4 {
	
	@SuppressWarnings("deprecation")
	public static void setDimension(Player player, Integer dimension) {
		CraftPlayer cp = (CraftPlayer) player;
		PacketPlayOutRespawn packet = new PacketPlayOutRespawn(dimension, EnumDifficulty.getById(player.getWorld().getDifficulty().getValue()), WorldType.NORMAL, EnumGamemode.getById(player.getGameMode().getValue()));
	    cp.getHandle().playerConnection.sendPacket(packet);
		Chunk chunk = player.getWorld().getChunkAt(player.getLocation());
	    for (int x = -10; x < 10; x++) {
	      for (int z = -10; z < 10; z++) {
	        player.getWorld().refreshChunk(chunk.getX() + x, chunk.getZ() + z);
	      }
	    }
	}
	
}
