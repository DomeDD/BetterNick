/*
 * All rights by DomeDD (2018)
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free and as long as you mention me (DomeDD) 
 * You are NOT allowed to claim this plugin (BetterNick) as your own
 * You are NOT allowed to publish this plugin (BetterNick) or your modified version of this plugin (BetterNick)
 * 
 */
package de.domedd.betternick.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.domedd.betternick.BetterNick;
import de.domedd.betternick.api.betternickapi.BetterNickAPI;
import de.domedd.betternick.api.events.PlayerCallNickEvent;
import de.domedd.betternick.api.events.PlayerCallRandomNickEvent;
import de.domedd.betternick.api.events.PlayerCallRandomSkinEvent;

public class AutoNick implements Listener {
	
	private BetterNick pl;
	
	public AutoNick(BetterNick main) {
		this.pl = main;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if(p.hasPermission("BetterNick.Nick")) {
			if(!BetterNickAPI.getApi().playerExists(p)) {
				BetterNickAPI.getApi().createPlayer(p);
			}
			if(!pl.getConfig().getBoolean("Config.Nick on Join")) {
				if(BetterNickAPI.getApi().hasPlayerAutoNick(p)) {
					if(BetterNickAPI.getApi().isPlayerNicked(p)) {
						Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
							@Override
							public void run() {
								Bukkit.getPluginManager().callEvent(new PlayerCallNickEvent(p, BetterNickAPI.getApi().getNickName(p)));
								Bukkit.getPluginManager().callEvent(new PlayerCallRandomSkinEvent(p));
							}
						}, 2);
					} else {
						Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
							@Override
							public void run() {
								Bukkit.getPluginManager().callEvent(new PlayerCallRandomNickEvent(p));
								Bukkit.getPluginManager().callEvent(new PlayerCallRandomSkinEvent(p));
							}
						}, 2);
					}
				} else {
					if(BetterNickAPI.getApi().isPlayerNicked(p)) {
						Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
							@Override
							public void run() {
								Bukkit.getPluginManager().callEvent(new PlayerCallNickEvent(p, BetterNickAPI.getApi().getNickName(p)));
								Bukkit.getPluginManager().callEvent(new PlayerCallRandomSkinEvent(p));
							}
						}, 2);
					}
				}
			}
		}
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(BetterNickAPI.getApi().isPlayerNicked(p)) {
			if(!pl.getConfig().getBoolean("Config.Keep NickName On Quit")) {
				BetterNickAPI.getApi().resetPlayerChatName(p);
				BetterNickAPI.getApi().resetPlayerDisplayName(p);
				BetterNickAPI.getApi().resetPlayerTablistName(p);
				BetterNickAPI.getApi().resetPlayerNickNameOnQuit(p, false);
				BetterNickAPI.getApi().removeNickedPlayer(p);
			} else {
				BetterNickAPI.getApi().resetPlayerChatName(p);
				BetterNickAPI.getApi().resetPlayerDisplayName(p);
				BetterNickAPI.getApi().resetPlayerTablistName(p);
				BetterNickAPI.getApi().resetPlayerNickNameOnQuit(p, true);
				BetterNickAPI.getApi().removeNickedPlayer(p);
			}
		}
	}
}
