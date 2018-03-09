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

class PlayerUnnickEvent extends Event implements Cancellable {
	
	/**
	 * constructor
	 * @param player The player
	 *
	 */
	PlayerUnnickEvent(Player arg0);
	
	/**
	 * @return Player The player
	 *
	 */
	Player getPlayer();
	
	/**
	 * Call this method to send the player a message.
	 *
	 */
	void setUnNickMessage(String message);
	
	/**
	 * Call this method to stop sending the player an actionbar message.
	 *
	 */
	void stopNickActionbarMessage();
}
