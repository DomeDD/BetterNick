/*
 * All rights by DomeDD
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free 
 * You are NOT allowed to claim this plugin as your own
 * You are NOT allowed to publish this plugin or your modified version of this plugin
 * 
 */
package de.domedd.betternick;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.mojang.authlib.GameProfile;

import de.domedd.betternick.addons.autonickitem.AutoNickItem;
import de.domedd.betternick.addons.randomnickgui.RandomNickGui;
import de.domedd.betternick.api.VanishManager;
import de.domedd.betternick.api.nickedplayer.NickedOfflinePlayer;
import de.domedd.betternick.api.nickedplayer.NickedPlayer;
import de.domedd.betternick.commands.AutoNickCMD;
import de.domedd.betternick.commands.NickCMD;
import de.domedd.betternick.commands.NickListCMD;
import de.domedd.betternick.commands.RealNameCMD;
import de.domedd.betternick.commands.SkinCMD;
import de.domedd.betternick.commands.UnNickCMD;
import de.domedd.betternick.files.NickedPlayersFile;
import de.domedd.betternick.listeners.AutoNick;
import de.domedd.betternick.listeners.BetterNickEvents;
import de.domedd.betternick.listeners.PlayerDeath;
import de.domedd.betternick.mysqlconnection.MySQL;
import de.domedd.betternick.packets.VersionChecker;
import de.domedd.betternick.packets.v1_10_R1;
import de.domedd.betternick.packets.v1_11_R1;
import de.domedd.betternick.packets.v1_12_R1;
import de.domedd.betternick.packets.v1_8_R2;
import de.domedd.betternick.packets.v1_8_R3;
import de.domedd.betternick.packets.v1_9_R1;
import de.domedd.betternick.packets.v1_9_R2;
import net.milkbowl.vault.chat.Chat;

public class BetterNick extends JavaPlugin implements Listener {
	
	public MySQL mysql;
	public Field nameField;
	public Chat chat = null;
	public boolean nte = false;
	public boolean cloudnet = false;
	public boolean coloredtags = false;
	public Logger log = this.getLogger();
	public ArrayList<String> nickedPlayers = new ArrayList<String>();
	
	@Override
	public void onEnable() {
		switch(VersionChecker.getBukkitVersion()) {
		case v1_8_R1:
			log.warning("Minecraft version v1_8_R1 is not supported");
			break;
		case v1_8_R2:
			log.info("Hooking into v1_8_R2...");
			this.getServer().getPluginManager().registerEvents(new v1_8_R2(this), this);
			loadPlugin();
			break;
		case v1_8_R3:
			log.info("Hooking into v1_8_R3...");
			this.getServer().getPluginManager().registerEvents(new v1_8_R3(this), this);
			loadPlugin();
			break;
		case v1_9_R1:
			log.info("Hooking into v1_9_R1...");
			this.getServer().getPluginManager().registerEvents(new v1_9_R1(this), this);
			loadPlugin();
			break;
		case v1_9_R2:
			log.info("Hooking into v1_9_R2...");
			this.getServer().getPluginManager().registerEvents(new v1_9_R2(this), this);
			loadPlugin();
			break;
		case v1_10_R1:
			log.info("Hooking into v1_10_R1...");
			this.getServer().getPluginManager().registerEvents(new v1_10_R1(this), this);
			loadPlugin();
			break;
		case v1_11_R1:
			log.info("Hooking into v1_11_R1...");
			this.getServer().getPluginManager().registerEvents(new v1_11_R1(this), this);
			loadPlugin();
			break;
		case v1_12_R1:
			log.info("Hooking into v1_12_R1...");
			this.getServer().getPluginManager().registerEvents(new v1_12_R1(this), this);
			loadPlugin();
			break;
		}
	}
	@Override
	public void onDisable() {
		if(this.getConfig().getBoolean("MySQL.Enabled")) {
			mysql.close();
		}
	}
	
