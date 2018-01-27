/*
 * All rights by DomeDD
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free 
 * You are NOT allowed to claim this plugin as your own
 * You are NOT allowed to publish this plugin or your modified version of this plugin
 * 
 */
package de.domedd.betternick.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.domedd.betternick.BetterNick;
import de.domedd.betternick.api.nickedplayer.NickedPlayer;

public class AutoNick implements Listener {
	
	private BetterNick pl;
	
	public AutoNick(BetterNick main) {
		this.pl = main;
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		NickedPlayer p = new NickedPlayer(e.getPlayer());
		String nameprefix = pl.getConfig().getString("Config.Display Name Prefix").replace("&", "§");
		String nametagprefix = pl.getConfig().getString("Config.Name Tag Prefix").replace("&", "§");
		String tablistprefix = pl.getConfig().getString("Config.Tablist Name Prefix").replace("&", "§");
		if(p.hasPermission("BetterNick.Nick")) {
			if(!p.exists()) {
				p.create();
			}
			if(!pl.getConfig().getBoolean("Config.Lobby Mode")) {
				if(p.hasAutoNick()) {
					if(p.isNicked()) {
						String nick = p.getNickName();
						Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
							@Override
							public void run() {
								p.setNickName(nick, nameprefix, nametagprefix, tablistprefix);
								p.setRandomSkin();
							}
						}, 2);
					} else {
						Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
							@Override
							public void run() {
								p.setRandomNickName(nameprefix, nametagprefix, tablistprefix);
								p.setRandomSkin();
							}
						}, 2);
					}
				} else {
					if(p.isNicked()) {
						Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
							@Override
							public void run() {
								p.setNickName(p.getNickName(), nameprefix, nametagprefix, tablistprefix);
								p.setRandomSkin();
							}
						}, 2);
					}
				}
			}
		}
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		NickedPlayer p = new NickedPlayer(e.getPlayer());
		if(!pl.getConfig().getBoolean("Config.Keep NickName On Quit")) {
			if(p.hasPermission("BetterNick.UnNick")) {
				if(p.isNicked()) {
					p.unNickOnLeave();
				}			
			}
		} else {
			if(pl.players.containsKey(p)) {
				pl.players.remove(p);
			}
		}
	}
	
}
