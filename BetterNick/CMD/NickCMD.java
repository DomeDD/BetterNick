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

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import BetterNick.Main;
import BetterNick.API.NickAPI;

public class NickCMD implements CommandExecutor {

	private Main pl;
	public NickCMD(Main main) {
		this.pl = main;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdlabel, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(args.length == 0) {
				if(p.hasPermission("BetterNick.RandomNick")) {
					String nameprefix = pl.getConfig().getString("Config.Display Name Prefix").replace("&", "§");
					String nametagprefix = pl.getConfig().getString("Config.Name Tag Prefix").replace("&", "§");
					String tablistprefix = pl.getConfig().getString("Config.Tablist Name Prefix").replace("&", "§");
					NickAPI.setRandomNickName(p, nameprefix, nametagprefix, tablistprefix);
					NickAPI.setRandomSkin(p);
				}
			}
			if(args.length == 1) {
				if(p.hasPermission("BetterNick.Nick")) {
					String nameprefix = pl.getConfig().getString("Config.Display Name Prefix").replace("&", "§");
					String nametagprefix = pl.getConfig().getString("Config.Name Tag Prefix").replace("&", "§");
					String tablistprefix = pl.getConfig().getString("Config.Tablist Name Prefix").replace("&", "§");
					NickAPI.setNickName(p, args[0], nameprefix, nametagprefix, tablistprefix);
					NickAPI.setRandomSkin(p);
				}
			}
		}
		return true;
	}
}
