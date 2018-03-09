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

class PlayerNickEvent extends Event implements Cancellable {

	/**
	 * constructor
	 * @param player The player
	 * @param nickname The nickname
	 *
	 */
	PlayerNickEvent(Player player, String nickname);
	
	/**
	 * @return Player The player
	 *
	 */
	Player getPlayer();
	
	/**
	 * @return String The nickname of the player
	 *
	 */
	String getNickName();
	
	/**
	 * Call this method to send the player a message.
	 *
	 */
	void setNickMessage(String message);
	
	/**
	 * Call this method to send the player an actionbar message.
	 *
	 */
	void setNickActionbarMessage(String message);
	
	/**
	 * Call this method to stop sending the player an actionbar message.
	 *
	 */
	void stopNickActionbarMessage();
}
