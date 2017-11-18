/*
 * All rights by DomeDD
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free 
 * You are NOT allowed to claim this plugin as your own
 * You are NOT allowed to publish this plugin or your modified version of this plugin
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
	private static String USERNAME;
	private static String PASSWORD;
	private static String DATABASE;
	private static String HOST;
	private static String PORT;
	private static Connection CONNECTION;
	
	@SuppressWarnings("static-access")
	public MySQL(BetterNick main) {
		this.pl = main;
	}
	
	@SuppressWarnings("static-access")
	public MySQL(String arg0, String arg1, String arg2, String arg3, String arg4) {
		this.USERNAME = arg0;
		this.PASSWORD = arg1;
		this.DATABASE = arg2;
		this.HOST = arg3;
		this.PORT = arg4;
	}
	public static boolean connected() {
		return CONNECTION != null;
	}
	public void connect() {
		if(!connected()) {
			try {
				CONNECTION = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?user=" + USERNAME + "&password=" + PASSWORD + "&autoReconnect=true");
				pl.log.info("Connected to MySQL Database successfully");
			} catch (SQLException s) {
				s.printStackTrace();
			}
		}
	}
	public void close() {
		if(connected()) {
			try {
				CONNECTION.close();
				pl.log.info("Disconnected from MySQL Database successfully");
			} catch (SQLException s) {
				s.printStackTrace();
			}
		}
	}
	public void addTable() {
		if(connected()) {
			try {
				CONNECTION.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS BetterNick (UUID VARCHAR(100), NAME VARCHAR(100), NICKNAME VARCHAR(100), NICKED VARCHAR(10), AUTONICK VARCHAR(10))");
			} catch (SQLException s) {
				s.printStackTrace();
			}
		}
	}
	public void update(String query) {
		if(connected()) {
			try {
				CONNECTION.createStatement().executeUpdate(query);
			} catch (SQLException s) {
				s.printStackTrace();
			}
		}
	}
	public ResultSet result(String query) {
		ResultSet r = null;
		try {
			Statement t = CONNECTION.createStatement();
			r = t.executeQuery(query);
		} catch (SQLException s) {
			connect();
			System.err.println(s);
		} catch (NullPointerException s) {
			connect();
			System.err.println(s);
		}
		return r;
	}
}
