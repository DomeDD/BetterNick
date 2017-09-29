/*
 * All rights by DomeDD
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free 
 * You are NOT allowed to claim this plugin as your own
 * You are NOT allowed to publish this plugin or your modified version of this plugin
 * 
 */
package BetterNick.Versions;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.nametagedit.plugin.NametagEdit;

import BetterNick.Main;
import BetterNick.API.GameProfileBuilder;
import BetterNick.API.NickAPI;
import BetterNick.API.UUIDFetcher;
import BetterNick.API.Events.PlayerNickEvent;
import BetterNick.API.Events.PlayerSkinResetEvent;
import BetterNick.API.Events.PlayerSkinSetEvent;
import BetterNick.API.Events.PlayerUnNickEvent;
import BetterNick.Files.NickedPlayers;
import BetterNick.MySQL.MySQL_Connection;
import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.Packet;
import net.minecraft.server.v1_9_R1.PacketPlayInClientCommand;
import net.minecraft.server.v1_9_R1.PacketPlayOutChat;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_9_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_9_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_9_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_9_R1.PlayerConnection;

public class v1_9_R1 implements Listener {
	
	private static Main pl;
	@SuppressWarnings("static-access")
	public v1_9_R1(Main main) {
		this.pl = main;
	}
	private static HashMap<Player, String> DefaultPermsPrefix = new HashMap<Player, String>();
	private static HashMap<Player, Double> health = new HashMap<Player, Double>();
	private static HashMap<Player, Integer> food = new HashMap<Player, Integer>();
	private static HashMap<Player, Location> location = new HashMap<Player, Location>();
	
