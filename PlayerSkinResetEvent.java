/*
 * All rights by DomeDD
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free 
 * You are NOT allowed to claim this plugin as your own
 * You are NOT allowed to publish this plugin or your modified version of this plugin
 * 
 */
class PlayerSkinResetEvent extends Event {
	
	/**
   	 * @return player
	 */
	Player getPlayer();
	
	/**
   	 * @return nicked player
	 */
	NickedPlayer getNickedPlayer();
	
	/**
   	 * @return nickname
	 */
	String getNickName();
	
	/**
   	 * @param arg0 message
	 */
	void setSkinResetMessage(String arg0);
}