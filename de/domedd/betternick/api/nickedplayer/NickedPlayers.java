/*
 * All rights by DomeDD
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free 
 * You are NOT allowed to claim this plugin as your own
 * You are NOT allowed to publish this plugin or your modified version of this plugin
 * 
 */
package de.domedd.betternick.api.nickedplayer;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import de.domedd.betternick.BetterNick;

public class NickedPlayers implements Listener {
	
	private static BetterNick pl;
	
	@SuppressWarnings("static-access")
	public NickedPlayers(BetterNick main) {
		this.pl = main;
	}
	public NickedPlayers() {}
	public List<NickedPlayer> getAll() {
		List<NickedPlayer> list = new ArrayList<NickedPlayer>();
		for(String names : pl.nickedPlayers) {
			list.add(new NickedPlayer(Bukkit.getPlayer(names)));
		}
		return list;
	}
}
