/*
 * All rights by DomeDD
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free 
 * You are NOT allowed to claim this plugin as your own
 * You are NOT allowed to publish this plugin or your modified version of this plugin
 * 
 */
package BetterNick.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import BetterNick.Main;
import BetterNick.API.NickAPI;

public class AutoNick implements Listener {

	private Main pl;
	public AutoNick(Main main) {
		this.pl = main;
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		String nameprefix = pl.getConfig().getString("Config.Display Name Prefix").replace("&", "§");
		String nametagprefix = pl.getConfig().getString("Config.Name Tag Prefix").replace("&", "§");
		String tablistprefix = pl.getConfig().getString("Config.Tablist Name Prefix").replace("&", "§");
		if(p.hasPermission("BetterNick.Nick")) {
			NickAPI.createNickedPlayer(p);
			if(!pl.getConfig().getBoolean("Config.BungeeCord Lobby Mode")) {
				if(NickAPI.autoNick(p)) {
					NickAPI.setRandomNickName(p, nameprefix, nametagprefix, tablistprefix);
					NickAPI.setRandomSkin(p);
				}
			}
		}
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(p.hasPermission("BetterNick.UnNick")) {
			if(NickAPI.isNicked(p)) {
				NickAPI.UnNickOnLeave(p);
			}			
		}
	}
}
