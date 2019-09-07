/*
 * All rights by DomeDD (2019)
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
import org.bukkit.event.player.PlayerChangedWorldEvent;
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
			if(pl.getConfig().getBoolean("Config.Nick on Join")) {
				if(BetterNickAPI.getApi().hasPlayerAutoNick(p)) {
					if(BetterNickAPI.getApi().wasPlayerNicked(p)) {
						Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
							@Override
							public void run() {
								Bukkit.getPluginManager().callEvent(new PlayerCallNickEvent(p, BetterNickAPI.getApi().getLogoutNickName(p)));
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
					if(BetterNickAPI.getApi().wasPlayerNicked(p)) {
						Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
							@Override
							public void run() {
								Bukkit.getPluginManager().callEvent(new PlayerCallNickEvent(p, BetterNickAPI.getApi().getLogoutNickName(p)));
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
			BetterNickAPI.getApi().resetPlayerNickNameOnQuit(p, BetterNickAPI.getApi().hasPlayerKeepNick(p));
		}
	}
	@EventHandler
	public void onPlayerWorldChange(PlayerChangedWorldEvent e) {
		Player p = e.getPlayer();
		if(BetterNickAPI.getApi().isPlayerNicked(p)) {
			String nickname = BetterNickAPI.getApi().getNickName(p);
			String chatprefix = pl.getConfig().getString("Nick Options.Chat Prefix").replace("&", "§");
			String chatsuffix = pl.getConfig().getString("Nick Options.Chat Suffix").replace("&", "§");
			String tablistprefix = pl.getConfig().getString("Nick Options.Tablist Prefix").replace("&", "§");
			String tablistsuffix = pl.getConfig().getString("Nick Options.Tablist Suffix").replace("&", "§");
			
			BetterNickAPI.getApi().setPlayerChatName(p, nickname, chatprefix, chatsuffix);
			if(pl.chat.getName().equals("LuckPerms")) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
					@Override
					public void run() {
						BetterNickAPI.getApi().setPlayerTablistName(p, nickname, tablistprefix, tablistsuffix);
					}
				}, 8);
			} else {
				BetterNickAPI.getApi().setPlayerTablistName(p, nickname, tablistprefix, tablistsuffix);
			}
		}
	}
}
