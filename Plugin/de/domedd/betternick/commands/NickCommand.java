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
import de.domedd.betternick.addons.randomnickgui.RandomNickGui;
import de.domedd.betternick.api.events.PlayerCallNickEvent;
import de.domedd.betternick.api.events.PlayerCallRandomNickEvent;
import de.domedd.betternick.api.events.PlayerCallRandomSkinEvent;
import de.domedd.betternick.files.NickedPlayersFile;

public class NickCommand implements CommandExecutor {

	private BetterNick pl;
	
	public NickCommand(BetterNick main) {
		this.pl = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdlable, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(args.length == 0) {
				if(p.hasPermission("BetterNick.RandomNick")) {
					if(!pl.getConfig().getBoolean("Addons.Random Nick Gui.Enabled")) {
						Bukkit.getPluginManager().callEvent(new PlayerCallRandomNickEvent(p));
						if(pl.getConfig().getBoolean("Config.Nick And Skin Combination")) {
							Bukkit.getPluginManager().callEvent(new PlayerCallRandomSkinEvent(p));
						}
					} else {
						p.openInventory(RandomNickGui.randomNicksInventory(p));
					}
				} else {
					if(pl.getConfig().getBoolean("Messages.Enabled")) {
						p.sendMessage(pl.prefix + pl.getConfig().getString("Messages.No Permissions").replace("&", "§"));
					}
				}
			} else if(args.length == 1) {
				if(args[0].equalsIgnoreCase("reload")) {
					if(p.hasPermission("BetterNick.Reload")) {
						pl.reloadConfig();
						NickedPlayersFile.reload();
						if(pl.getConfig().getBoolean("Messages.Enabled")) {
							p.sendMessage(pl.prefix + pl.getConfig().getString("Messages.Files Reloaded").replace("&", "§"));
						}
					} else {
						if(pl.getConfig().getBoolean("Messages.Enabled")) {
							p.sendMessage(pl.prefix + pl.getConfig().getString("Messages.No Permissions").replace("&", "§"));
						}
					}
				} else {
					if(p.hasPermission("BetterNick.Nick")) {
						Bukkit.getPluginManager().callEvent(new PlayerCallNickEvent(p, args[0]));
					} else {
						if(pl.getConfig().getBoolean("Messages.Enabled")) {
							p.sendMessage(pl.prefix + pl.getConfig().getString("Messages.No Permissions").replace("&", "§"));
						}
					}
				}
			}
		}else {
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("reload")) {
					pl.reloadConfig();
					NickedPlayersFile.reload();
					Bukkit.getConsoleSender().sendMessage(pl.prefix + pl.getConfig().getString("Messages.Reloaded").replace("&", "§"));
				}
			}
		}
		return true;
	}
}
