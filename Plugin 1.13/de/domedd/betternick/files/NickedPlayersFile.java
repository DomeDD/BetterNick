/*
 * All rights by DomeDD (2018)
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free and as long as you mention me (DomeDD) 
 * You are NOT allowed to claim this plugin (BetterNick) as your own
 * You are NOT allowed to publish this plugin (BetterNick) or your modified version of this plugin (BetterNick)
 * 
 */
package de.domedd.betternick.files;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class NickedPlayersFile {

	public static File file = new File("plugins/BetterNick", "NickedPlayers.yml");
	public static FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
	
	public static void save() {
	    try {
	    	cfg.save(file);
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	}
	public static void loadDefaultFile() {
		cfg.options().copyDefaults(true);
		save();
	}
	public static void reload() {
		try {
			cfg.load(file);
		} catch (IOException | InvalidConfigurationException e) {
			save();
			reload();
		}
	}
}
