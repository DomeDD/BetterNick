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
import de.domedd.betternick.api.betternickapi.BetterNickAPI;

public class NickListCommand implements CommandExecutor {

	private BetterNick pl;
	
	public NickListCommand(BetterNick main) {
		this.pl = main;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdlable, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(args.length == 0) {
				if(p.hasPermission("BetterNick.NickList")) {
					if(BetterNickAPI.getApi().getNickedPlayers().isEmpty()) {
						p.sendMessage(pl.prefix + pl.getConfig().getString("Messages.No Player Nicked Error").replace("&", "§"));
					} else {
						int i = 1;
						p.sendMessage(pl.prefix + pl.getConfig().getString("Messages.Get Nicked Players Header").replace("&", "§"));
						for(Player nps : BetterNickAPI.getApi().getNickedPlayers()) {
							p.sendMessage(pl.prefix + pl.getConfig().getString("Messages.Get Nicked Players").replace("[ID]", i + "").replace("[PLAYER]", BetterNickAPI.getApi().getNickName(nps)).replace("[NAME]", BetterNickAPI.getApi().getRealName(nps)).replace("&", "§"));
							i++;
						}
					}
				} else {
					if(pl.getConfig().getBoolean("Messages.Enabled")) {
						p.sendMessage(pl.prefix + pl.getConfig().getString("Messages.No Permissions").replace("&", "§"));
					}
				}
			}
		} else {
			if(BetterNickAPI.getApi().getNickedPlayers().isEmpty()) {
				Bukkit.getConsoleSender().sendMessage(pl.prefix + pl.getConfig().getString("Messages.No Player Nicked Error").replace("&", "§"));
			} else {
				int i = 1;
				Bukkit.getConsoleSender().sendMessage(pl.prefix + pl.getConfig().getString("Messages.Get Nicked Players Header").replace("&", "§"));
				for(Player nps : BetterNickAPI.getApi().getNickedPlayers()) {
					Bukkit.getConsoleSender().sendMessage(pl.prefix + pl.getConfig().getString("Messages.Get Nicked Players").replace("[ID]", i + "").replace("[PLAYER]", BetterNickAPI.getApi().getNickName(nps)).replace("[NAME]", BetterNickAPI.getApi().getRealName(nps)).replace("&", "§"));
					i++;
				}
			}
		}
		return true;
	}
}
