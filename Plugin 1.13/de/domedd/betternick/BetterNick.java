/*
 * All rights by DomeDD (2018)
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free and as long as you mention me (DomeDD) 
 * You are NOT allowed to claim this plugin (BetterNick) as your own
 * You are NOT allowed to publish this plugin (BetterNick) or your modified version of this plugin (BetterNick)
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
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.mojang.authlib.GameProfile;

import de.domedd.betternick.addons.chathook.ChatHook;
import de.domedd.betternick.addons.joinquitmessage.JoinQuitMessage;
import de.domedd.betternick.addons.nickoptionsitem.NickOptionsItem;
import de.domedd.betternick.addons.placeholderapi.PlaceholderAPIHook;
import de.domedd.betternick.addons.randomnickgui.RandomNickGui;
import de.domedd.betternick.addons.supervanish.SuperVanishHook;
import de.domedd.betternick.api.Metrics;
import de.domedd.betternick.api.betternickapi.BetterNickAPI;
import de.domedd.betternick.api.betternickapi.PlayerData;
import de.domedd.betternick.commands.AutoNickCommand;
import de.domedd.betternick.commands.KeepNickCommand;
import de.domedd.betternick.commands.NickCommand;
import de.domedd.betternick.commands.NickListCommand;
import de.domedd.betternick.commands.RealNameCommand;
import de.domedd.betternick.commands.SeeNickCommand;
import de.domedd.betternick.commands.SkinCommand;
import de.domedd.betternick.commands.UnnickCommand;
import de.domedd.betternick.listeners.AutoNick;
import de.domedd.betternick.listeners.BetterNickEvents;
import de.domedd.betternick.mysqlconnection.MySQL;
import de.domedd.betternick.packets.VersionChecker;
import de.domedd.betternick.packets.VersionChecker.BukkitVersion;
import de.domedd.betternick.packets.v1_13_R1;
import de.domedd.betternick.packets.v1_13_R2;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;

public class BetterNick extends JavaPlugin implements Listener {
	
	public Logger log = this.getLogger();
	public Field nameField;
	public MySQL mysql;
	public Chat chat = null;
	public Economy econ = null;
	public boolean nte = false;
	public boolean coloredtags = false;
	public boolean cloudnet = false;
	public String prefix;
	
	@Override
	public void onEnable() {
		if(VersionChecker.getBukkitVersion() == BukkitVersion.v1_13_R1) {
			log.info("Hooking into v1_13_R1...");
			this.getServer().getPluginManager().registerEvents(new v1_13_R1(this), this);
			hookIntoAdditionalPlugins();
			loadPlugin();
		} else if(VersionChecker.getBukkitVersion() == BukkitVersion.v1_13_R2) {
			log.info("Hooking into v1_13_R2...");
			this.getServer().getPluginManager().registerEvents(new v1_13_R2(this), this);
			hookIntoAdditionalPlugins();
			loadPlugin();
		} else {
			log.warning("Your Bukkit version " + VersionChecker.getBukkitVersion().toString() + " is not supported!");
			log.warning("Use the 1.8.3 - 1.12.2 version instead!");
		}
	}
	@Override
	public void onDisable() {
		if(getConfig().getBoolean("MySQL.Enabled")) {
			mysql.disconnect();
		}
	}
	public static BetterNickAPI getApi() {
		return BetterNickAPI.getApi();
	}
	
	private void loadPlugin() {
		saveDefaultConfig();
		prefix = getConfig().getString("Messages.Prefix").replace("&", "§");
		nameField = getField(GameProfile.class, "name");
		this.getServer().getPluginManager().registerEvents(new PlayerData(this), this);
		this.getServer().getPluginManager().registerEvents(new BetterNickAPI(this), this);
		this.getServer().getPluginManager().registerEvents(new AutoNick(this), this);
		this.getServer().getPluginManager().registerEvents(new MySQL(this), this);
		if(this.getConfig().getBoolean("Addons.Nick Options Item.Enabled")) {
			this.getServer().getPluginManager().registerEvents(new NickOptionsItem(this), this);
		}
		if(this.getConfig().getBoolean("Addons.Random Nick Gui.Enabled")) {
			this.getServer().getPluginManager().registerEvents(new RandomNickGui(this), this);
		}
		if(this.getConfig().getBoolean("Addons.Join / Quit Message.Enabled")) {
			this.getServer().getPluginManager().registerEvents(new JoinQuitMessage(this), this);
		}
		if(!getConfig().getBoolean("Config.API Mode")) {
			getCommand("nick").setExecutor(new NickCommand(this));
			getCommand("skin").setExecutor(new SkinCommand(this));
			getCommand("unnick").setExecutor(new UnnickCommand(this));
			getCommand("autonick").setExecutor(new AutoNickCommand(this));
			getCommand("nicklist").setExecutor(new NickListCommand(this));
			getCommand("realname").setExecutor(new RealNameCommand(this));
			getCommand("seenick").setExecutor(new SeeNickCommand(this));
			getCommand("keepnick").setExecutor(new KeepNickCommand(this));		
			this.getServer().getPluginManager().registerEvents(new BetterNickEvents(this), this);
		} else {
			log.info("activated in API mode...");
		}
		getApi();
		if(getConfig().getBoolean("MySQL.Enabled")) {
			connectToMySQL();
		}
		if(getConfig().getBoolean("Config.Send Metrics")) {
			sendMetrics();
		}
		if(getConfig().getBoolean("Config.Auto Update Check")) {
			checkForUpdates();
		}
	}
	private void hookIntoAdditionalPlugins() {
		if(Bukkit.getPluginManager().getPlugin("NametagEdit") != null) {
			log.info("Hooking into NametagEdit...");
			nte = true;
		}
		if(Bukkit.getPluginManager().getPlugin("SuperVanish") != null || Bukkit.getPluginManager().getPlugin("PremiumVanish") != null) {
			log.info("Hooking into SuperVanish/PremiumVanish...");
			this.getServer().getPluginManager().registerEvents(new SuperVanishHook(this), this);
		}
		if(Bukkit.getPluginManager().getPlugin("Vault") != null) {
			RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
    		RegisteredServiceProvider<Economy> econProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
            if(chatProvider != null) {
            	log.info("Hooking into " + chatProvider.getProvider().getName() + " via Vault...");
                chat = chatProvider.getProvider();
                this.getServer().getPluginManager().registerEvents(new ChatHook(this), this);
            }
            if(econProvider != null) {
            	log.info("Hooking into " + econProvider.getProvider().getName() + " via Vault...");
                econ = econProvider.getProvider();
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
    	if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
    		log.info("Hooking into PlaceholderAPI...");
    		new PlaceholderAPIHook(this).register();
    	}
	}
	private void sendMetrics() {
		@SuppressWarnings("unused")
		Metrics metrics = new Metrics(this);
	}
	private void connectToMySQL() {
		mysql = new MySQL(getConfig().getString("MySQL.Username"), getConfig().getString("MySQL.Password"), getConfig().getString("MySQL.Database"), getConfig().getString("MySQL.Host"), getConfig().getString("MySQL.Port"));
		mysql.connect();
		mysql.createTables();
	}
	private void checkForUpdates() {
		log.info("Checking for updates...");
		ReadableByteChannel channel = null;
		String newVersionString = "0.0";
		double oldVersion = 0.0;
		double newVersion = 0.0;
		File file = new File("plugins", "BetterNick.zip");
		try {
			URL versionURL = new URL("https://api.spigotmc.org/legacy/update.php?resource=39633");
			BufferedReader reader = new BufferedReader(new InputStreamReader(versionURL.openStream()));
			newVersionString = reader.readLine();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(!newVersionString.equals(getDescription().getVersion())) {
			if(newVersionString.equals("0.0")) {
				log.info("Failed to check for update. Try again later");
			}
			newVersion = Double.valueOf(newVersionString.replace("-SNAPSHOT", ""));
			oldVersion = Double.valueOf(getDescription().getVersion().replace("-SNAPSHOT", ""));
			if(newVersion > oldVersion || getDescription().getVersion().contains("-SNAPSHOT") && newVersion == oldVersion) {
				log.info("Found a new version (v" + newVersionString + ")");
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
					log.info("Successfully downloaded the newest plugin version (v" + newVersionString + "). Please unpack the zip-file, pick the mathcing file for your server and restart your server");
					log.info("Checkout the newest update description to find out if you need to update your config.yml: https://www.spigotmc.org/resources/better-nick-api-1-8-3-1-12-2.39633/updates");
				} else {
					log.info("Download the update here: https://www.spigotmc.org/resources/better-nick-api-1-8-3-1-12-2.39633/");
				}
			} else {
				log.info("No new version available. You are up to date");
			}
		} else {
			log.info("No new version available. You are up to date");
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
