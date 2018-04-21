/*
 * All rights by DomeDD (2018)
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free and as long as you mention me (DomeDD) 
 * You are NOT allowed to claim this plugin (BetterNick) as your own
 * You are NOT allowed to publish this plugin (BetterNick) or your modified version of this plugin (BetterNick)
 * 
 */
package de.domedd.betternick.addons.joinquitmessage;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.domedd.betternick.BetterNick;
import de.domedd.betternick.api.betternickapi.BetterNickAPI;

public class JoinQuitMessage implements Listener {
	
	private BetterNick pl;
	
	public JoinQuitMessage(BetterNick main) {
		this.pl = main;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		e.setJoinMessage(null);
		Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
			@Override
			public void run() {
				if(BetterNickAPI.getApi().isPlayerNicked(p)) {
					Bukkit.broadcastMessage(pl.getConfig().getString("Addons.Join / Quit Message.Join Message").replace("[NAME]", BetterNickAPI.getApi().getNickName(p)).replace("&", "§"));
				} else {
					Bukkit.broadcastMessage(pl.getConfig().getString("Addons.Join / Quit Message.Join Message").replace("[NAME]", p.getName()).replace("&", "§"));
				}
			}
		}, 5);
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		e.setQuitMessage(null);
		Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
			@Override
			public void run() {
				if(BetterNickAPI.getApi().isPlayerNicked(p)) {
					Bukkit.broadcastMessage(pl.getConfig().getString("Addons.Join / Quit Message.Quit Message").replace("[NAME]", BetterNickAPI.getApi().getNickName(p)).replace("&", "§"));
				} else {
					Bukkit.broadcastMessage(pl.getConfig().getString("Addons.Join / Quit Message.Quit Message").replace("[NAME]", p.getName()).replace("&", "§"));
				}
			}
		}, 3);
	}
}
