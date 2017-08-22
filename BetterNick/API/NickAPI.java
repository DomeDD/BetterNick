/*
 * All rights by DomeDD
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free 
 * You are NOT allowed to claim this plugin as your own
 * You are NOT allowed to publish this plugin or your modified version of this plugin
 * 
 */
package BetterNick.API;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import BetterNick.Main;
import BetterNick.Files.NickedPlayers;
import BetterNick.MySQL.MySQL_Connection;
import BetterNick.Versions.v1_10_R1;
import BetterNick.Versions.v1_11_R1;
import BetterNick.Versions.v1_12_R1;
import BetterNick.Versions.v1_8_R2;
import BetterNick.Versions.v1_8_R3;
import BetterNick.Versions.v1_9_R1;
import BetterNick.Versions.v1_9_R2;

public class NickAPI implements Listener {
	
	private static HashMap<Player, Integer> taskID = new HashMap<Player, Integer>();
	private static Main pl;
	@SuppressWarnings("static-access")
	public NickAPI(Main main) {
		this.pl = main;
	}
	public static boolean MySQLEnabled() {
		return pl.getConfig().getBoolean("Config.MySQL");
	}
	public static String getNickName(UUID p) {
		String name = "";
		if(MySQLEnabled()) {
			try {
				ResultSet rs = MySQL_Connection.Result("SELECT * FROM BetterNick WHERE UUID='" + p + "'");
				if(rs.next() && rs.getString("NICKNAME") == null);
				name = rs.getString("NICKNAME");
			} catch(SQLException e) {
				e.printStackTrace();
			}
		} else {
			name = NickedPlayers.cfg.getString("NickedPlayers." + p + ".NickName");
		}
		return name;
	}
	public static String getRealName(UUID p) {
		String name = "";
		if(MySQLEnabled()) {
			try {
				ResultSet rs = MySQL_Connection.Result("SELECT * FROM BetterNick WHERE UUID='" + p + "'");
				if(rs.next() && rs.getString("NAME") == null);
				name = rs.getString("NAME");
			} catch(SQLException e) {
				e.printStackTrace();
			}
		} else {
			name = NickedPlayers.cfg.getString("NickedPlayers." + p + ".Name");
		}
		return name;
	}
	public static boolean NickedPlayerExists(UUID p) {
		boolean exists = false;
		if(MySQLEnabled()) {
			try {
		    	ResultSet rs = MySQL_Connection.Result("SELECT * FROM BetterNick WHERE UUID='" + p + "'");
		    	if (rs.next()) {
		    		exists = true;
		    	} else {
		    		exists = false;
		    	}
		    } catch (SQLException e) {
		    	e.printStackTrace();
		    }
		} else {
			if(NickedPlayers.cfg.contains("NickedPlayers." + p)) {
				exists = true;
			}
		}
	    return exists;
	}
	public static void createNickedPlayer(UUID p) {
		if(!NickedPlayerExists(p)) {
			if(MySQLEnabled()) {
				MySQL_Connection.update("INSERT INTO BetterNick (UUID, NAME, NICKNAME, NICKED, AUTONICK) VALUES ('" + p + "', '" + Bukkit.getPlayer(p).getName() + "', '" + Bukkit.getPlayer(p).getName() + "', 'false', 'false');");
			} else {
				NickedPlayers.cfg.set("NickedPlayers." + p + ".Name", Bukkit.getPlayer(p).getName());
				NickedPlayers.cfg.set("NickedPlayers." + p + ".NickName", Bukkit.getPlayer(p).getName());
				NickedPlayers.cfg.set("NickedPlayers." + p + ".Nicked", false);
				NickedPlayers.cfg.set("NickedPlayers." + p + ".AutoNick", false);
				NickedPlayers.saveFile();
			}
		}
	}
	public static void setNickName(UUID p, String nick, String nameprefix, String nametagprefix, String tablistprefix) {
		if(Bukkit.getVersion().contains("(MC: 1.8.3)")) {
			v1_8_R2.setNickName(p, nick, nameprefix, nametagprefix, tablistprefix);
		}
		if(Bukkit.getVersion().contains("(MC: 1.8.4)") || Bukkit.getVersion().contains("(MC: 1.8.5)") || Bukkit.getVersion().contains("(MC: 1.8.6)") || Bukkit.getVersion().contains("(MC: 1.8.7)") || Bukkit.getVersion().contains("(MC: 1.8.8)")) {
			v1_8_R3.setNickName(p, nick, nameprefix, nametagprefix, tablistprefix);
		}
		if(Bukkit.getVersion().contains("(MC: 1.9)") || Bukkit.getVersion().contains("(MC: 1.9.1)") || Bukkit.getVersion().contains("(MC: 1.9.2)") || Bukkit.getVersion().contains("(MC: 1.9.3)")) {
			v1_9_R1.setNickName(p, nick, nameprefix, nametagprefix, tablistprefix);
		}
		if(Bukkit.getVersion().contains("(MC: 1.9.4)")) {
			v1_9_R2.setNickName(p, nick, nameprefix, nametagprefix, tablistprefix);
		}
		if(Bukkit.getVersion().contains("(MC: 1.10)") || Bukkit.getVersion().contains("(MC: 1.10.1)") || Bukkit.getVersion().contains("(MC: 1.10.2)")) {
			v1_10_R1.setNickName(p, nick, nameprefix, nametagprefix, tablistprefix);
		}
		if(Bukkit.getVersion().contains("(MC: 1.11)") || Bukkit.getVersion().contains("(MC: 1.11.1)") || Bukkit.getVersion().contains("(MC: 1.11.2)")) {
			v1_11_R1.setNickName(p, nick, nameprefix, nametagprefix, tablistprefix);
		}
		if(Bukkit.getVersion().contains("(MC: 1.12)") || Bukkit.getVersion().contains("(MC: 1.12.1)")) {
			v1_12_R1.setNickName(p, nick, nameprefix, nametagprefix, tablistprefix);
		}
	}
	public static void setRandomNickName(UUID p, String nameprefix, String nametagprefix, String tablistprefix) {
		if(Bukkit.getVersion().contains("(MC: 1.8.3)")) {
			v1_8_R2.setRandomNickName(p, nameprefix, nametagprefix, tablistprefix);
		}
		if(Bukkit.getVersion().contains("(MC: 1.8.4)") || Bukkit.getVersion().contains("(MC: 1.8.5)") || Bukkit.getVersion().contains("(MC: 1.8.6)") || Bukkit.getVersion().contains("(MC: 1.8.7)") || Bukkit.getVersion().contains("(MC: 1.8.8)")) {
			v1_8_R3.setRandomNickName(p, nameprefix, nametagprefix, tablistprefix);
		}
		if(Bukkit.getVersion().contains("(MC: 1.9)") || Bukkit.getVersion().contains("(MC: 1.9.1)") || Bukkit.getVersion().contains("(MC: 1.9.2)") || Bukkit.getVersion().contains("(MC: 1.9.3)")) {
			v1_9_R1.setRandomNickName(p, nameprefix, nametagprefix, tablistprefix);
		}
		if(Bukkit.getVersion().contains("(MC: 1.9.4)")) {
			v1_9_R2.setRandomNickName(p, nameprefix, nametagprefix, tablistprefix);
		}
		if(Bukkit.getVersion().contains("(MC: 1.10)") || Bukkit.getVersion().contains("(MC: 1.10.1)") || Bukkit.getVersion().contains("(MC: 1.10.2)")) {
			v1_10_R1.setRandomNickName(p, nameprefix, nametagprefix, tablistprefix);
		}
		if(Bukkit.getVersion().contains("(MC: 1.11)") || Bukkit.getVersion().contains("(MC: 1.11.1)") || Bukkit.getVersion().contains("(MC: 1.11.2)")) {
			v1_11_R1.setRandomNickName(p, nameprefix, nametagprefix, tablistprefix);
		}
		if(Bukkit.getVersion().contains("(MC: 1.12)") || Bukkit.getVersion().contains("(MC: 1.12.1)")) {
			v1_12_R1.setRandomNickName(p, nameprefix, nametagprefix, tablistprefix);
		}
	}
	public static void UnNick(UUID p) {
		if(Bukkit.getVersion().contains("(MC: 1.8.3)")) {
			v1_8_R2.UnNick(p);
		}
		if(Bukkit.getVersion().contains("(MC: 1.8.4)") || Bukkit.getVersion().contains("(MC: 1.8.5)") || Bukkit.getVersion().contains("(MC: 1.8.6)") || Bukkit.getVersion().contains("(MC: 1.8.7)") || Bukkit.getVersion().contains("(MC: 1.8.8)")) {
			v1_8_R3.UnNick(p);
		}
		if(Bukkit.getVersion().contains("(MC: 1.9)") || Bukkit.getVersion().contains("(MC: 1.9.1)") || Bukkit.getVersion().contains("(MC: 1.9.2)") || Bukkit.getVersion().contains("(MC: 1.9.3)")) {
			v1_9_R1.UnNick(p);
		}
		if(Bukkit.getVersion().contains("(MC: 1.9.4)")) {
			v1_9_R2.UnNick(p);
		}
		if(Bukkit.getVersion().contains("(MC: 1.10)") || Bukkit.getVersion().contains("(MC: 1.10.1)") || Bukkit.getVersion().contains("(MC: 1.10.2)")) {
			v1_10_R1.UnNick(p);
		}
		if(Bukkit.getVersion().contains("(MC: 1.11)") || Bukkit.getVersion().contains("(MC: 1.11.1)") || Bukkit.getVersion().contains("(MC: 1.11.2)")) {
			v1_11_R1.UnNick(p);
		}
		if(Bukkit.getVersion().contains("(MC: 1.12)") || Bukkit.getVersion().contains("(MC: 1.12.1)")) {
			v1_12_R1.UnNick(p);
		}
	}
	public static void setSkin(UUID p, String pskin) {
		if(Bukkit.getVersion().contains("(MC: 1.8.3)")) {
			v1_8_R2.setSkin(p, pskin);
		}
		if(Bukkit.getVersion().contains("(MC: 1.8.4)") || Bukkit.getVersion().contains("(MC: 1.8.5)") || Bukkit.getVersion().contains("(MC: 1.8.6)") || Bukkit.getVersion().contains("(MC: 1.8.7)") || Bukkit.getVersion().contains("(MC: 1.8.8)")) {
			v1_8_R3.setSkin(p, pskin);
		}
		if(Bukkit.getVersion().contains("(MC: 1.9)") || Bukkit.getVersion().contains("(MC: 1.9.1)") || Bukkit.getVersion().contains("(MC: 1.9.2)") || Bukkit.getVersion().contains("(MC: 1.9.3)")) {
			v1_9_R1.setSkin(p, pskin);
		}
		if(Bukkit.getVersion().contains("(MC: 1.9.4)")) {
			v1_9_R2.setSkin(p, pskin);
		}
		if(Bukkit.getVersion().contains("(MC: 1.10)") || Bukkit.getVersion().contains("(MC: 1.10.1)") || Bukkit.getVersion().contains("(MC: 1.10.2)")) {
			v1_10_R1.setSkin(p, pskin);
		}
		if(Bukkit.getVersion().contains("(MC: 1.11)") || Bukkit.getVersion().contains("(MC: 1.11.1)") || Bukkit.getVersion().contains("(MC: 1.11.2)")) {
			v1_11_R1.setSkin(p, pskin);
		}
		if(Bukkit.getVersion().contains("(MC: 1.12)") || Bukkit.getVersion().contains("(MC: 1.12.1)")) {
			v1_12_R1.setSkin(p, pskin);
		}
	}
	public static void setRandomSkin(UUID p) {
		if(Bukkit.getVersion().contains("(MC: 1.8.3)")) {
			v1_8_R2.setRandomSkin(p);
		}
		if(Bukkit.getVersion().contains("(MC: 1.8.4)") || Bukkit.getVersion().contains("(MC: 1.8.5)") || Bukkit.getVersion().contains("(MC: 1.8.6)") || Bukkit.getVersion().contains("(MC: 1.8.7)") || Bukkit.getVersion().contains("(MC: 1.8.8)")) {
			v1_8_R3.setRandomSkin(p);
		}
		if(Bukkit.getVersion().contains("(MC: 1.9)") || Bukkit.getVersion().contains("(MC: 1.9.1)") || Bukkit.getVersion().contains("(MC: 1.9.2)") || Bukkit.getVersion().contains("(MC: 1.9.3)")) {
			v1_9_R1.setRandomSkin(p);
		}
		if(Bukkit.getVersion().contains("(MC: 1.9.4)")) {
			v1_9_R2.setRandomSkin(p);
		}
		if(Bukkit.getVersion().contains("(MC: 1.10)") || Bukkit.getVersion().contains("(MC: 1.10.1)") || Bukkit.getVersion().contains("(MC: 1.10.2)")) {
			v1_10_R1.setRandomSkin(p);
		}
		if(Bukkit.getVersion().contains("(MC: 1.11)") || Bukkit.getVersion().contains("(MC: 1.11.1)") || Bukkit.getVersion().contains("(MC: 1.11.2)")) {
			v1_11_R1.setRandomSkin(p);
		}
		if(Bukkit.getVersion().contains("(MC: 1.12)") || Bukkit.getVersion().contains("(MC: 1.12.1)")) {
			v1_12_R1.setRandomSkin(p);
		}
	}
	public static void resetSkin(UUID p) {
		if(Bukkit.getVersion().contains("(MC: 1.8.3)")) {
			v1_8_R2.resetSkin(p);
		}
		if(Bukkit.getVersion().contains("(MC: 1.8.4)") || Bukkit.getVersion().contains("(MC: 1.8.5)") || Bukkit.getVersion().contains("(MC: 1.8.6)") || Bukkit.getVersion().contains("(MC: 1.8.7)") || Bukkit.getVersion().contains("(MC: 1.8.8)")) {
			v1_8_R3.resetSkin(p);
		}
		if(Bukkit.getVersion().contains("(MC: 1.9)") || Bukkit.getVersion().contains("(MC: 1.9.1)") || Bukkit.getVersion().contains("(MC: 1.9.2)") || Bukkit.getVersion().contains("(MC: 1.9.3)")) {
			v1_9_R1.resetSkin(p);
		}
		if(Bukkit.getVersion().contains("(MC: 1.9.4)")) {
			v1_9_R2.resetSkin(p);
		}
		if(Bukkit.getVersion().contains("(MC: 1.10)") || Bukkit.getVersion().contains("(MC: 1.10.1)") || Bukkit.getVersion().contains("(MC: 1.10.2)")) {
			v1_10_R1.resetSkin(p);
		}
		if(Bukkit.getVersion().contains("(MC: 1.11)") || Bukkit.getVersion().contains("(MC: 1.11.1)") || Bukkit.getVersion().contains("(MC: 1.11.2)")) {
			v1_11_R1.resetSkin(p);
		}
		if(Bukkit.getVersion().contains("(MC: 1.12)") || Bukkit.getVersion().contains("(MC: 1.12.1)")) {
			v1_12_R1.resetSkin(p);
		}
	}
	public static void autoNick(UUID p, boolean autonick) {
		if(MySQLEnabled()) {
			MySQL_Connection.update("UPDATE BetterNick SET AUTONICK='" + autonick + "' WHERE UUID='" + p + "'");
		} else {
			NickedPlayers.cfg.set("NickedPlayers." + p + ".AutoNick", autonick);
			NickedPlayers.saveFile();
		}
	}
	public static boolean autoNick(UUID p) {
		boolean autoNick = false;
		if(MySQLEnabled()) {
			try {
				ResultSet rs = MySQL_Connection.Result("SELECT * FROM BetterNick WHERE UUID='" + p + "'");
				if(rs.next()) {
					autoNick = rs.getBoolean("AUTONICK");
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		} else {
			autoNick = NickedPlayers.cfg.getBoolean("NickedPlayers." + p + ".AutoNick");
		}
		return autoNick;
	}
	public static boolean isNicked(UUID p) {
		boolean nicked = false;
		if(MySQLEnabled()) {
			try {
				ResultSet rs = MySQL_Connection.Result("SELECT * FROM BetterNick WHERE UUID='" + p + "'");
				if(rs.next()) {
					nicked = rs.getBoolean("NICKED");
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		} else {
			nicked = NickedPlayers.cfg.getBoolean("NickedPlayers." + p + ".Nicked");
		}
		return nicked;
	}
	public static boolean isNickNameUsed(String name) {
		boolean used = false;
		if(MySQLEnabled()) {
			try {
				ResultSet rs = MySQL_Connection.Result("SELECT NICKNAME FROM BetterNick WHERE NICKNAME='" + name + "'");
				if(rs.next()) {
					used = true;
				} else {
					used = false;
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		} else {
			for(Player all : Bukkit.getOnlinePlayers()) {
				if(NickedPlayers.cfg.getString("NickedPlayers." + all.getUniqueId().toString() + ".NickName") != null && NickedPlayers.cfg.getString("NickedPlayers." + all.getUniqueId().toString() + ".NickName").equals(name)) {
					used = true;
				}
			}
		}
		return used;
	}
	public static void sendActionBar(UUID p, String msg) {
		int tid = 0;
		tid = Bukkit.getScheduler().scheduleSyncRepeatingTask(pl, new Runnable() {
			@Override
			public void run() {
				if(Bukkit.getVersion().contains("(MC: 1.8.3)")) {
					v1_8_R2.sendActionBar(p, msg);
				}
				if(Bukkit.getVersion().contains("(MC: 1.8.4)") || Bukkit.getVersion().contains("(MC: 1.8.5)") || Bukkit.getVersion().contains("(MC: 1.8.6)") || Bukkit.getVersion().contains("(MC: 1.8.7)") || Bukkit.getVersion().contains("(MC: 1.8.8)")) {
					v1_8_R3.sendActionBar(p, msg);
				}
				if(Bukkit.getVersion().contains("(MC: 1.9)") || Bukkit.getVersion().contains("(MC: 1.9.1)") || Bukkit.getVersion().contains("(MC: 1.9.2)") || Bukkit.getVersion().contains("(MC: 1.9.3)")) {
					v1_9_R1.sendActionBar(p, msg);
				}
				if(Bukkit.getVersion().contains("(MC: 1.9.4)")) {
					v1_9_R2.sendActionBar(p, msg);
				}
				if(Bukkit.getVersion().contains("(MC: 1.10)") || Bukkit.getVersion().contains("(MC: 1.10.1)") || Bukkit.getVersion().contains("(MC: 1.10.2)")) {
					v1_10_R1.sendActionBar(p, msg);
				}
				if(Bukkit.getVersion().contains("(MC: 1.11)") || Bukkit.getVersion().contains("(MC: 1.11.1)") || Bukkit.getVersion().contains("(MC: 1.11.2)")) {
					v1_11_R1.sendActionBar(p, msg);
				}
				if(Bukkit.getVersion().contains("(MC: 1.12)") || Bukkit.getVersion().contains("(MC: 1.12.1)")) {
					v1_12_R1.sendActionBar(p, msg);
				}
			}
		}, 0, 40);
		taskID.put(Bukkit.getPlayer(p), tid);
	}
	public static void endActionBar(UUID p) {
		if(taskID.containsKey(Bukkit.getPlayer(p))) {
			int tid = taskID.get(Bukkit.getPlayer(p));
			pl.getServer().getScheduler().cancelTask(tid);
			taskID.remove(Bukkit.getPlayer(p));
		}
	}
}
