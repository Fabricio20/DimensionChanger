package me.Fabricio20.SpigotMC;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class DimensionUtils {
	
	String version = "v1_7_R4";
	
	@SuppressWarnings("deprecation")
	public static void setDimension(World world, Integer dimension) {
		try {
			Class.forName("net.minecraft.server.v1_7_R4.PacketPlayOutRespawn");
			for(Player player: Bukkit.getOnlinePlayers()) {
				if(player.getWorld().getName().equals(world.getName())) {
					me.Fabricio20.SpigotMC.V1_7.R4.setDimension(player, dimension);
				}
			}
		} catch(ClassNotFoundException ex) {
			try {
				Class.forName("net.minecraft.server.v1_8_R1.PacketPlayOutRespawn");
				for(Player player: Bukkit.getOnlinePlayers()) {
					if(player.getWorld().getName().equals(world.getName())) {
						me.Fabricio20.SpigotMC.V1_8.R1.setDimension(player, dimension);
					}
				}
			} catch (ClassNotFoundException exx) {
				try {
					Class.forName("net.minecraft.server.v1_8_R2.PacketPlayOutRespawn");
					for(Player player: Bukkit.getOnlinePlayers()) {
						if(player.getWorld().getName().equals(world.getName())) {
							me.Fabricio20.SpigotMC.V1_8.R2.setDimension(player, dimension);
						}
					}
				} catch (ClassNotFoundException exxx) {
					try {
						Class.forName("net.minecraft.server.v1_8_R3.PacketPlayOutRespawn");
						for(Player player: Bukkit.getOnlinePlayers()) {
							if(player.getWorld().getName().equals(world.getName())) {
								me.Fabricio20.SpigotMC.V1_8.R3.setDimension(player, dimension);
							}
						}
					} catch (ClassNotFoundException exxxx) {
						try {
							Class.forName("net.minecraft.server.v1_9_R1.PacketPlayOutRespawn");
							for(Player player: Bukkit.getOnlinePlayers()) {
								if(player.getWorld().getName().equals(world.getName())) {
									me.Fabricio20.SpigotMC.V1_9.R1.setDimension(player, dimension);
								}
							}
						} catch (ClassNotFoundException exxxxx) {
							System.out.println("[DimensionChanger] Unsupported Server Version Detected!");
						}
					}
				}
			}
		}
	}
	
	public static void setDimension(Player player, Integer dimension) {
		try {
			Class.forName("net.minecraft.server.v1_7_R4.PacketPlayOutRespawn");
			me.Fabricio20.SpigotMC.V1_7.R4.setDimension(player, dimension);
		} catch(ClassNotFoundException ex) {
			try {
				Class.forName("net.minecraft.server.v1_8_R1.PacketPlayOutRespawn");
				me.Fabricio20.SpigotMC.V1_8.R1.setDimension(player, dimension);
			} catch (ClassNotFoundException exx) {
				try {
					Class.forName("net.minecraft.server.v1_8_R2.PacketPlayOutRespawn");
					me.Fabricio20.SpigotMC.V1_8.R2.setDimension(player, dimension);
				} catch (ClassNotFoundException exxx) {
					try {
						Class.forName("net.minecraft.server.v1_8_R3.PacketPlayOutRespawn");
						me.Fabricio20.SpigotMC.V1_8.R3.setDimension(player, dimension);
					} catch (ClassNotFoundException exxxx) {
						try {
							Class.forName("net.minecraft.server.v1_9_R1.PacketPlayOutRespawn");
							me.Fabricio20.SpigotMC.V1_9.R1.setDimension(player, dimension);
						} catch (ClassNotFoundException exxxxx) {
							System.out.println("[DimensionChanger] Unsupported Server Version Detected!");
						}
					}
				}
			}
		}
	}
	
}
