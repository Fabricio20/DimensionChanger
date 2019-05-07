package me.Fabricio20.SpigotMC;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DimensionCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(sender.hasPermission(Main.theClass.config.getString("Permission"))) {
			if(args.length == 0) {
				if(sender instanceof Player) {
					Player player = (Player) sender;
					if(Main.theClass.config.contains("Dimensions")) {
						if(Main.theClass.config.contains("Dimensions." + player.getWorld().getName())) {
							Integer d = Main.theClass.config.getInt("Dimensions." + player.getWorld().getName());
							if(d == 0) {
								player.sendMessage("§cWorld Dimension: §7Normal");
								sender.sendMessage("§cUsage: §7/dc <setworld/setplayer> [world/player] [dimension]");
							} else if(d == -1) {
								player.sendMessage("§cWorld Dimension: §7Nether");
								sender.sendMessage("§cUsage: §7/dc <setworld/setplayer> [world/player] [dimension]");
							} else {
								player.sendMessage("§cWorld Dimension: §7The End");
								sender.sendMessage("§cUsage: §7/dc <setworld/setplayer> [world/player] [dimension]");
							}
						} else {
							sender.sendMessage("§cUsage: §7/dc <setworld/setplayer> [world/player] [dimension]");
						}
					} else {
						sender.sendMessage("§cUsage: §7/dc <setworld/setplayer> [world/player] [dimension]");
					}
				} else {
					sender.sendMessage("§cUsage: §7/dc <setworld/setplayer> [world/player] [dimension]");
				}
			}
			if(args.length == 1) {
				sender.sendMessage("§cUsage: §7/dc <setworld/setplayer> [world/player] [dimension]");
			}
			if(args.length == 2) {
				sender.sendMessage("§cUsage: §7/dc <setworld/setplayer> [world/player] [dimension]");
			}
			if(args.length >= 3) {
				if(args[0].equalsIgnoreCase("setworld")) {
					World world = Bukkit.getWorld(args[1]);
					if(world != null) {
						Integer dimension = 0;
						try {
							dimension = Integer.parseInt(args[2]);
						} catch (Exception ex) {
							sender.sendMessage("§cDimensionChanger: §7This is not a number!");
							return false;
						}
						Main.theClass.config.set("Dimensions." + world.getName(), dimension);
						Main.theClass.config.saveConfig();
						DimensionUtils.setDimension(world, dimension);
						sender.sendMessage("§cDimensionChanger: §aDimension Changed For World: " + world.getName() + "!");
					} else {
						sender.sendMessage("§cDimensionChanger: §7I Cant Find This World!");
					}
				} else if(args[0].equalsIgnoreCase("setplayer")) {
					Player player = Bukkit.getPlayer(args[1]);
					if(player != null) {
						Integer dimension = 0;
						try {
							dimension = Integer.parseInt(args[2]);
						} catch (Exception ex) {
							sender.sendMessage("§cDimensionChanger: §7This is not a number!");
							return false;
						}
						DimensionUtils.setDimension(player, dimension);
						sender.sendMessage("§cDimensionChanger: §aDimension Changed For Player: " + player.getName() + "!");
					} else {
						sender.sendMessage("§cDimensionChanger: §7I Cant Find That Player Online!");
					}
				} else {
					sender.sendMessage("§cUsage: §7/dc <setworld/setplayer> [world/player] [dimension]");
				}
			}
		} else {
			sender.sendMessage(Main.theClass.config.getString("NoPerm").replace("&", "§"));
		}
		return false;
	}
	
}
