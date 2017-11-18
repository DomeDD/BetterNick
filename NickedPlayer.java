/*
 * All rights by DomeDD
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free 
 * You are NOT allowed to claim this plugin as your own
 * You are NOT allowed to publish this plugin or your modified version of this plugin
 * 
 */
class NickedPlayer implements Player, Listener {
	
	/**
	 * constructor
	 * @param arg0 player
	 */
	NickedPlayer(Player arg0);
	
	/**
	 * @param arg0 nickname
	 * @param arg1 displayname prefix
	 * @param arg2 nametag prefix
	 * @param arg3 playerlist prefix
	 */
	NickedPlayer setNickName(String arg0, String arg1, String arg2, String arg3);
	
	/**
	 * @param arg0 displayname prefix
	 * @param arg1 nametag prefix
	 * @param arg2 playerlist prefix
	 */
	NickedPlayer setRandomNickName(String arg0, String arg1, String arg2);
	
	/**
	 * @param arg0 skin owner
	 */
	NickedPlayer setSkin(String arg0);
	
	NickedPlayer setRandomSkin();
	
	NickedPlayer unNick();
	
	NickedPlayer resetSkin();
	
	NickedPlayer unNickOnLeave();
	
	/**
	 * @param arg0 autonick
	 */
	NickedPlayer setAutoNick(boolean arg0);
	
	/**
	 * @return boolean true if player enabled autonick
	 */
	boolean hasAutoNick();
	
	/**
	 * @return boolean true if player is nicked
	 */
	boolean isNicked();
	
	/**
	 * @return boolean true if nickname is used
	 */
	boolean isNickNameUsed(String arg0);
	
	/**
	 * @param arg0 text
	 */
	NickedPlayer sendActionbar(String arg0);
	
	NickedPlayer endActionbar();
	
	NickedPlayer create();
	
	/**
	 * @return boolean true if player exists
	 */
	boolean exists();
	
	/**
	 * @return NickedPlayer
	 */
	NickedPlayer getNickedPlayer();
	
	/**
	 * @return String nickname of player
	 */
	String getNickName();
	
	/**
	 * @return String real name of player
	 */
	String getRealName();
	
	/**
	 * @return String skin owner name
	 */
	String getSkin();
	
	/**
	 * @return String displayname prefix
	 */
	String getDisplayNamePrefix();
	
	/**
	 * @return String nametag prefix
	 */
	String getNameTagPrefix();
	
	/**
	 * @return String playerlist prefix
	 */
	String getPlayerListPrefix();
}