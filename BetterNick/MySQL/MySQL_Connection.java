/*
 * All rights by DomeDD
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free 
 * You are NOT allowed to claim this plugin as your own
 * You are NOT allowed to publish this plugin or your modified version of this plugin
 * 
 */
package BetterNick.MySQL;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

import BetterNick.Main;

public class MySQL_Connection implements Listener {

	private static Main pl;
	@SuppressWarnings("static-access")
	public MySQL_Connection(Main main) {
		this.pl = main;
	}
	private static String username;
	private static String password;
	private static String database;
	private static String host;
	private static String port;
	private static Connection connection;
	public static File file = new File("plugins/BetterNick", "MySQL.yml");
	public static FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
	public static void saveFile() {
	    try {
	      cfg.save(file);
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	}
	public MySQL_Connection(String user, String password, String host, String database) {}
	public static void setDefaultMySQL() {
		cfg.options().copyDefaults(true);
		cfg.addDefault("Username", "root");
		cfg.addDefault("Password", "password");
		cfg.addDefault("Database", "localhost");
		cfg.addDefault("Host", "localhost");
		cfg.addDefault("Port", "3306");
		saveFile();
	}
	public static void connectMySQL() {
		username = cfg.getString("Username");
		password = cfg.getString("Password");
		database = cfg.getString("Database");
		host = cfg.getString("Host");
		port = cfg.getString("Port");
	}
	public static boolean connected() {
		return connection != null;
	}
	public static void connect() {
		if(!connected()) {
			try {
				connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?user=" + username + "&password=" + password + "&autoReconnect=true");
				pl.log.info("Connected to MySQL Database successfully");
			} catch (SQLException s) {
				s.printStackTrace();
			}
		}
	}
	public static void close() {
		if(connected()) {
			try {
				connection.close();
				pl.log.info("Disconnected from MySQL Database successfully");
			} catch (SQLException s) {
				s.printStackTrace();
			}
		}
	}
	public static void addTable() {
		if(connected()) {
			try {
				connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS BetterNick (UUID VARCHAR(100), NAME VARCHAR(100), NICKNAME VARCHAR(100), NICKED VARCHAR(10), AUTONICK VARCHAR(10))");
			} catch (SQLException s) {
				s.printStackTrace();
			}
		}
	}
	public static void update(String query) {
		if(connected()) {
			try {
				connection.createStatement().executeUpdate(query);
			} catch (SQLException s) {
				s.printStackTrace();
			}
		}
	}
	public static ResultSet Result(String query) {
		ResultSet r = null;
		try {
			Statement t = connection.createStatement();
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
