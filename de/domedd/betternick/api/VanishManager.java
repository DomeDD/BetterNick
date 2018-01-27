/*
 * All rights by DomeDD
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free 
 * You are NOT allowed to claim this plugin as your own
 * You are NOT allowed to publish this plugin or your modified version of this plugin
 * 
 */
package de.domedd.betternick.api;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.domedd.betternick.BetterNick;
import de.domedd.betternick.api.nickedplayer.NickedPlayer;
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
	    NickedPlayer np = new NickedPlayer(p);
	    if(np.isNicked() && pl.getConfig().getBoolean("Config.Nicked Actionbar")) {
			np.endActionbar();
		}
	}
	@EventHandler
	public void onUnVanish(PlayerShowEvent e) {
	    Player p = e.getPlayer();
	    NickedPlayer np = new NickedPlayer(p);
	    if(np.isNicked() && pl.getConfig().getBoolean("Config.Nicked Actionbar")) {
	    	np.endActionbar();
			np.sendActionbar(pl.getConfig().getString("Messages.Nicked Actionbar").replace("[NAME]", np.getNickName()).replace("&", "§"));
		}
	}

}
