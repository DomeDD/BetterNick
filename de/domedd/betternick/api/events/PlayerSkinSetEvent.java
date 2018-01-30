/*
 * All rights by DomeDD
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free 
 * You are NOT allowed to claim this plugin as your own
 * You are NOT allowed to publish this plugin or your modified version of this plugin
 * 
 */
package de.domedd.betternick.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.domedd.betternick.api.nickedplayer.NickedPlayer;

public class PlayerSkinSetEvent extends Event implements Cancellable {
	
	public static HandlerList handlers = new HandlerList();
	private NickedPlayer player;
	private String pSkin;
	private boolean cancelled;
	
	public PlayerSkinSetEvent(NickedPlayer arg0, String arg1) {
		player = arg0;
		pSkin = arg1;
		cancelled = false;
	}
	public NickedPlayer getNickedPlayer() {
		return player;
	}
	public Player getPlayer() {
		return player;
	}
	public String getPlayerSkinName() {
		return pSkin;
	}
	public void setSkinSetMessage(String arg0) {
		player.sendMessage(arg0);
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
