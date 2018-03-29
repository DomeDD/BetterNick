/*
 * All rights by DomeDD (2018)
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free and as long as you mention me (DomeDD) 
 * You are NOT allowed to claim this plugin (BetterNick) as your own
 * You are NOT allowed to publish this plugin (BetterNick) or your modified version of this plugin (BetterNick)
 * 
 */
package de.domedd.betternick.mysqlconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.event.Listener;

import de.domedd.betternick.BetterNick;

public class MySQL implements Listener {
	
	private static BetterNick pl;
	private String username, password, database, host, port;
	private Connection connection;
	
	@SuppressWarnings("static-access")
	public MySQL(BetterNick main) {
		this.pl = main;
	}
	
	public MySQL(String username, String password, String database, String host, String port) {
		this.username = username;
		this.password = password;
		this.database = database;
		this.host = host;
		this.port = port;
	}
	public boolean connected() {
		return connection != null;
	}
	public void connect() {
		if(!connected()) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
				pl.log.info("Successfully connected to a MySQL database");
			} catch(SQLException | ClassNotFoundException s) {
				pl.log.warning(s.getMessage());
			}
		} else {
			pl.log.warning("You are already connected to a MySQL database");
		}
	}
	public void disconnect() {
		if(connected()) {
			try {
				connection.close();
			} catch(SQLException s) {
				pl.log.warning(s.getMessage());
			}
		}
	}
	public void createTable() {
		if(connected()) {
			try {
				connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS BetterNick (UUID VARCHAR(100), NAME VARCHAR(100), NICKNAME VARCHAR(100), NICKED VARCHAR(10), AUTONICK VARCHAR(10))");
			} catch (SQLException s) {
				pl.log.warning(s.getMessage());
			}
		}
	}
	public void update(String query) {
		if(connected()) {
			try {
				connection.createStatement().executeUpdate(query);
			} catch (SQLException s) {
				pl.log.warning(s.getMessage());
			}
		}
	}
	public ResultSet result(String query) {
		ResultSet r = null;
		try {
			Statement t = connection.createStatement();
			r = t.executeQuery(query);
		} catch (SQLException s) {
			connect();
			pl.log.warning(s.getMessage());
		} catch (NullPointerException s) {
			connect();
			pl.log.warning(s.getMessage());
		}
		return r;
	}
}
