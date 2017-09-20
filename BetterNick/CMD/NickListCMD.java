/*
 * All rights by DomeDD
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free 
 * You are NOT allowed to claim this plugin as your own
 * You are NOT allowed to publish this plugin or your modified version of this plugin
 * 
 */
package BetterNick.CMD;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import BetterNick.Main;
import BetterNick.API.NickAPI;

public class NickListCMD implements CommandExecutor {

	private Main pl;
	public NickListCMD(Main main) {
		this.pl = main;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdlabel, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(args.length == 0) {
				if(p.hasPermission("BetterNick.NickList")) {
					if(pl.nickedPlayers.isEmpty()) {
						p.sendMessage(pl.getConfig().getString("Config.Messages.Get Nicked Players Error").replace("&", "§"));
					} else {
						p.sendMessage(pl.getConfig().getString("Config.Messages.Get Nicked Players Header").replace("&", "§"));
						for(int i = 0; i < pl.nickedPlayers.size(); i++) {
							p.sendMessage(pl.getConfig().getString("Config.Messages.Get Nicked Players").replace("[ID]", Integer.valueOf(i + 1).toString()).replace("[PLAYER]", Bukkit.getPlayer(pl.nickedPlayers.get(i)).getDisplayName()).replace("[NAME]", NickAPI.getRealName(Bukkit.getPlayer(pl.nickedPlayers.get(i)))).replace("&", "§"));
						}
					}
				}
			}
		} else {
			if(pl.nickedPlayers.isEmpty()) {
				Bukkit.getConsoleSender().sendMessage(pl.getConfig().getString("Config.Messages.Get Nicked Players Error").replace("&", "§"));
			} else {
				Bukkit.getConsoleSender().sendMessage(pl.getConfig().getString("Config.Messages.Get Nicked Players Header").replace("&", "§"));
				for(int i = 0; i < pl.nickedPlayers.size(); i++) {
					Bukkit.getConsoleSender().sendMessage(pl.getConfig().getString("Config.Messages.Get Nicked Players").replace("[ID]", Integer.valueOf(i + 1).toString()).replace("[PLAYER]", Bukkit.getPlayer(pl.nickedPlayers.get(i)).getDisplayName()).replace("[NAME]", NickAPI.getRealName(Bukkit.getPlayer(pl.nickedPlayers.get(i)))).replace("&", "§"));
				}
			}
		}
		return true;
	}
}
