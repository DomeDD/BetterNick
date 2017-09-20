/*
 * All rights by DomeDD
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free 
 * You are NOT allowed to claim this plugin as your own
 * You are NOT allowed to publish this plugin or your modified version of this plugin
 * 
 */
package BetterNick;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.mojang.authlib.GameProfile;

import BetterNick.API.NickAPI;
import BetterNick.API.Updater.SpigetUpdate;
import BetterNick.API.Updater.UpdateCallback;
import BetterNick.API.Updater.Comparator.VersionComparator;
import BetterNick.CMD.NickCMD;
import BetterNick.CMD.NickListCMD;
import BetterNick.CMD.RealNameCMD;
import BetterNick.CMD.SkinCMD;
import BetterNick.CMD.UnNickCMD;
import BetterNick.Files.NickedPlayers;
import BetterNick.Listeners.AutoNick;
import BetterNick.Listeners.BetterNickEvents;
import BetterNick.Listeners.PlayerDeath;
import BetterNick.MySQL.MySQL_Connection;
import BetterNick.Versions.v1_10_R1;
import BetterNick.Versions.v1_11_R1;
import BetterNick.Versions.v1_12_R1;
import BetterNick.Versions.v1_8_R2;
import BetterNick.Versions.v1_8_R3;
import BetterNick.Versions.v1_9_R1;
import BetterNick.Versions.v1_9_R2;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

public class Main extends JavaPlugin implements Listener {
	
	public Field nameField;
	public Chat chat = null;
	public Permission perms = null;
	public Logger log = this.getLogger();
	public ArrayList<String> nickedPlayers = new ArrayList<>();
	
	public void onEnable() {
		checkandLoadVersions();
	}
	public void onDisable() {
		MySQL_Connection.close();
	}
	
