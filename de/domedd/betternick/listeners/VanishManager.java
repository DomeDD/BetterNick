/*
 * All rights by DomeDD (2017)
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free and as long as you mention me (DomeDD) 
 * You are NOT allowed to claim this plugin (BetterNick) as your own
 * You are NOT allowed to publish this plugin (BetterNick) or your modified version of this plugin (BetterNick)
 * 
 */
package de.domedd.betternick.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.domedd.betternick.BetterNick;
import de.domedd.betternick.api.betternickapi.BetterNickAPI;
import de.myzelyam.api.vanish.PlayerHideEvent;
import de.myzelyam.api.vanish.PlayerShowEvent;

public class VanishManager implements Listener {
	
	private BetterNick pl;
	
	public VanishManager(BetterNick main) {
		this.pl = main;
	}
	@EventHandler
	public void onVanish(PlayerHideEvent e) {
	    Player p = e.getPlayer();
	    if(BetterNickAPI.getApi().isPlayerNicked(p) && pl.getConfig().getBoolean("Config.Nicked Actionbar")) {
	    	BetterNickAPI.getApi().stopPlayerActionbar(p);
		}
	}
	@EventHandler
	public void onUnVanish(PlayerShowEvent e) {
	    Player p = e.getPlayer();
	    if(BetterNickAPI.getApi().isPlayerNicked(p) && pl.getConfig().getBoolean("Config.Nicked Actionbar")) {
	    	BetterNickAPI.getApi().stopPlayerActionbar(p);
	    	BetterNickAPI.getApi().sendPlayerActionbar(p, pl.getConfig().getString("Messages.Nicked Actionbar").replace("[NAME]", BetterNickAPI.getApi().getNickName(p)).replace("&", "§"));
		}
	}
}
