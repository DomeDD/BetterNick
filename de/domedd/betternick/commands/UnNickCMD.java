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

public class UnNickCMD implements CommandExecutor {

	private BetterNick pl;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdlabel, String[] args) {
		if(sender instanceof Player) {
			NickedPlayer p = new NickedPlayer((Player) sender);
			if(args.length == 0) {
				if(p.hasPermission("BetterNick.UnNick")) {
					if(p.isNicked()) {
						p.unNick();
						p.resetSkin();
					} else {
						p.resetSkin();
					}
				}
			}
		} else {
			if(args.length == 1) {
				NickedPlayer t = new NickedPlayer(Bukkit.getPlayer(args[0]));
				if(t.isOnline()) {
					if(t.isNicked()) {
						t.unNick();
						t.resetSkin();
					} else {
						t.resetSkin();
					}
				} else {
					Bukkit.getConsoleSender().sendMessage(pl.getConfig().getString("Config.Messages.See Real Name Error").replace("&", "§"));
				}
			}
		}
		return true;
	}
}
