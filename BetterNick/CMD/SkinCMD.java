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

import BetterNick.API.NickAPI;

public class SkinCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdlabel, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(args.length == 0) {
				if(p.hasPermission("BetterNick.RandomSkin")) {
					NickAPI.setRandomSkin(p.getUniqueId());
				}
			}
			if(args.length == 1) {
				if(p.hasPermission("BetterNick.Skin")) {
					NickAPI.setSkin(p.getUniqueId(), args[0]);
				}
			}
		}
		return true;
	}

}
