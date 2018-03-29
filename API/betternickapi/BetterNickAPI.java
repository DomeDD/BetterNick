/*
 * All rights by DomeDD (2018)
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free and as long as you mention me (DomeDD) 
 * You are NOT allowed to claim this plugin (BetterNick) as your own
 * You are NOT allowed to publish this plugin (BetterNick) or your modified version of this plugin (BetterNick)
 * 
 */
package de.domedd.betternick.api;

class BetterNickAPI implements Listener {

	/**
	 * @return BetterNickAPI
	 *
	 */
	BetterNickAPI getApi();
	
	/**
	 * Call this method to set a player a nickname, a nametag prefix and a nametag suffix.
	 * If NametagEdit or ColoredTags is NOT installed on your server,
	 * you can't set a nametag suffix and only a nametag prefix with the length of 2 chars.
	 *
	 * @param player The player
	 * @param nickname The nickname
	 * @param nametagprefix The nametag prefix
	 * @param nametagsuffix The nametag suffix
	 *
	 */
	void setPlayerNickName(Player player, String nickname, String nametagprefix, String nametagsuffix);
	
	/**
	 * Call this method to set a player a random nickname, a nametag prefix and a nametag suffix.
	 * The nick name will be chosen from a list in the config.yml
	 * If NametagEdit or ColoredTags is NOT installed on your server,
	 * you can't set a nametag suffix and only a nametag prefix with the length of 2 chars.
	 *
	 * @param player The player
	 * @param nametagprefix The nametag prefix
	 * @param nametagsuffix The nametag suffix
	 *
	 */
	void setRandomPlayerNickName(Player player, String nametagprefix, String nametagsuffix);
	
	/**
	 * Call this method to set a player a chat nickname, a chatname prefix and a chatname suffix.
	 * If Vault is disabled in the config.yml or a Vault compatible chat plugin is NOT successfully
	 * hooked into BetterNick, you can't use this method.
	 *
	 * @param player The player
	 * @param nickname The chat nickname
	 * @param chatprefix The chatname prefix
	 * @param chatsuffix The chatname suffix
	 *
	 */
	void setPlayerChatName(Player player, String nickname, String chatprefix, String chatsuffix);
	
	/**
	 * Call this method to set a player a display nickname, a displayname prefix and a displayname suffix.
	 *
	 * @param player The player
	 * @param nickname The display nickname
	 * @param displaynameprefix The displayname prefix
	 * @param displaynamesuffix The displayname suffix
	 *
	 */
	void setPlayerDisplayName(Player player, String nickname, String displaynameprefix, String displaynamesuffix);
	
	
	/**
	 * Call this method to set a player a tablist nickname, a tablist prefix and a tablist suffix.
	 *
	 * @param player The player
	 * @param nickname The tablist nickname
	 * @param tablistprefix The tablist name prefix
	 * @param tablistsuffix The tablist name suffix
	 *
	 */
	void setPlayerTablistName(Player player, String nickname, String tablistprefix, String tablistsuffix);
	
	/**
	 * Call this method to reset the players nickname, nametag prefix and nametag suffix.
	 *
	 * @param player The player
	 *
	 */
	void resetPlayerNickName(Player player);
	
	/**
	 * Call this method to reset the players nickname, nametag prefix and nametag suffix on quit.
	 *
	 * @param player The player
	 * @param keepNick If the player keeps his/her nick for next join or not
	 *
	 */
	void resetPlayerNickNameOnQuit(Player player, boolean keepNick);
	
	/**
	 * Call this method to reset the players chatname, chatname prefix and chatname suffix.
	 * If Vault is disabled in the config.yml or a Vault compatible chat plugin is NOT successfully
	 * hooked into BetterNick, you can't use this method.
	 *
	 * @param player The player
	 *
	 */
	void resetPlayerChatName(Player player);
	
	/**
	 * Call this method to reset the players displayname, displayname prefix and displayname suffix.
	 *
	 * @param player The player
	 *
	 */
	void resetPlayerDisplayName(Player player);
	
	/**
	 * Call this method to reset the players tablist, tablist prefix and tablist suffix.
	 *
	 * @param player The player
	 *
	 */
	void resetPlayerTablistName(Player player);
	
	/**
	 * Call this method to set a player a new skin.
	 *
	 * @param player The player
	 * @param skin The new skin
	 *
	 */
	void setPlayerSkin(Player player, String skin);
	
	/**
	 * Call this method to set a player a new random skin.
	 *
	 * @param player The player
	 *
	 */
	void setRandomPlayerSkin(Player player);
	
	/**
	 * Call this method to reset the players skin.
	 *
	 * @param player The player
	 *
	 */
	void resetPlayerSkin(Player player);
	
	/**
	 * Call this method to get the nickname of a player.
	 *
	 * @param player The player
	 * @return String nickname of the player
	 *
	 */
	String getNickName(Player player);
	
	/**
	 * Call this method to get the real name of a player.
	 *
	 * @param player The player
	 * @return String real name of the player
	 *
	 */
	String getRealName(Player player);
	
	/**
	 * Call this method to get the skin of a player.
	 *
	 * @param player The player
	 * @return String skin of the player
	 *
	 */
	String getSkin(Player player);
	
	/**
	 * Call this method to set a player the autonick setting.
	 *
	 * @param player The player
	 * @param autonick True or false
	 *
	 */
	void setPlayerAutoNick(Player player, boolean autonick);
	
	/**
	 * Call this method to get the autonick setting of a player.
	 *
	 * @param player The player
	 * @return boolean True if the player enabled autonick
	 *
	 */
	boolean hasPlayerAutoNick(Player player);
	
	/**
	 * Call this method to get if a player is nicked.
	 *
	 * @param player The player
	 * @return boolean True if the player is nicked
	 *
	 */
	boolean isPlayerNicked(Player player);
	
	/**
	 * Call this method to get if a nickname is already in usage.
	 *
	 * @param nickname The nickname
	 * @return boolean True if the nickname is already in usage
	 *
	 */
	boolean isNickNameUsed(String nickname);
	
	/**
	 * Call this method to send a player an actionbar message.
	 *
	 * @param player The player
	 * @param message The message
	 *
	 */
	void sendPlayerActionbar(Player player, String message);
	
	/**
	 * Call this method to stop sending a player an actionbar message.
	 *
	 * @param player The player
	 *
	 */
	void stopPlayerActionbar(Player player);
	
	/**
	 * Call this method to get if a player exists.
	 *
	 * @param player The player
	 * @return boolean True if the player exists
	 *
	 */
	boolean playerExists(Player player);
	
	/**
	 * Call this method to create a player.
	 *
	 * @param player The player
	 *
	 */
	void createPlayer(Player player);
	
	/**
	 * Call this method to remove a player from the nicked players list.
	 *
	 * @param player The player
	 *
	 */
	void removeNickedPlayer(Player player);
	
	/**
	 * Call this method to get all nicked players.
	 *
	 * @return List<Player> List of all nicked players
	 *
	 */
	List<Player> getNickedPlayers();	
}
