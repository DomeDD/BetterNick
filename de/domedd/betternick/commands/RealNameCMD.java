/*
 * All rights by DomeDD
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free 
 * You are NOT allowed to claim this plugin as your own
 * You are NOT allowed to publish this plugin or your modified version of this plugin
 * 
 */
package de.domedd.betternick.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.domedd.betternick.BetterNick;
import de.domedd.betternick.api.nickedplayer.NickedPlayer;

public class RealNameCMD implements CommandExecutor {
	
	private BetterNick pl;
	
	public RealNameCMD(BetterNick main) {
		this.pl = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdlabel, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(args.length == 1) {
				if(p.hasPermission("BetterNick.RealName")) {
					NickedPlayer t = new NickedPlayer(Bukkit.getPlayer(args[0]));
					if(t.isOnline()) {
						p.sendMessage(pl.getConfig().getString("Messages.See Real Name")
								.replace("[PLAYER]", t.getDisplayName())
								.replace("[NAME]", t.getRealName())
								.replace("&", "§"));
					} else {
						p.sendMessage(pl.getConfig().getString("Messages.See Real Name Error").replace("&", "§"));
					}
				} else {
					if(pl.getConfig().getBoolean("Messages.Enabled")) {
						p.sendMessage(pl.getConfig().getString("Messages.No Permissions").replace("&", "§"));
					}
				}
			}
		} else {
			if(args.length == 1) {
				NickedPlayer t = new NickedPlayer(Bukkit.getPlayer(args[0]));
				if(t.isOnline()) {
					Bukkit.getConsoleSender().sendMessage(pl.getConfig().getString("Messages.See Real Name")
							.replace("[PLAYER]", t.getDisplayName()).replace("[NAME]", t.getRealName())
							.replace("&", "§"));
				} else {
					Bukkit.getConsoleSender().sendMessage(pl.getConfig().getString("Messages.See Real Name Error").replace("&", "§"));
				}
			}
		}
		return true;
	}

}
