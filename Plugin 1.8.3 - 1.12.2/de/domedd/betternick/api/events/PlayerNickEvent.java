/*
 * All rights by DomeDD (2018)
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

public class PlayerNickEvent extends Event implements Cancellable {

	public static HandlerList handlers = new HandlerList();
	private Player player;
	private String nick;
	private boolean cancelled;
	
	/**
	 * constructor
	 * @param player The player
	 * @param nickname The nickname
	 *
	 */
	public PlayerNickEvent(Player player, String nickname) {
		this.player = player;
		nick = nickname;
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
	 * @return String The nickname of the player
	 *
	 */
	public String getNickName() {
		return nick;
	}
	
	/**
	 * Call this method to send the player a message.
	 *
	 */
	public void setNickMessage(String message) {
		player.sendMessage(message);
	}
	
	/**
	 * Call this method to send the player an actionbar message.
	 *
	 */
	public void setNickActionbarMessage(String message) {
		BetterNickAPI.getApi().sendPlayerActionbar(player, message);
	}
	
	/**
	 * Call this method to stop sending the player an actionbar message.
	 *
	 */
	public void stopNickActionbarMessage() {
		BetterNickAPI.getApi().stopPlayerActionbar(player);
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
