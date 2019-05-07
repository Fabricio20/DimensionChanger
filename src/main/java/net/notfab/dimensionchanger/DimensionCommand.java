package net.notfab.dimensionchanger;

import net.notfab.spigot.simpleconfig.SimpleConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DimensionCommand implements CommandExecutor {

    private final SimpleConfig config;
    private final NMSHandler nmsHandler;
    private final String USAGE;

    public DimensionCommand(SimpleConfig simpleConfig, NMSHandler nmsHandler) {
        this.config = simpleConfig;
        this.nmsHandler = nmsHandler;
        this.USAGE = this.translate("&cUsage: &7/dc <setWorld/setPlayer> [world/player] [dimension]");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission(config.getString("Permission"))) {
            sender.sendMessage(this.translate(config.getString("NoPerm")));
        } else {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (config.contains("Dimensions." + player.getWorld().getName())) {
                        int d = config.getInt("Dimensions." + player.getWorld().getName());
                        if (d == 0) {
                            player.sendMessage(this.translate("&cWorld Dimension: &7Normal"));
                        } else if (d == -1) {
                            player.sendMessage(this.translate("&cWorld Dimension: &7Nether"));
                        } else if (d == 1) {
                            sender.sendMessage(this.translate("&cWorld Dimension: &7The End"));
                        } else {
                            sender.sendMessage(this.translate("&cWorld Dimension: &7Unknown"));
                        }
                        sender.sendMessage(this.USAGE);
                    } else {
                        sender.sendMessage(this.translate("&cWorld Dimension: &7Default"));
                        sender.sendMessage(this.USAGE);
                    }
                } else {
                    sender.sendMessage(this.USAGE);
                }
            } else if (args.length == 1 || args.length == 2) {
                sender.sendMessage(this.USAGE);
            } else {
                if (args[0].equalsIgnoreCase("setWorld")) {
                    World world = Bukkit.getWorld(args[1]);
                    if (world != null) {
                        int dimension;
                        try {
                            dimension = Integer.parseInt(args[2]);
                        } catch (Exception ex) {
                            sender.sendMessage(this.translate("&cDimensionChanger: &7This is not a number!"));
                            return false;
                        }
                        config.set("Dimensions." + world.getName(), dimension);
                        config.save();
                        for (Player player : world.getPlayers()) {
                            this.nmsHandler.setDimension(player, dimension);
                        }
                        sender.sendMessage(this.translate("&cDimensionChanger: &aDimension Changed For World: " + world.getName() + "!"));
                    } else {
                        sender.sendMessage(this.translate("&cDimensionChanger: &7I Cant Find This World!"));
                    }
                } else if (args[0].equalsIgnoreCase("setPlayer")) {
                    Player player = Bukkit.getPlayer(args[1]);
                    if (player != null) {
                        int dimension;
                        try {
                            dimension = Integer.parseInt(args[2]);
                        } catch (Exception ex) {
                            sender.sendMessage(this.translate("&cDimensionChanger: &7This is not a number!"));
                            return false;
                        }
                        if (this.nmsHandler.setDimension(player, dimension)) {
                            sender.sendMessage(this.translate("&cDimensionChanger: &aDimension Changed For Player: " + player.getName() + "!"));
                        } else {
                            sender.sendMessage(this.translate("&cDimensionChanger: Error while changing dimension."));
                        }
                    } else {
                        sender.sendMessage(this.translate("&cDimensionChanger: &7I Cant Find That Player Online!"));
                    }
                } else {
                    sender.sendMessage(this.USAGE);
                }
            }
        }
        return true;
    }

    private String translate(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

}
