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

public class UnNickCMD implements CommandExecutor {

	private Main pl;
	public UnNickCMD(Main main) {
		this.pl = main;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdlabel, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(args.length == 0) {
				if(p.hasPermission("BetterNick.UnNick")) {
					if(NickAPI.isNicked(p.getUniqueId())) {
						NickAPI.UnNick(p.getUniqueId());
						NickAPI.resetSkin(p.getUniqueId());
					} else {
						NickAPI.resetSkin(p.getUniqueId());
					}
				}
			}
		} else {
			if(args.length == 1) {
				Player t = Bukkit.getPlayer(args[0]);
				if(t != null) {
					if(NickAPI.isNicked(t.getUniqueId())) {
						NickAPI.UnNick(t.getUniqueId());
						NickAPI.resetSkin(t.getUniqueId());
					} else {
						NickAPI.resetSkin(t.getUniqueId());
					}
				} else {
					Bukkit.getConsoleSender().sendMessage(pl.getConfig().getString("Config.Messages.See Real Name Error").replace("&", "§"));
				}
			}
		}
		return true;
	}
}