	private void loadPlugin() {
		saveDefaultConfig();
		nameField = getField(GameProfile.class, "name");
		this.getServer().getPluginManager().registerEvents(new PlayerDeath(this), this);
		this.getServer().getPluginManager().registerEvents(new NickedPlayer(this), this);
		this.getServer().getPluginManager().registerEvents(new NickedOfflinePlayer(this), this);
		this.getServer().getPluginManager().registerEvents(new MySQL(this), this);
		if(getConfig().getBoolean("Config.API Mode")) {
	    	log.info("Plugin activated in API mode");
		} else {
			this.getServer().getPluginManager().registerEvents(new AutoNick(this), this);
			this.getServer().getPluginManager().registerEvents(new BetterNickEvents(this), this);
			if(this.getConfig().getBoolean("Addons.AutoNick Item.Enabled")) {
				this.getServer().getPluginManager().registerEvents(new AutoNickItem(this), this);
			}
			if(this.getConfig().getBoolean("Addons.Random Nick Gui.Enabled")) {
				this.getServer().getPluginManager().registerEvents(new RandomNickGui(this), this);
			}
			getCommand("nick").setExecutor(new NickCMD(this));
			getCommand("unnick").setExecutor(new UnNickCMD());
			getCommand("realname").setExecutor(new RealNameCMD(this));
			getCommand("skin").setExecutor(new SkinCMD());
			getCommand("nicklist").setExecutor(new NickListCMD(this));
			getCommand("autonick").setExecutor(new AutoNickCMD(this));
		}
		if(Bukkit.getPluginManager().getPlugin("NametagEdit") != null) {
			log.info("Hooking into NametagEdit...");
			nte = true;
		}
		if(Bukkit.getPluginManager().getPlugin("SuperVanish") != null || Bukkit.getPluginManager().getPlugin("PremiumVanish") != null) {
			log.info("Hooking into SuperVanish/PremiumVanish...");
			this.getServer().getPluginManager().registerEvents(new VanishManager(this), this);
		}
    	if(getConfig().getBoolean("Config.Use Vault")) {
    		RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
            if (chatProvider != null) {
            	log.info("Hooking into " + chatProvider.getProvider().getName() + " via Vault...");
                chat = chatProvider.getProvider();
            }
    	}
    	if(Bukkit.getPluginManager().getPlugin("CloudNetAPI") != null) {
    		log.info("Hooking into CloudNetAPI...");
			cloudnet = true;
		}
    	if(Bukkit.getPluginManager().getPlugin("ColoredTags") != null) {
    		log.info("Hooking into ColoredTags...");
    		coloredtags = true;
    	}
    	if(this.getConfig().getBoolean("MySQL.Enabled")) {
			this.mysql = new MySQL(this.getConfig().getString("MySQL.Username"), this.getConfig().getString("MySQL.Password"), this.getConfig().getString("MySQL.Database"), this.getConfig().getString("MySQL.Host"), this.getConfig().getString("MySQL.Port"));
			this.mysql.connect();
			this.mysql.addTable();
		} else {
			NickedPlayersFile.loadDefaultFile();
		}
		if(this.getConfig().getBoolean("Config.Auto Update Check")) {
			checkForUpdates();
		}
	}
	private void checkForUpdates() {
		log.info("Checking for updates...");
		ReadableByteChannel channel = null;
		double newVersion = 0;
		File file = new File("plugins", "BetterNick 1.8.3 - 1.12.2.jar");
		try {
			URL versionURL = new URL("https://api.spigotmc.org/legacy/update.php?resource=39633");
			BufferedReader reader = new BufferedReader(new InputStreamReader(versionURL.openStream()));
			newVersion = Double.valueOf(reader.readLine());
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(newVersion > Double.valueOf(getDescription().getVersion())) {
			log.info("Found a new version (v" + newVersion + ")");
			if(getConfig().getBoolean("Config.Auto Update Download")) {
				log.info("Starting download...");
				try {
					HttpURLConnection downloadURL = (HttpURLConnection) new URL(String.format("http://api.spiget.org/v2/resources/%s/download", 39633)).openConnection();
					downloadURL.setRequestProperty("User-Agent", "SpigetResourceUpdater/Bukkit");
					channel = Channels.newChannel(downloadURL.getInputStream());
				} catch (IOException e) {
					throw new RuntimeException("Download failed", e);
				}
				try {
					FileOutputStream output = new FileOutputStream(file);
					output.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);
					output.flush();
					output.close();
				} catch (IOException e) {
					throw new RuntimeException("File could not be saved", e);
				}
				log.info("Successfully updated plugin to v" + newVersion + ". Please restart your server");
				log.info("Checkout the newest update description to find out if you need to update your config.yml: https://www.spigotmc.org/resources/better-nick-api-1-8-3-1-12-2.39633/updates");
			} else {
				log.info("Download the update here: https://www.spigotmc.org/resources/better-nick-api-1-8-3-1-12-2.39633/");
			}
		} else {
			log.info("No new version available");
		}
	}
	private Field getField(Class<?> clazz, String name) {
		try {
			Field field = clazz.getDeclaredField(name);
			field.setAccessible(true);
			return field;
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
			return null;
		}
	}
}
