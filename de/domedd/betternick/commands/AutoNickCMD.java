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

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.domedd.betternick.BetterNick;
import de.domedd.betternick.api.nickedplayer.NickedPlayer;

public class AutoNickCMD implements CommandExecutor {

	private BetterNick pl;
	
	public AutoNickCMD(BetterNick main) {
		this.pl = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
		if(sender instanceof Player) {
			NickedPlayer p = new NickedPlayer((Player) sender);
			if(p.hasPermission("BetterNick.JoinItem")) {
				if(p.hasAutoNick()) {
					p.setAutoNick(false);
					p.sendMessage(pl.getConfig().getString("Messages.AutoNick Turned Off").replace("&", "§"));
				} else {
					p.setAutoNick(true);
					p.sendMessage(pl.getConfig().getString("Messages.AutoNick Turned On").replace("&", "§"));
				}
			}
		}
		return true;
	}
}