	public static Field getField(Class<?> clazz, String name) {
		try {
			Field field = clazz.getDeclaredField(name);
			field.setAccessible(true);
			return field;
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
			return null;
		}
	}
	private void checkForUpdates() {
		if(getConfig().getBoolean("Config.Auto Update Check")) {
			log.info("Checking for updates...");
			SpigetUpdate updater = new SpigetUpdate(this, 39633);
			updater.setVersionComparator(VersionComparator.SEM_VER);
			updater.checkForUpdate(new UpdateCallback() {
				@Override
				public void updateAvailable(String newVersion, String downloadUrl, boolean hasDirectDownload) {
					if(hasDirectDownload) {
						if(getConfig().getBoolean("Config.Auto Update Download")) {
							if(updater.downloadUpdate()) {
								log.info("Successfully updated plugin to v" + newVersion + ". Unpack the .zip and copy both files into your plugins folder and restart your server");
								log.info("Checkout the newest update description to find out if you need to update your config.yml: https://www.spigotmc.org/resources/better-nick-api-1-8-3-1-12.39633/updates");
							} else {
								log.warning("Update failed. Try to download v" + newVersion + " manuelly. Reason: " + updater.getFailReason());
							}
						}
					}
				}
				@Override
				public void upToDate() {
					log.info("No new version available");
				}
			});
		}
	}
	private void loadPlugin() {
		if(getConfig().getBoolean("Config.API Mode")) {
			nameField = getField(GameProfile.class, "name");
			this.getServer().getPluginManager().registerEvents(new NickAPI(this), this);
			this.getServer().getPluginManager().registerEvents(new PlayerDeath(this), this);
			this.getServer().getPluginManager().registerEvents(new MySQL_Connection(this), this);
			if(getConfig().getBoolean("Config.Use Vault")) {
	    		RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
	    		RegisteredServiceProvider<Permission> permsProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
	            if (chatProvider != null) {
	                chat = chatProvider.getProvider();
	            }
	            if (permsProvider != null) {
	                perms = permsProvider.getProvider();
	            }
	    	}
			MySQL_Connection.setDefaultMySQL();
			saveDefaultConfig();
			if(getConfig().getBoolean("Config.MySQL")) {
				MySQL_Connection.connectMySQL();
		    	MySQL_Connection.connect();
		    	MySQL_Connection.addTable();
			} else {
				NickedPlayers.loadDefaultFile();
			}
			checkForUpdates();
	    	log.info("Plugin activated in API mode");
		} else {
			nameField = getField(GameProfile.class, "name");
			this.getServer().getPluginManager().registerEvents(new NickAPI(this), this);
			this.getServer().getPluginManager().registerEvents(new AutoNick(this), this);
			this.getServer().getPluginManager().registerEvents(new PlayerDeath(this), this);
			this.getServer().getPluginManager().registerEvents(new MySQL_Connection(this), this);
			this.getServer().getPluginManager().registerEvents(new BetterNickEvents(this), this);
			getCommand("nick").setExecutor(new NickCMD(this));
			getCommand("unnick").setExecutor(new UnNickCMD(this));
			getCommand("realname").setExecutor(new RealNameCMD(this));
			getCommand("skin").setExecutor(new SkinCMD());
			getCommand("nicklist").setExecutor(new NickListCMD(this));
	    	if(getConfig().getBoolean("Config.Use Vault")) {
	    		RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
	    		RegisteredServiceProvider<Permission> permsProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
	            if (chatProvider != null) {
	                chat = chatProvider.getProvider();
	            }
	            if (permsProvider != null) {
	                perms = permsProvider.getProvider();
	            }
	    	}
	    	MySQL_Connection.setDefaultMySQL();
			saveDefaultConfig();
			if(getConfig().getBoolean("Config.MySQL")) {
				MySQL_Connection.connectMySQL();
		    	MySQL_Connection.connect();
		    	MySQL_Connection.addTable();
			} else {
				NickedPlayers.loadDefaultFile();
			}
			checkForUpdates();
		}
	}
	private void checkandLoadVersions() {
		if(Bukkit.getVersion().contains("(MC: 1.8)") || Bukkit.getVersion().contains("(MC: 1.8.1)") || Bukkit.getVersion().contains("(MC: 1.8.2)")) {
			log.warning("Minecraft version v1_8_R1 is not supported");
			return;
		} else if(Bukkit.getVersion().contains("(MC: 1.8.3)")) {
			this.getServer().getPluginManager().registerEvents(new v1_8_R2(this), this);
			loadPlugin();
			log.info("Hooked into v1_8_R2 successfully");
		} else if(Bukkit.getVersion().contains("(MC: 1.8.4)") || Bukkit.getVersion().contains("(MC: 1.8.5)") || Bukkit.getVersion().contains("(MC: 1.8.6)") || Bukkit.getVersion().contains("(MC: 1.8.7)") || Bukkit.getVersion().contains("(MC: 1.8.8)")) {
			this.getServer().getPluginManager().registerEvents(new v1_8_R3(this), this);
			loadPlugin();
			log.info("Hooked into v1_9_R1 successfully");
		} else if(Bukkit.getVersion().contains("(MC: 1.9)") || Bukkit.getVersion().contains("(MC: 1.9.1)") || Bukkit.getVersion().contains("(MC: 1.9.2)") || Bukkit.getVersion().contains("(MC: 1.9.3)")) {
			this.getServer().getPluginManager().registerEvents(new v1_9_R1(this), this);
			loadPlugin();
			log.info("Hooked into v1_9_R2 successfully");
		} else if(Bukkit.getVersion().contains("(MC: 1.9.4)")) {
			this.getServer().getPluginManager().registerEvents(new v1_9_R2(this), this);
			loadPlugin();
			log.info("Hooked into v1_9_R3 successfully");
		} else if(Bukkit.getVersion().contains("(MC: 1.10)") || Bukkit.getVersion().contains("(MC: 1.10.1)") || Bukkit.getVersion().contains("(MC: 1.10.2)")) {
			this.getServer().getPluginManager().registerEvents(new v1_10_R1(this), this);
			loadPlugin();
			log.info("Hooked into v1_10_R1 successfully");
		} else if(Bukkit.getVersion().contains("(MC: 1.11)") || Bukkit.getVersion().contains("(MC: 1.11.1)") || Bukkit.getVersion().contains("(MC: 1.11.2)")) {
			this.getServer().getPluginManager().registerEvents(new v1_11_R1(this), this);
			loadPlugin();
			log.info("Hooked into v1_11_R1 successfully");
		} else if(Bukkit.getVersion().contains("(MC: 1.12)") || Bukkit.getVersion().contains("(MC: 1.12.1)") || Bukkit.getVersion().contains("(MC: 1.12.2)")) {
			this.getServer().getPluginManager().registerEvents(new v1_12_R1(this), this);
			loadPlugin();
			log.info("Hooked into v1_12_R1 successfully");
		} else {
			log.warning("Your minecraft version is not supported");
			return;
		}
	}
}
