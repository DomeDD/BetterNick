/*
 * All rights by DomeDD
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free 
 * You are NOT allowed to claim this plugin as your own
 * You are NOT allowed to publish this plugin or your modified version of this plugin
 * 
 */
package BetterNick.Files;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

public class NickedPlayers implements Listener {
	
	public static File file = new File("plugins/BetterNick", "NickedPlayers.yml");
	public static FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
	
	public static void saveFile() {
	    try {
	    	cfg.save(file);
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	}
	public static void loadDefaultFile() {
		cfg.addDefault("NickedPlayers.DefaultUUID.Name", "DefaultName");
		cfg.addDefault("NickedPlayers.DefaultUUID.NickName", "DefaultNickName");
		cfg.addDefault("NickedPlayers.DefaultUUID.Nicked", "DefaultBoolean");
		cfg.addDefault("NickedPlayers.DefaultUUID.AutoNick", "DefaultBoolean");
		cfg.options().copyDefaults(true);
		saveFile();
	}
}
