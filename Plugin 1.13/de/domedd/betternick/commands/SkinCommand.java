/*
 * All rights by DomeDD (2018)
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free and as long as you mention me (DomeDD) 
 * You are NOT allowed to claim this plugin (BetterNick) as your own
 * You are NOT allowed to publish this plugin (BetterNick) or your modified version of this plugin (BetterNick)
 * 
 */
package de.domedd.betternick.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.domedd.betternick.BetterNick;
import de.domedd.betternick.api.events.PlayerCallRandomSkinEvent;
import de.domedd.betternick.api.events.PlayerCallSkinEvent;

public class SkinCommand implements CommandExecutor {

	private BetterNick pl;
	
	public SkinCommand(BetterNick main) {
		this.pl = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdlable, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(args.length == 0) {
				if(p.hasPermission("BetterNick.RandomSkin")) {
					Bukkit.getPluginManager().callEvent(new PlayerCallRandomSkinEvent(p));
				} else {
					if(pl.getConfig().getBoolean("Messages.Enabled")) {
						p.sendMessage(pl.prefix + pl.getConfig().getString("Messages.No Permissions").replace("&", "§"));
					}
				}
			} else if(args.length == 1) {
				Player t = Bukkit.getPlayer(args[0]);
				if(t != null && t.isOnline()) {
					if(p.hasPermission("BetterNick.RandomSkin")) {
						Bukkit.getPluginManager().callEvent(new PlayerCallRandomSkinEvent(t));
					} else {
						if(pl.getConfig().getBoolean("Messages.Enabled")) {
							p.sendMessage(pl.prefix + pl.getConfig().getString("Messages.No Permissions").replace("&", "§"));
						}
					}
				} else {
					if(p.hasPermission("BetterNick.Skin")) {
						Bukkit.getPluginManager().callEvent(new PlayerCallSkinEvent(p, args[0]));
					} else {
						if(pl.getConfig().getBoolean("Messages.Enabled")) {
							p.sendMessage(pl.prefix + pl.getConfig().getString("Messages.No Permissions").replace("&", "§"));
						}
					}
				}
			} else if(args.length == 2) {
				if(p.hasPermission("BetterNick.Skin")) {
					Player t = Bukkit.getPlayer(args[0]);
					if(t != null && t.isOnline()) {
						Bukkit.getPluginManager().callEvent(new PlayerCallSkinEvent(t, args[1]));
					} else {
						p.sendMessage(pl.prefix + pl.getConfig().getString("Config.Messages.See Real Name Error").replace("&", "§"));
					}
				} else {
					if(pl.getConfig().getBoolean("Messages.Enabled")) {
						p.sendMessage(pl.prefix + pl.getConfig().getString("Messages.No Permissions").replace("&", "§"));
					}
				}
			}
		}
		return true;
	}
}
