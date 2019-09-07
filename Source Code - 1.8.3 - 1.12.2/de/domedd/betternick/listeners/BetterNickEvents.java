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

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.domedd.betternick.BetterNick;
import de.domedd.betternick.api.events.PlayerCallNickEvent;
import de.domedd.betternick.api.events.PlayerCallRandomNickEvent;
import de.domedd.betternick.api.events.PlayerCallRandomSkinEvent;
import de.domedd.betternick.api.events.PlayerCallSkinEvent;
import de.domedd.betternick.api.events.PlayerCallSkinResetEvent;
import de.domedd.betternick.api.events.PlayerCallUnnickEvent;
import de.domedd.betternick.api.events.PlayerNickEvent;
import de.domedd.betternick.api.events.PlayerSkinResetEvent;
import de.domedd.betternick.api.events.PlayerSkinSetEvent;
import de.domedd.betternick.api.events.PlayerUnnickEvent;

public class BetterNickEvents implements Listener {
	
	private BetterNick pl;
	
	public BetterNickEvents(BetterNick main) {
		this.pl = main;
	}
	
	@EventHandler
	public void onPlayerCallNick(PlayerCallNickEvent e) {
		String nametagprefix = pl.getConfig().getString("Nick Options.Nametag Prefix").replace("&", "§");
		String nametagsuffix = pl.getConfig().getString("Nick Options.Nametag Suffix").replace("&", "§");
		String chatprefix = pl.getConfig().getString("Nick Options.Chat Prefix").replace("&", "§");
		String chatsuffix = pl.getConfig().getString("Nick Options.Chat Suffix").replace("&", "§");
		String displaynameprefix = pl.getConfig().getString("Nick Options.Displayname Prefix").replace("&", "§");
		String displaynamesuffix = pl.getConfig().getString("Nick Options.Displayname Suffix").replace("&", "§");
		String tablistprefix = pl.getConfig().getString("Nick Options.Tablist Prefix").replace("&", "§");
		String tablistsuffix = pl.getConfig().getString("Nick Options.Tablist Suffix").replace("&", "§");
		
		e.setNickName(e.getNickName(), nametagprefix, nametagsuffix);
		e.setPlayerChatName(e.getNickName(), chatprefix, chatsuffix);
		e.setPlayerDisplayName(e.getNickName(), displaynameprefix, displaynamesuffix);
		e.setPlayerTablistName(e.getNickName(), tablistprefix, tablistsuffix);
	}
	@EventHandler
	public void onPlayerCallRandomNick(PlayerCallRandomNickEvent e) {
		String nametagprefix = pl.getConfig().getString("Nick Options.Nametag Prefix").replace("&", "§");
		String nametagsuffix = pl.getConfig().getString("Nick Options.Nametag Suffix").replace("&", "§");
		String chatprefix = pl.getConfig().getString("Nick Options.Chat Prefix").replace("&", "§");
		String chatsuffix = pl.getConfig().getString("Nick Options.Chat Suffix").replace("&", "§");
		String displaynameprefix = pl.getConfig().getString("Nick Options.Displayname Prefix").replace("&", "§");
		String displaynamesuffix = pl.getConfig().getString("Nick Options.Displayname Suffix").replace("&", "§");
		String tablistprefix = pl.getConfig().getString("Nick Options.Tablist Prefix").replace("&", "§");
		String tablistsuffix = pl.getConfig().getString("Nick Options.Tablist Suffix").replace("&", "§");
		
		e.setRandomNickName(nametagprefix, nametagsuffix);
		e.setPlayerChatName(e.getNickName(), chatprefix, chatsuffix);
		e.setPlayerDisplayName(e.getNickName(), displaynameprefix, displaynamesuffix);
		e.setPlayerTablistName(e.getNickName(), tablistprefix, tablistsuffix);
	}
	@EventHandler
	public void onPlayerNick(PlayerNickEvent e) {
		if(pl.getConfig().getBoolean("Messages.Enabled")) {
			e.setNickMessage(pl.prefix + pl.getConfig().getString("Messages.Nick Name Set").replace("[NAME]", e.getNickName()).replace("&", "§"));
		}
		if(pl.getConfig().getBoolean("Config.Nicked Actionbar")) {
			e.stopNickActionbarMessage();
			e.setNickActionbarMessage(pl.getConfig().getString("Messages.Nicked Actionbar").replace("[NAME]", e.getNickName()).replace("&", "§"));
		}
	}
	
	@EventHandler
	public void onPlayerCallSkin(PlayerCallSkinEvent e) {
		e.setSkin(e.getSkin());
	}
	@EventHandler
	public void onPlayerCallRandomSkin(PlayerCallRandomSkinEvent e) {
		e.setRandomSkin();
	}
	@EventHandler
	public void onPlayerSkinSetEvent(PlayerSkinSetEvent e) {
		if(pl.getConfig().getBoolean("Messages.Enabled")) {
			e.setSkinSetMessage(pl.prefix + pl.getConfig().getString("Messages.Skin Set").replace("&", "§"));
		}
	}
	
	@EventHandler
	public void onPlayerCallUnnick(PlayerCallUnnickEvent e) {
		e.resetPlayerNickName();
		e.resetPlayerDisplayName();
		e.resetPlayerTablistName();
		e.resetPlayerChatName();
	}
	@EventHandler
	public void onPlayerUnnick(PlayerUnnickEvent e) {
		if(pl.getConfig().getBoolean("Messages.Enabled")) {
			e.setUnNickMessage(pl.prefix + pl.getConfig().getString("Messages.Nick Name Removed").replace("&", "§"));
		}
		if(pl.getConfig().getBoolean("Config.Nicked Actionbar")) {
			e.stopNickActionbarMessage();
		}
	}
	
	@EventHandler
	public void onPlayerCallSkinReset(PlayerCallSkinResetEvent e) {
		e.resetSkin();
	}
	@EventHandler
	public void onPlayerResetSkin(PlayerSkinResetEvent e) {
		if(pl.getConfig().getBoolean("Messages.Enabled")) {
			e.setSkinResetMessage(pl.prefix + pl.getConfig().getString("Messages.Skin Removed").replace("&", "§"));
		}
	}
}
