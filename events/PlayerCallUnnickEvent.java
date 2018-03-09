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

class PlayerCallUnnickEvent extends Event implements Cancellable {
	
	/**
	 * constructor
	 * @param player The player
	 *
	 */
	PlayerCallUnnickEvent(Player player);
	
	/**
	 * @return Player The player
	 *
	 */
	Player getPlayer();
	
	/**
	 * Call this method to reset the players nickname, nametag prefix and nametag suffix.
	 *
	 */
	void resetPlayerNickName();
	
	/**
	 * Call this method to reset the players displayname, displayname prefix and displayname suffix.
	 *
	 */
	void resetPlayerDisplayName();
	
	/**
	 * Call this method to reset the players chatname, chatname prefix and chatname suffix.
	 * If Vault is disabled in the config.yml or a Vault compatible chat plugin is NOT successfully
	 * hooked into BetterNick, you can't use this method.
	 *
	 */
	void resetPlayerChatName();
	
	/**
	 * Call this method to reset the players tablist, tablist prefix and tablist suffix.
	 *
	 */
	void resetPlayerTablistName();
	
	/**
	 * @return String The real name of the player
	 *
	 */
	String getRealName();
}
