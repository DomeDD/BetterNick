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

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import BetterNick.Main;
import BetterNick.API.Events.PlayerNickEvent;
import BetterNick.API.Events.PlayerSkinResetEvent;
import BetterNick.API.Events.PlayerSkinSetEvent;
import BetterNick.API.Events.PlayerUnNickEvent;

public class BetterNickEvents implements Listener {

	private Main pl;
	
	public BetterNickEvents(Main main) {
		this.pl = main;
	}
	@EventHandler
	public void onNick(PlayerNickEvent e) {
		String nick = e.getNickName();
		if(pl.getConfig().getBoolean("Config.Messages.Enabled")) {
			e.setNickMessage(pl.getConfig().getString("Config.Messages.Nick Name Set").replace("[NAME]", nick).replace("&", "§"));
		}
		if(pl.getConfig().getBoolean("Config.Nicked Actionbar")) {
			e.stopNickActionbarMessage();
			e.setNickActionbarMessage(pl.getConfig().getString("Config.Messages.Nicked Actionbar").replace("[NAME]", nick).replace("&", "§"));
		}
	}
	@EventHandler
	public void onUnNick(PlayerUnNickEvent e) {
		if(pl.getConfig().getBoolean("Config.Messages.Enabled")) {
			e.setUnNickMessage(pl.getConfig().getString("Config.Messages.Nick Name Removed").replace("&", "§"));
		}
		if(pl.getConfig().getBoolean("Config.Nicked Actionbar")) {
			e.stopNickActionbarMessage();
		}
	}
	@EventHandler
	public void onSkinSet(PlayerSkinSetEvent e) {
		if(pl.getConfig().getBoolean("Config.Messages.Enabled")) {
			e.setSkinSetMessage(pl.getConfig().getString("Config.Messages.Skin Set").replace("&", "§"));
		}
	}
	@EventHandler
	public void onSkinReset(PlayerSkinResetEvent e) {
		if(pl.getConfig().getBoolean("Config.Messages.Enabled")) {
			e.setSkinResetMessage(pl.getConfig().getString("Config.Messages.Skin Removed").replace("&", "§"));
		}
	}
}
