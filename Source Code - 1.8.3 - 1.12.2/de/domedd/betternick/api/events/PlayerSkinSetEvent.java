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

public class PlayerSkinSetEvent extends Event implements Cancellable {
	
	public static HandlerList handlers = new HandlerList();
	private Player player;
	private String skin;
	private boolean cancelled;
	
	/**
	 * constructor
	 * @param player The player
	 *
	 */
	public PlayerSkinSetEvent(Player player, String skin) {
		this.player = player;
		this.skin = skin;
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
	 * @return String The players skin
	 *
	 */
	public String getSkin() {
		return skin;
	}
	
	/**
	 * Call this method to send the player a message.
	 *
	 */
	public void setSkinSetMessage(String message) {
		player.sendMessage(message);
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
	public void setCancelled(boolean arg0) {
		cancelled = arg0;
	}
}
