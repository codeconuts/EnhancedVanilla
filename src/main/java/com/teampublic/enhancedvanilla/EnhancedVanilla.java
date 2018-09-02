package com.teampublic.enhancedvanilla;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class EnhancedVanilla extends JavaPlugin {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("trackcombat")) {
			if (!sender.hasPermission("enhancedvanilla.trackcombat")) {
				sender.sendMessage("You are not permitted to track combats on a player!");
				return true;
			}
			if (args.length == 0) {
				sender.sendMessage("Please specify a player!");
				return true;
			}
			Player player = Bukkit.getPlayer(args[0]);
			if (player == null) {
				sender.sendMessage("Specified player is either invalid or offline!");
				return true;
			}
			new IgnitionCombat(this, player).open();
			sender.sendMessage("Successfully added a tracker to a player!");
			return true;
		}
		return false;
	}
	
}
