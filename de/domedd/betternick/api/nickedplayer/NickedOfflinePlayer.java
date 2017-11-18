/*
 * All rights by DomeDD
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free 
 * You are NOT allowed to claim this plugin as your own
 * You are NOT allowed to publish this plugin or your modified version of this plugin
 * 
 */
package de.domedd.betternick.api.nickedplayer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import de.domedd.betternick.BetterNick;
import de.domedd.betternick.files.NickedPlayersFile;

public class NickedOfflinePlayer implements OfflinePlayer, Listener {
	
	private OfflinePlayer p;
	private static BetterNick pl;
	
	@SuppressWarnings("static-access")
	public NickedOfflinePlayer(BetterNick main) {
		this.pl = main;
	}
	
	public NickedOfflinePlayer(OfflinePlayer arg0) {
		this.p = arg0;
	}
	
	public boolean exists() {
		if(pl.getConfig().getBoolean("MySQL.Enabled")) {
			try {
		    	ResultSet rs = pl.mysql.result("SELECT NAME FROM BetterNick WHERE UUID='" + this.p.getUniqueId() + "'");
		    	if(rs.next()) {
		    		return true;
		    	}
		    } catch (SQLException e) {
		    	e.printStackTrace();
		    }
		} else {
			if(NickedPlayersFile.cfg.contains("NickedPlayers." + this.p.getUniqueId())) {
				return true;
			}
		}
		return false;
	}
	public NickedOfflinePlayer addToDatabase() {
		if(this.p.hasPlayedBefore()) {
			if(pl.getConfig().getBoolean("MySQL.Enabled")) {
				pl.mysql.update("INSERT INTO BetterNick (UUID, NAME, NICKNAME, NICKED, AUTONICK) VALUES ('" + this.p.getUniqueId() + "', '" + this.p.getName() + "', '" + this.p.getName() + "', 'false', 'false');");
			} else {
				NickedPlayersFile.cfg.set("NickedPlayers." + this.p.getUniqueId() + ".Name", this.p.getName());
				NickedPlayersFile.cfg.set("NickedPlayers." + this.p.getUniqueId() + ".NickName", this.p.getName());
				NickedPlayersFile.cfg.set("NickedPlayers." + this.p.getUniqueId() + ".Nicked", false);
				NickedPlayersFile.cfg.set("NickedPlayers." + this.p.getUniqueId() + ".AutoNick", false);
				NickedPlayersFile.save();
			}
		}
		return this;
	}
	public NickedOfflinePlayer removeFromDatabase() {
		if(this.p.hasPlayedBefore() && exists()) {
			if(pl.getConfig().getBoolean("MySQL.Enabled")) {
				pl.mysql.update("DELETE FROM BetterNick WHERE UUID='" + this.p.getUniqueId() + "'");
			} else {
				NickedPlayersFile.cfg.set("NickedPlayers." + this.p.getUniqueId() + ".Name", null);
				NickedPlayersFile.cfg.set("NickedPlayers." + this.p.getUniqueId() + ".NickName", null);
				NickedPlayersFile.cfg.set("NickedPlayers." + this.p.getUniqueId() + ".Nicked", null);
				NickedPlayersFile.cfg.set("NickedPlayers." + this.p.getUniqueId() + ".AutoNick", null);
				NickedPlayersFile.save();
			}
		}
		return this;
	}
	
	/////////////////////////////////////
	
	@Override
	public boolean isOp() {
		return this.p.isOp();
	}
	@Override
	public void setOp(boolean arg0) {
		this.p.setOp(arg0);
	}
	@Override
	public Map<String, Object> serialize() {
		return this.p.serialize();
	}
	@Override
	public Location getBedSpawnLocation() {
		return this.p.getBedSpawnLocation();
	}
	@Override
	public long getFirstPlayed() {
		return this.p.getFirstPlayed();
	}
	@Override
	public long getLastPlayed() {
		return this.p.getLastPlayed();
	}
	@Override
	public String getName() {
		return this.p.getName();
	}
	@Override
	public Player getPlayer() {
		return this.p.getPlayer();
	}
	@Override
	public UUID getUniqueId() {
		return this.p.getUniqueId();
	}
	@Override
	public boolean hasPlayedBefore() {
		return this.p.hasPlayedBefore();
	}
	@Override
	public boolean isBanned() {
		return this.p.isBanned();
	}
	@Override
	public boolean isOnline() {
		return this.p.isOnline();
	}
	@Override
	public boolean isWhitelisted() {
		return this.p.isWhitelisted();
	}
	@Deprecated
	@Override
	public void setBanned(boolean arg0) {
		this.p.setBanned(arg0);
	}
	@Override
	public void setWhitelisted(boolean arg0) {
		this.p.setWhitelisted(arg0);
	}
}
