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

public class NickCMD implements CommandExecutor {

	private BetterNick pl;
	
	public NickCMD(BetterNick main) {
		this.pl = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdlabel, String[] args) {
		if(sender instanceof Player) {
			NickedPlayer p = new NickedPlayer((Player) sender);
			if(args.length == 0) {
				if(p.hasPermission("BetterNick.RandomNick")) {
					String nameprefix = pl.getConfig().getString("Config.Display Name Prefix").replace("&", "§");
					String nametagprefix = pl.getConfig().getString("Config.Name Tag Prefix").replace("&", "§");
					String tablistprefix = pl.getConfig().getString("Config.Tablist Name Prefix").replace("&", "§");
					p.setRandomNickName(nameprefix, nametagprefix, tablistprefix);
					p.setRandomSkin();
				}
			}
			if(args.length == 1) {
				if(p.hasPermission("BetterNick.Nick")) {
					String nameprefix = pl.getConfig().getString("Config.Display Name Prefix").replace("&", "§");
					String nametagprefix = pl.getConfig().getString("Config.Name Tag Prefix").replace("&", "§");
					String tablistprefix = pl.getConfig().getString("Config.Tablist Name Prefix").replace("&", "§");
					p.setNickName(args[0], nameprefix, nametagprefix, tablistprefix);
					p.setRandomSkin();
				}
			}
		}
		return true;
	}
}
