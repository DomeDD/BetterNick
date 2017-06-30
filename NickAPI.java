/*
 * All rights by DomeDD
 */

public class NickAPI {
	
	/**
   	 * @param p uuid of the player
   	 * @return true if the player exists
	 */
	boolean NickedPlayerExists(UUID p);
	
	/**
   	 * @param p uuid of the player
   	 */
  	void createNickedPlayer(UUID p);
	
	/**
   	 * @param p uuid of the player
    	 * @return true if the player is nicked
   	 */
  	boolean isNicked(UUID p);
  
  	/**
   	 * @param name nickname
   	 * @return true if the nickname is used
   	 */
  	boolean isNickNameUsed(String name);
  
  	/**
   	 * @param p uuid of the player
   	 * @return nickname of the player
   	 */
  	String getNickName(UUID p);
  
  	/**
   	 * @param p uuid of the player
       	 * @return name of the player
   	 */
  	String getRealName(UUID p);
  
  	/**
   	 * @param p uuid of the player
   	 * @param nick nickname
   	 * @param nameprefix nameprefix
   	 * @param nametagprefix nametagprefix
   	 * @param tablistprefix tablistprefix
   	 */
  	void setNickName(UUID p, String nick, String nameprefix, String nametagprefix, String tablistprefix);
  
  	/**
   	 * @param p uuid of the player
   	 * @param nameprefix nameprefix
   	 * @param nametagprefix nametagprefix
   	 * @param tablistprefix tablistprefix
   	 */
	void setRandomNickName(UUID p, String nameprefix, String nametagprefix, String tablistprefix);
  
  	/**
   	 * @param p uuid of the player
   	 * @param pskin skin owner
   	 */
  	void setSkin(UUID p, String pskin);
  
  	/**
   	 * @param p uuid of the player
   	 */
  	void setRandomSkin(UUID p);
  
  	/**
   	 * @param p uuid of the player
   	 */
  	void UnNick(UUID p);
  
  	/**
   	 * @param p uuid of the player
   	 */
  	void resetSkin(UUID p);
}
