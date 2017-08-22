/*
 * All rights by DomeDD
 */
public class PlayerNickEvent {
	
	/**
   	 * @return player
	 */
	Player getPlayer();
	
	/**
   	 * @return nickname
	 */
	String getNickName();
	
	/**
   	 * @param arg0 message
	 */
	void setNickMessage(String arg0);
	
	/**
   	 * @param arg0 message
	 */
	void setNickActionbarMessage(String arg0);
	
	void stopNickActionbarMessage();
}