	public static void setNickName(Player p, String nick, String nameprefix, String nametagprefix, String tablistprefix) {
		CraftPlayer cp = (CraftPlayer) p;
		@SuppressWarnings("rawtypes")
		List blacklist = pl.getConfig().getStringList("Config.Names Black List");
		Bukkit.getPluginManager().callEvent(new PlayerNickEvent(p, nick));
		if(NickAPI.NickedPlayerExists(p)) {
			if(nick.length() <= 14) {
				if(!blacklist.contains(nick)) {
					if(!NickAPI.isNickNameUsed(nick)) {
						if(pl.nickedPlayers.contains(nameprefix + nick)) {
							pl.nickedPlayers.remove(nameprefix + nick);
							pl.nickedPlayers.add(nameprefix + nick);
						} else {
							pl.nickedPlayers.add(nameprefix + nick);
						}
						try {
							if(pl.nte) {
								pl.nameField.set(cp.getProfile(), nick);
							} else {
								pl.nameField.set(cp.getProfile(), nametagprefix + nick);
							}
						} catch (IllegalArgumentException | IllegalAccessException e) {
							e.printStackTrace();
						}
						destroy(cp);
						removeFromTablist(cp);
						Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
							@Override
							public void run() {
								spawn(cp);
								addToTablist(cp);
							}
						}, 4);
						p.setDisplayName(nameprefix + nick);
						Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
							@Override
							public void run() {
								if(pl.nte) {
									NametagEdit.getApi().setPrefix(p.getDisplayName(), nametagprefix);
								}
								p.setPlayerListName(tablistprefix + nick);
							}
						}, 2);
						if(pl.getConfig().getBoolean("Config.Use Vault")) {
							if(!DefaultPermsPrefix.containsKey(p)) {
								DefaultPermsPrefix.put(p, pl.chat.getPlayerPrefix(p));
							}
							for(World w : Bukkit.getWorlds()) {
								pl.chat.setPlayerPrefix(w.getName(), p, pl.getConfig().getString("Config.Permissions System Prefix").replace("&", "§"));
							}
						}
						if(NickAPI.MySQLEnabled()) {
							Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
								@Override
								public void run() {
									MySQL_Connection.update("UPDATE BetterNick SET NICKNAME='" + nick + "' WHERE UUID='" + p.getUniqueId() + "'");
									MySQL_Connection.update("UPDATE BetterNick SET NICKED='true' WHERE UUID='" + p.getUniqueId() + "'");
								}
							}, 2);
						} else {
							Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
								@Override
								public void run() {
									NickedPlayers.cfg.set("NickedPlayers." + p.getUniqueId() + ".NickName", nick);
									NickedPlayers.cfg.set("NickedPlayers." + p.getUniqueId() + ".Nicked", true);
									NickedPlayers.saveFile();
								}
							}, 2);
						}
					} else {
						setRandomNickName(cp, nameprefix, nametagprefix, tablistprefix);
					}
				} else {
					setRandomNickName(cp, nameprefix, nametagprefix, tablistprefix);
				}
			} else if(pl.getConfig().getBoolean("Config.Messages.Enabled")) {
				p.sendMessage(pl.getConfig().getString("Config.Messages.Nick Set Error").replace("[NAME]", nick).replace("&", "§"));
			}
		} else {
			NickAPI.createNickedPlayer(p);
			setNickName(cp, nick, nameprefix, nametagprefix, tablistprefix);
		}
	}
	public static void setRandomNickName(Player p, String nameprefix, String nametagprefix, String tablistprefix) {
		CraftPlayer cp = (CraftPlayer) p;
		@SuppressWarnings("rawtypes")
		List blacklist = pl.getConfig().getStringList("Config.Names Black List");
		@SuppressWarnings("rawtypes")
		List names = pl.getConfig().getStringList("Config.Names");
		Random r = new Random();
		int i = r.nextInt(names.size());
		Bukkit.getPluginManager().callEvent(new PlayerNickEvent(p, names.get(i).toString()));
		if(NickAPI.NickedPlayerExists(p)) {
			if(names.get(i).toString().length() <= 14) {
				if(!blacklist.contains(names.get(i).toString())) {
					if(!NickAPI.isNickNameUsed(names.get(i).toString())) {
						if(pl.nickedPlayers.contains(nameprefix + names.get(i).toString())) {
							pl.nickedPlayers.remove(nameprefix + names.get(i).toString());
							pl.nickedPlayers.add(nameprefix + names.get(i).toString());
						} else {
							pl.nickedPlayers.add(nameprefix + names.get(i).toString());
						}
						try {
							if(pl.nte) {
								pl.nameField.set(cp.getProfile(), names.get(i).toString());
							} else {
								pl.nameField.set(cp.getProfile(), nametagprefix + names.get(i).toString());
							}
						} catch (IllegalArgumentException | IllegalAccessException e) {
							e.printStackTrace();
						}
						destroy(cp);
						removeFromTablist(cp);
						Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
							@Override
							public void run() {
								addToTablist(cp);
								spawn(cp);
							}
						}, 4);
						p.setDisplayName(nameprefix + names.get(i));
						Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
							@Override
							public void run() {
								if(pl.nte) {
									NametagEdit.getApi().setPrefix(p.getDisplayName(), nametagprefix);
								}
								p.setPlayerListName(tablistprefix + names.get(i));
							}
						}, 2);
						if(pl.getConfig().getBoolean("Config.Use Vault")) {
							if(!DefaultPermsPrefix.containsKey(p)) {
								DefaultPermsPrefix.put(p, pl.chat.getPlayerPrefix(p));
							}
							for(World w : Bukkit.getWorlds()) {
								pl.chat.setPlayerPrefix(w.getName(), p, pl.getConfig().getString("Config.Permissions System Prefix").replace("&", "§"));
							}
						}
						if(NickAPI.MySQLEnabled()) {
							Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
								@Override
								public void run() {
									MySQL_Connection.update("UPDATE BetterNick SET NICKNAME='" + names.get(i).toString() + "' WHERE UUID='" + p.getUniqueId() + "'");
									MySQL_Connection.update("UPDATE BetterNick SET NICKED='true' WHERE UUID='" + p.getUniqueId() + "'");
								}
							}, 2);
						} else {
							Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
								@Override
								public void run() {
									NickedPlayers.cfg.set("NickedPlayers." + p.getUniqueId() + ".NickName", names.get(i).toString());
									NickedPlayers.cfg.set("NickedPlayers." + p.getUniqueId() + ".Nicked", true);
									NickedPlayers.saveFile();
								}
							}, 2);
						}
					} else {
						setRandomNickName(cp, nameprefix, nametagprefix, tablistprefix);
					}
				} else {
					setRandomNickName(cp, nameprefix, nametagprefix, tablistprefix);
				}
			} else {
				pl.log.warning("This Random Nickname is too long! Please edit " + names.get(i).toString() + " in the config.yml!");
				setRandomNickName(cp, nameprefix, nametagprefix, tablistprefix);
			}
		} else {
			NickAPI.createNickedPlayer(p);
			setRandomNickName(cp, nameprefix, nametagprefix, tablistprefix);
		}
	}
	public static void UnNick(Player p) {
		CraftPlayer cp = (CraftPlayer) p;
		Bukkit.getPluginManager().callEvent(new PlayerUnNickEvent(p));
		if(pl.nickedPlayers.contains(p.getDisplayName())) {
			pl.nickedPlayers.remove(p.getDisplayName());
		}
		if(NickAPI.NickedPlayerExists(p)) {
			try {
				pl.nameField.set(cp.getProfile(), NickAPI.getRealName(p));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			destroy(cp);
			removeFromTablist(cp);
			Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
				@Override
				public void run() {
					spawn(cp);
					addToTablist(cp);
				}
			}, 4);
			p.setDisplayName(NickAPI.getRealName(p));
			p.setPlayerListName(NickAPI.getRealName(p));
			if(pl.getConfig().getBoolean("Config.Use Vault")) {
				for(World w : Bukkit.getWorlds()) {
					pl.chat.setPlayerPrefix(w.getName(), p, DefaultPermsPrefix.get(p));
				}
				DefaultPermsPrefix.remove(p);
			}
			if(NickAPI.MySQLEnabled()) {
				MySQL_Connection.update("UPDATE BetterNick SET NICKNAME='" + p.getName() + "' WHERE UUID='" + p.getUniqueId() + "'");
				MySQL_Connection.update("UPDATE BetterNick SET NICKED='false' WHERE UUID='" + p.getUniqueId() + "'");
			} else {
				NickedPlayers.cfg.set("NickedPlayers." + p.getUniqueId() + ".NickName", p.getName());
				NickedPlayers.cfg.set("NickedPlayers." + p.getUniqueId() + ".Nicked", false);
				NickedPlayers.saveFile();
			}
		} else {
			NickAPI.createNickedPlayer(p);
			UnNick(cp);
		}
	}
	public static void setSkin(Player p, String pskin) {
		CraftPlayer cp = (CraftPlayer) p;
		GameProfile profile = GameProfileBuilder.fetch(UUIDFetcher.getUUID(pskin));
		Bukkit.getPluginManager().callEvent(new PlayerSkinSetEvent(p, pskin));
		Collection<Property> properties = profile.getProperties().get("textures");
		cp.getProfile().getProperties().removeAll("textures");
		cp.getProfile().getProperties().putAll("textures", properties);
		if(pl.getConfig().getBoolean("Config.Skin Self Update")) {
			destroy(cp);
			removeFromTablist(cp);
			location.put(p, p.getLocation().add(0, 1, 0));
			health.put(p, p.getHealth());
			food.put(p, p.getFoodLevel());
			p.setHealth(0);
			Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
				@Override
				public void run() {
					addToTablist(cp);
					spawn(cp);
					respawn(cp);
					p.setHealth(health.get(p));
					p.setFoodLevel(food.get(p));
					p.teleport(location.get(p));
				}
			}, 4);
		} else {
			destroy(cp);
			removeFromTablist(cp);
			Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
				@Override
				public void run() {
					addToTablist(cp);
					spawn(cp);
				}
			}, 4);
		}
	}
	public static void setRandomSkin(Player p) {
		CraftPlayer cp = (CraftPlayer) p;
		GameProfile profile = cp.getProfile();
		@SuppressWarnings("rawtypes")
		List skins = pl.getConfig().getStringList("Config.Skins");
		Random r = new Random();
		int i = r.nextInt(skins.size());
		Bukkit.getPluginManager().callEvent(new PlayerSkinSetEvent(p, skins.get(i).toString()));
		profile = GameProfileBuilder.fetch(UUIDFetcher.getUUID(skins.get(i).toString()));
		Collection<Property> properties = profile.getProperties().get("textures");
		cp.getProfile().getProperties().removeAll("textures");
		cp.getProfile().getProperties().putAll("textures", properties);
		if(pl.getConfig().getBoolean("Config.Skin Self Update")) {
			destroy(cp);
			removeFromTablist(cp);
			location.put(p, p.getLocation().add(0, 1, 0));
			health.put(p, p.getHealth());
			food.put(p, p.getFoodLevel());
			p.setHealth(0);
			Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
				@Override
				public void run() {
					addToTablist(cp);
					spawn(cp);
					respawn(cp);
					p.setHealth(health.get(p));
					p.setFoodLevel(food.get(p));
					p.teleport(location.get(p));
				}
			}, 4);
		} else {
			destroy(cp);
			removeFromTablist(cp);
			Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
				@Override
				public void run() {
					addToTablist(cp);
					spawn(cp);
				}
			}, 4);
		}
	}
	public static void resetSkin(Player p) {
		CraftPlayer cp = (CraftPlayer) p;
		GameProfile profile = cp.getProfile();
		Bukkit.getPluginManager().callEvent(new PlayerSkinResetEvent(p));
		if(NickAPI.NickedPlayerExists(p)) {
			profile = GameProfileBuilder.fetch(UUIDFetcher.getUUID(NickAPI.getNickName(p)));
			Collection<Property> properties = profile.getProperties().get("textures");
			cp.getProfile().getProperties().removeAll("textures");
			cp.getProfile().getProperties().putAll("textures", properties);
			if(pl.getConfig().getBoolean("Config.Skin Self Update")) {
				destroy(cp);
				removeFromTablist(cp);
				location.put(p, p.getLocation().add(0, 1, 0));
				health.put(p, p.getHealth());
				food.put(p, p.getFoodLevel());
				p.setHealth(0);
				Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
					@Override
					public void run() {
						addToTablist(cp);
						spawn(cp);
						respawn(cp);
						p.setHealth(health.get(p));
						p.setFoodLevel(food.get(p));
						p.teleport(location.get(p));
					}
				}, 4);
			} else {
				destroy(cp);
				removeFromTablist(cp);
				Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
					@Override
					public void run() {
						addToTablist(cp);
						spawn(cp);
					}
				}, 4);
			}
		} else {
			NickAPI.createNickedPlayer(p);
			resetSkin(cp);
		}
	}
	public static void UnNickOnLeave(Player p) {
		CraftPlayer cp = (CraftPlayer) p;
		Bukkit.getPluginManager().callEvent(new PlayerUnNickEvent(p));
		if(pl.nickedPlayers.contains(p.getDisplayName())) {
			pl.nickedPlayers.remove(p.getDisplayName());
		}
		if(NickAPI.NickedPlayerExists(p)) {
			try {
				pl.nameField.set(cp.getProfile(), NickAPI.getRealName(p));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			destroy(cp);
			removeFromTablist(cp);
			p.setDisplayName(NickAPI.getRealName(p));
			p.setPlayerListName(NickAPI.getRealName(p));
			if(pl.getConfig().getBoolean("Config.Use Vault")) {
				for(World w : Bukkit.getWorlds()) {
					pl.chat.setPlayerPrefix(w.getName(), p, DefaultPermsPrefix.get(p));
				}
				DefaultPermsPrefix.remove(p);
			}
			if(NickAPI.MySQLEnabled()) {
				MySQL_Connection.update("UPDATE BetterNick SET NICKNAME='" + p.getName() + "' WHERE UUID='" + p.getUniqueId() + "'");
				MySQL_Connection.update("UPDATE BetterNick SET NICKED='false' WHERE UUID='" + p.getUniqueId() + "'");
			} else {
				NickedPlayers.cfg.set("NickedPlayers." + p.getUniqueId() + ".NickName", p.getName());
				NickedPlayers.cfg.set("NickedPlayers." + p.getUniqueId() + ".Nicked", false);
				NickedPlayers.saveFile();
			}
		} else {
			NickAPI.createNickedPlayer(p);
			UnNickOnLeave(cp);
		}
	}
	public static void sendActionBar(Player p, String msg) {
		CraftPlayer cp = (CraftPlayer) p;
		if(p != null) {
			PlayerConnection Connection = cp.getHandle().playerConnection;
			IChatBaseComponent ABchat = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + msg + "\"}");
			PacketPlayOutChat ABpacket = new PacketPlayOutChat(ABchat, (byte)2);
			Connection.sendPacket(ABpacket); 
		} else {
			NickAPI.endActionBar(p);
		}
	}
	private static void spawn(CraftPlayer cp) {
		if(cp != null) {
			for(Player all : Bukkit.getOnlinePlayers()) {
	        	if(!all.equals(cp)) {
	        		PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(cp.getHandle());
	        		((CraftPlayer)all).getHandle().playerConnection.sendPacket(spawn);
	        	}
	        }
		}
	}
	private static void destroy(CraftPlayer cp) {
		PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(cp.getEntityId());
		sendPacket(destroy);
	}
	private static void addToTablist(CraftPlayer cp) {
		if(cp != null) {
			PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, cp.getHandle());
			sendPacket(packet);
		}
	}
	private static void removeFromTablist(CraftPlayer cp) {
		PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, cp.getHandle());
		sendPacket(packet);
	}
	private static void respawn(CraftPlayer cp) {
		cp.getHandle().playerConnection.a(new PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN));
	}
	private static void sendPacket(Packet<?> packet) {
		for(Player all : Bukkit.getOnlinePlayers()) {
			((CraftPlayer)all).getHandle().playerConnection.sendPacket(packet);
		}
	}
}
