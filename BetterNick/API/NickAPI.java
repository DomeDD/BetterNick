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
	public static String getNickName(Player p) {
		UUID uuid = p.getUniqueId();
		String name = "";
		if(MySQLEnabled()) {
			try {
				ResultSet rs = MySQL_Connection.Result("SELECT NICKNAME FROM BetterNick WHERE UUID='" + uuid + "'");
				if(rs.next() && rs.getString("NICKNAME") == null);
				name = rs.getString("NICKNAME");
			} catch(SQLException e) {
				e.printStackTrace();
			}
		} else {
			name = NickedPlayers.cfg.getString("NickedPlayers." + uuid + ".NickName");
		}
		return name;
	}
	public static String getRealName(Player p) {
		UUID uuid = p.getUniqueId();
		String name = "";
		if(MySQLEnabled()) {
			try {
				ResultSet rs = MySQL_Connection.Result("SELECT NAME FROM BetterNick WHERE UUID='" + uuid + "'");
				if(rs.next() && rs.getString("NAME") == null);
				name = rs.getString("NAME");
			} catch(SQLException e) {
				e.printStackTrace();
			}
		} else {
			name = NickedPlayers.cfg.getString("NickedPlayers." + uuid + ".Name");
		}
		return name;
	}
	public static boolean NickedPlayerExists(Player p) {
		UUID uuid = p.getUniqueId();
		boolean exists = false;
		if(MySQLEnabled()) {
			try {
		    	ResultSet rs = MySQL_Connection.Result("SELECT NAME FROM BetterNick WHERE UUID='" + uuid + "'");
		    	if (rs.next()) {
		    		exists = true;
		    	} else {
		    		exists = false;
		    	}
		    } catch (SQLException e) {
		    	e.printStackTrace();
		    }
		} else {
			if(NickedPlayers.cfg.contains("NickedPlayers." + uuid)) {
				exists = true;
			}
		}
	    return exists;
	}
	public static void createNickedPlayer(Player p) {
		UUID uuid = p.getUniqueId();
		if(!NickedPlayerExists(p)) {
			if(MySQLEnabled()) {
				MySQL_Connection.update("INSERT INTO BetterNick (UUID, NAME, NICKNAME, NICKED, AUTONICK) VALUES ('" + uuid + "', '" + p.getName() + "', '" + p.getName() + "', 'false', 'false');");
			} else {
				NickedPlayers.cfg.set("NickedPlayers." + uuid + ".Name", p.getName());
				NickedPlayers.cfg.set("NickedPlayers." + uuid + ".NickName", p.getName());
				NickedPlayers.cfg.set("NickedPlayers." + uuid + ".Nicked", false);
				NickedPlayers.cfg.set("NickedPlayers." + uuid + ".AutoNick", false);
				NickedPlayers.saveFile();
			}
		}
	}
	public static void setNickName(Player p, String nick, String nameprefix, String nametagprefix, String tablistprefix) {
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
		if(Bukkit.getVersion().contains("(MC: 1.12)") || Bukkit.getVersion().contains("(MC: 1.12.1)") || Bukkit.getVersion().contains("(MC: 1.12.2)")) {
			v1_12_R1.setNickName(p, nick, nameprefix, nametagprefix, tablistprefix);
		}
	}
	public static void setRandomNickName(Player p, String nameprefix, String nametagprefix, String tablistprefix) {
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
		if(Bukkit.getVersion().contains("(MC: 1.12)") || Bukkit.getVersion().contains("(MC: 1.12.1)") || Bukkit.getVersion().contains("(MC: 1.12.2)")) {
			v1_12_R1.setRandomNickName(p, nameprefix, nametagprefix, tablistprefix);
		}
	}
	public static void UnNick(Player p) {
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
		if(Bukkit.getVersion().contains("(MC: 1.12)") || Bukkit.getVersion().contains("(MC: 1.12.1)") || Bukkit.getVersion().contains("(MC: 1.12.2)")) {
			v1_12_R1.UnNick(p);
		}
	}
	public static void setSkin(Player p, String pskin) {
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
		if(Bukkit.getVersion().contains("(MC: 1.12)") || Bukkit.getVersion().contains("(MC: 1.12.1)") || Bukkit.getVersion().contains("(MC: 1.12.2)")) {
			v1_12_R1.setSkin(p, pskin);
		}
	}
	public static void setRandomSkin(Player p) {
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
		if(Bukkit.getVersion().contains("(MC: 1.12)") || Bukkit.getVersion().contains("(MC: 1.12.1)") || Bukkit.getVersion().contains("(MC: 1.12.2)")) {
			v1_12_R1.setRandomSkin(p);
		}
	}
	public static void resetSkin(Player p) {
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
		if(Bukkit.getVersion().contains("(MC: 1.12)") || Bukkit.getVersion().contains("(MC: 1.12.1)") || Bukkit.getVersion().contains("(MC: 1.12.2)")) {
			v1_12_R1.resetSkin(p);
		}
	}
	public static void UnNickOnLeave(Player p) {
		if(Bukkit.getVersion().contains("(MC: 1.8.3)")) {
			v1_8_R2.UnNickOnLeave(p);
		}
		if(Bukkit.getVersion().contains("(MC: 1.8.4)") || Bukkit.getVersion().contains("(MC: 1.8.5)") || Bukkit.getVersion().contains("(MC: 1.8.6)") || Bukkit.getVersion().contains("(MC: 1.8.7)") || Bukkit.getVersion().contains("(MC: 1.8.8)")) {
			v1_8_R3.UnNickOnLeave(p);
		}
		if(Bukkit.getVersion().contains("(MC: 1.9)") || Bukkit.getVersion().contains("(MC: 1.9.1)") || Bukkit.getVersion().contains("(MC: 1.9.2)") || Bukkit.getVersion().contains("(MC: 1.9.3)")) {
			v1_9_R1.UnNickOnLeave(p);
		}
		if(Bukkit.getVersion().contains("(MC: 1.9.4)")) {
			v1_9_R2.UnNickOnLeave(p);
		}
		if(Bukkit.getVersion().contains("(MC: 1.10)") || Bukkit.getVersion().contains("(MC: 1.10.1)") || Bukkit.getVersion().contains("(MC: 1.10.2)")) {
			v1_10_R1.UnNickOnLeave(p);
		}
		if(Bukkit.getVersion().contains("(MC: 1.11)") || Bukkit.getVersion().contains("(MC: 1.11.1)") || Bukkit.getVersion().contains("(MC: 1.11.2)")) {
			v1_11_R1.UnNickOnLeave(p);
		}
		if(Bukkit.getVersion().contains("(MC: 1.12)") || Bukkit.getVersion().contains("(MC: 1.12.1)") || Bukkit.getVersion().contains("(MC: 1.12.2)")) {
			v1_12_R1.UnNickOnLeave(p);
		}
	}
	public static void autoNick(Player p, boolean autonick) {
		UUID uuid = p.getUniqueId();
		if(MySQLEnabled()) {
			MySQL_Connection.update("UPDATE BetterNick SET AUTONICK='" + autonick + "' WHERE UUID='" + uuid + "'");
		} else {
			NickedPlayers.cfg.set("NickedPlayers." + uuid + ".AutoNick", autonick);
			NickedPlayers.saveFile();
		}
	}
	public static boolean autoNick(Player p) {
		UUID uuid = p.getUniqueId();
		boolean autoNick = false;
		if(MySQLEnabled()) {
			try {
				ResultSet rs = MySQL_Connection.Result("SELECT * FROM BetterNick WHERE UUID='" + uuid + "'");
				if(rs.next()) {
					autoNick = rs.getBoolean("AUTONICK");
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		} else {
			autoNick = NickedPlayers.cfg.getBoolean("NickedPlayers." + uuid + ".AutoNick");
		}
		return autoNick;
	}
	public static boolean isNicked(Player p) {
		UUID uuid = p.getUniqueId();
		boolean nicked = false;
		if(MySQLEnabled()) {
			try {
				ResultSet rs = MySQL_Connection.Result("SELECT * FROM BetterNick WHERE UUID='" + uuid + "'");
				if(rs.next()) {
					nicked = rs.getBoolean("NICKED");
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		} else {
			nicked = NickedPlayers.cfg.getBoolean("NickedPlayers." + uuid + ".Nicked");
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
	public static void sendActionBar(Player p, String msg) {
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
				if(Bukkit.getVersion().contains("(MC: 1.12)") || Bukkit.getVersion().contains("(MC: 1.12.1)") || Bukkit.getVersion().contains("(MC: 1.12.2)")) {
					v1_12_R1.sendActionBar(p, msg);
				}
			}
		}, 0, 40);
		taskID.put(p, tid);
	}
	public static void endActionBar(Player p) {
		if(taskID.containsKey(p)) {
			int tid = taskID.get(p);
			pl.getServer().getScheduler().cancelTask(tid);
			taskID.remove(p);
		}
	}
}
