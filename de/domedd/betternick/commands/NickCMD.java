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
import de.domedd.betternick.addons.randomnickgui.RandomNickGui;
import de.domedd.betternick.api.nickedplayer.NickedPlayer;
import de.domedd.betternick.files.NickedPlayersFile;

public class NickCMD implements CommandExecutor {

	private BetterNick pl;
	
	public NickCMD(BetterNick main) {
		this.pl = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdlabel, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			NickedPlayer np = new NickedPlayer((Player) sender);
			if(args.length == 0) {
				if(p.hasPermission("BetterNick.RandomNick")) {
					if(!pl.getConfig().getBoolean("Addons.Random Nick Gui.Enabled")) {
						String nameprefix = pl.getConfig().getString("Config.Display Name Prefix").replace("&", "§");
						String nametagprefix = pl.getConfig().getString("Config.Name Tag Prefix").replace("&", "§");
						String tablistprefix = pl.getConfig().getString("Config.Tablist Name Prefix").replace("&", "§");
						np.setRandomNickName(nameprefix, nametagprefix, tablistprefix);
						np.setRandomSkin();
					} else {
						p.openInventory(RandomNickGui.randomNicksInventory(p));
					}
				}
			}
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("reload")) {
					pl.reloadConfig();
					NickedPlayersFile.reload();
					p.sendMessage(pl.getConfig().getString("Messages.Reloaded").replace("&", "§"));
				} else {
					String nameprefix = pl.getConfig().getString("Config.Display Name Prefix").replace("&", "§");
					String nametagprefix = pl.getConfig().getString("Config.Name Tag Prefix").replace("&", "§");
					String tablistprefix = pl.getConfig().getString("Config.Tablist Name Prefix").replace("&", "§");
					np.setNickName(args[0], nameprefix, nametagprefix, tablistprefix);
					np.setRandomSkin();
				}
			}
		} else {
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("reload")) {
					pl.reloadConfig();
					NickedPlayersFile.reload();
					Bukkit.getConsoleSender().sendMessage(pl.getConfig().getString("Messages.Reloaded").replace("&", "§"));
				}
			}
		}
		return true;
	}
}
