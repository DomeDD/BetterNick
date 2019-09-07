/*
 * All rights by DomeDD (2019)
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free and as long as you mention me (DomeDD) 
 * You are NOT allowed to claim this plugin (BetterNick) as your own
 * You are NOT allowed to publish this plugin (BetterNick) or your modified version of this plugin (BetterNick)
 * 
 */
package de.domedd.betternick.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.domedd.betternick.api.betternickapi.BetterNickAPI;

public class PlayerCallUnnickEvent extends Event implements Cancellable {

	public static HandlerList handlers = new HandlerList();
	private Player player;
	private boolean cancelled;
	
	/**
	 * constructor
	 * @param player The player
	 *
	 */
	public PlayerCallUnnickEvent(Player player) {
		this.player = player;
		cancelled = false;
	}
	
	/**
	 * @return Player The player
	 *
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Call this method to reset the players nickname, nametag prefix and nametag suffix.
	 *
	 */
	public void resetPlayerNickName() {
		BetterNickAPI.getApi().resetPlayerNickName(player);
	}
	
	/**
	 * Call this method to reset the players displayname, displayname prefix and displayname suffix.
	 *
	 */
	public void resetPlayerDisplayName() {
		BetterNickAPI.getApi().resetPlayerDisplayName(player);
	}
	
	/**
	 * Call this method to reset the players chatname, chatname prefix and chatname suffix.
	 * If Vault is disabled in the config.yml or a Vault compatible chat plugin is NOT successfully
	 * hooked into BetterNick, you can't use this method.
	 *
	 */
	public void resetPlayerChatName() {
		BetterNickAPI.getApi().resetPlayerChatName(player);
	}
	
	/**
	 * Call this method to reset the players tablist, tablist prefix and tablist suffix.
	 *
	 */
	public void resetPlayerTablistName() {
		BetterNickAPI.getApi().resetPlayerTablistName(player);
	}
	
	/**
	 * @return String The real name of the player
	 *
	 */
	public String getRealName() {
		return BetterNickAPI.getApi().getRealName(player);
	}
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	public static HandlerList getHandlerList() {
		return handlers;
	}
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
