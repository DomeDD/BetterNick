/*
 * All rights by DomeDD
 */

public class NickAPI {
	
	/**
   	 * @param p player
   	 * @return true if the player exists
	 */
	boolean NickedPlayerExists(Player p);
	
	/**
   	 * @param p player
   	 */
  	void createNickedPlayer(Player p);
	
	/**
   	 * @param p player
    	 * @return true if the player is nicked
   	 */
  	boolean isNicked(Player p);
  
  	/**
   	 * @param name nickname
   	 * @return true if the nickname is used
   	 */
  	boolean isNickNameUsed(String name);
  
  	/**
   	 * @param p player
   	 * @return nickname of the player
   	 */
  	String getNickName(UUID p);
  
  	/**
   	 * @param p player
       	 * @return name of the player
   	 */
  	String getRealName(Player p);
  
  	/**
   	 * @param p player
   	 * @param nick nickname
   	 * @param nameprefix nameprefix
   	 * @param nametagprefix nametagprefix
   	 * @param tablistprefix tablistprefix
   	 */
  	void setNickName(Player p, String nick, String nameprefix, String nametagprefix, String tablistprefix);
  
  	/**
   	 * @param p player
   	 * @param nameprefix nameprefix
   	 * @param nametagprefix nametagprefix
   	 * @param tablistprefix tablistprefix
   	 */
	void setRandomNickName(Player p, String nameprefix, String nametagprefix, String tablistprefix);
  
  	/**
   	 * @param p player
   	 * @param pskin skin owner
   	 */
  	void setSkin(Player p, String pskin);
  
  	/**
   	 * @param p player
   	 */
  	void setRandomSkin(Player p);
  
  	/**
   	 * @param p player
   	 */
  	void UnNick(Player p);
  
  	/**
   	 * @param p player
   	 */
  	void resetSkin(Player p);
}
