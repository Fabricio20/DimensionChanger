package me.Fabricio20.SpigotMC.V1_8;

import net.minecraft.server.v1_8_R3.EnumDifficulty;
import net.minecraft.server.v1_8_R3.PacketPlayOutRespawn;
import net.minecraft.server.v1_8_R3.WorldType;
import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;

import org.bukkit.Chunk;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class R3 {
	
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
