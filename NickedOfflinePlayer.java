/*
 * All rights by DomeDD
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free 
 * You are NOT allowed to claim this plugin as your own
 * You are NOT allowed to publish this plugin or your modified version of this plugin
 * 
 */
class NickedOfflinePlayer implements OfflinePlayer, Listener {
	
	/**
	 * constructor
	 * @param arg0 offlineplayer
	 */
	NickedOfflinePlayer(OfflinePlayer arg0);
	
	/**
	 * @return boolean true if player exists
	 */
	boolean exists();
	
	NickedOfflinePlayer addToDatabase();
	
	NickedOfflinePlayer removeFromDatabase();
}