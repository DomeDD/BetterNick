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
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import BetterNick.Main;
import BetterNick.API.GameProfileBuilder;
import BetterNick.API.NickAPI;
import BetterNick.API.UUIDFetcher;
import BetterNick.Files.NickedPlayers;
import BetterNick.MySQL.MySQL_Connection;
import net.minecraft.server.v1_8_R2.IChatBaseComponent;
import net.minecraft.server.v1_8_R2.Packet;
import net.minecraft.server.v1_8_R2.PacketPlayOutChat;
import net.minecraft.server.v1_8_R2.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R2.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R2.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R2.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R2.PlayerConnection;

public class v1_8_R2 implements Listener {

	private static Main pl;
	@SuppressWarnings("static-access")
	public v1_8_R2(Main main) {
		this.pl = main;
	}
	private static HashMap<Player, String> DefaultPermsPrefix = new HashMap<Player, String>();
	
	public static void setNickName(UUID p, String nick, String nameprefix, String nametagprefix, String tablistprefix) {
		CraftPlayer cp = (CraftPlayer) Bukkit.getPlayer(p);
		@SuppressWarnings("rawtypes")
		List blacklist = pl.getConfig().getStringList("Config.Names Black List");
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
							pl.nameField.set(cp.getProfile(), nametagprefix + nick);
						} catch (IllegalArgumentException | IllegalAccessException e) {
							e.printStackTrace();
						}
						destroy(p);
						removeFromTablist(p);
						Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
							@Override
							public void run() {
								spawn(p);
								addToTablist(p);
							}
						}, 4);
						Bukkit.getPlayer(p).setDisplayName(nameprefix + nick);
						Bukkit.getPlayer(p).setPlayerListName(tablistprefix + nick);
						if(pl.getConfig().getBoolean("Config.Use Vault")) {
							if(!DefaultPermsPrefix.containsKey(Bukkit.getPlayer(p))) {
								DefaultPermsPrefix.put(Bukkit.getPlayer(p), pl.chat.getPlayerPrefix(Bukkit.getPlayer(p)));
							}
							for(World w : Bukkit.getWorlds()) {
								pl.chat.setPlayerPrefix(w.getName(), Bukkit.getPlayer(p), pl.getConfig().getString("Config.Permissions System Prefix").replace("&", "§"));
							}
						}
						if(NickAPI.MySQLEnabled()) {
							MySQL_Connection.update("UPDATE BetterNick SET NICKNAME='" + nick + "' WHERE UUID='" + p + "'");
							MySQL_Connection.update("UPDATE BetterNick SET NICKED='true' WHERE UUID='" + p + "'");
						} else {
							NickedPlayers.cfg.set("NickedPlayers." + p + ".NickName", nick);
							NickedPlayers.cfg.set("NickedPlayers." + p + ".Nicked", true);
							NickedPlayers.saveFile();
						}
						Bukkit.getPlayer(p).sendMessage(pl.getConfig().getString("Config.Messages.Nick Name Set").replace("[NAME]", nick).replace("&", "§"));
					} else {
						setRandomNickName(p, nameprefix, nametagprefix, tablistprefix);
					}
				} else {
					setRandomNickName(p, nameprefix, nametagprefix, tablistprefix);
				}
			} else {
				Bukkit.getPlayer(p).sendMessage(pl.getConfig().getString("Config.Messages.Nick Set Error").replace("[NAME]", nick).replace("&", "§"));
			}
		} else {
			NickAPI.createNickedPlayer(p);
			setNickName(p, nick, nameprefix, nametagprefix, tablistprefix);
		}
	}
	public static void setRandomNickName(UUID p, String nameprefix, String nametagprefix, String tablistprefix) {
		CraftPlayer cp = (CraftPlayer) Bukkit.getPlayer(p);
		@SuppressWarnings("rawtypes")
		List blacklist = pl.getConfig().getStringList("Config.Names Black List");
		@SuppressWarnings("rawtypes")
		List names = pl.getConfig().getStringList("Config.Names");
		Random r = new Random();
		int i = r.nextInt(names.size());
		if(NickAPI.NickedPlayerExists(p)) {
			if(names.get(i).toString().length() <= 14) {
				if(!blacklist.contains(names.get(i))) {
					if(!NickAPI.isNickNameUsed(names.get(i).toString())) {
						if(pl.nickedPlayers.contains(nameprefix + names.get(i).toString())) {
							pl.nickedPlayers.remove(nameprefix + names.get(i).toString());
							pl.nickedPlayers.add(nameprefix + names.get(i).toString());
						} else {
							pl.nickedPlayers.add(nameprefix + names.get(i).toString());
						}
						try {
							pl.nameField.set(cp.getProfile(), nametagprefix + names.get(i).toString());
						} catch (IllegalArgumentException | IllegalAccessException e) {
							e.printStackTrace();
						}
						destroy(p);
						removeFromTablist(p);
						Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
							@Override
							public void run() {
								addToTablist(p);
								spawn(p);
							}
						}, 4);
						Bukkit.getPlayer(p).setDisplayName(nameprefix + names.get(i));
						Bukkit.getPlayer(p).setPlayerListName(tablistprefix + names.get(i));
						if(pl.getConfig().getBoolean("Config.Use Vault")) {
							if(!DefaultPermsPrefix.containsKey(Bukkit.getPlayer(p))) {
								DefaultPermsPrefix.put(Bukkit.getPlayer(p), pl.chat.getPlayerPrefix(Bukkit.getPlayer(p)));
							}
							for(World w : Bukkit.getWorlds()) {
								pl.chat.setPlayerPrefix(w.getName(), Bukkit.getPlayer(p), pl.getConfig().getString("Config.Permissions System Prefix").replace("&", "§"));
							}
						}
						if(NickAPI.MySQLEnabled()) {
							MySQL_Connection.update("UPDATE BetterNick SET NICKNAME='" + names.get(i) + "' WHERE UUID='" + p + "'");
							MySQL_Connection.update("UPDATE BetterNick SET NICKED='true' WHERE UUID='" + p + "'");
						} else {
							NickedPlayers.cfg.set("NickedPlayers." + p + ".NickName", names.get(i));
							NickedPlayers.cfg.set("NickedPlayers." + p + ".Nicked", true);
							NickedPlayers.saveFile();
						}
						Bukkit.getPlayer(p).sendMessage(pl.getConfig().getString("Config.Messages.Nick Name Set").replace("[NAME]", names.get(i).toString()).replace("&", "§"));
					} else {
						setRandomNickName(p, nameprefix, nametagprefix, tablistprefix);
					}
				} else {
					setRandomNickName(p, nameprefix, nametagprefix, tablistprefix);
				}
			} else {
				Bukkit.getConsoleSender().sendMessage("§eBetter Nick §6Debug§7: §4This Random Nickname is to long. Please edit §c" + names.get(i).toString() + "§4!");
				setRandomNickName(p, nameprefix, nametagprefix, tablistprefix);
			}
		} else {
			NickAPI.createNickedPlayer(p);
			setRandomNickName(p, nameprefix, nametagprefix, tablistprefix);
		}
	}
	public static void UnNick(UUID p) {
		if(pl.nickedPlayers.contains(Bukkit.getPlayer(p).getDisplayName())) {
			pl.nickedPlayers.remove(Bukkit.getPlayer(p).getDisplayName());
		}
		CraftPlayer cp = (CraftPlayer) Bukkit.getPlayer(p);
		if(NickAPI.NickedPlayerExists(p)) {
			try {
				pl.nameField.set(cp.getProfile(), NickAPI.getRealName(p));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			destroy(p);
			removeFromTablist(p);
			Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
				@Override
				public void run() {
					addToTablist(p);
					spawn(p);
				}
			}, 4);
			Bukkit.getPlayer(p).setDisplayName(NickAPI.getRealName(p));
			Bukkit.getPlayer(p).setPlayerListName(NickAPI.getRealName(p));
			if(pl.getConfig().getBoolean("Config.Use Vault")) {
				for(World w : Bukkit.getWorlds()) {
					pl.chat.setPlayerPrefix(w.getName(), Bukkit.getPlayer(p), DefaultPermsPrefix.get(Bukkit.getPlayer(p)));
				}
				DefaultPermsPrefix.remove(Bukkit.getPlayer(p));
			}
			if(NickAPI.MySQLEnabled()) {
				MySQL_Connection.update("UPDATE BetterNick SET NICKNAME='" + Bukkit.getPlayer(p).getName() + "' WHERE UUID='" + p + "'");
				MySQL_Connection.update("UPDATE BetterNick SET NICKED='false' WHERE UUID='" + p + "'");
			} else {
				NickedPlayers.cfg.set("NickedPlayers." + p + ".NickName", Bukkit.getPlayer(p).getName());
				NickedPlayers.cfg.set("NickedPlayers." + p + ".Nicked", false);
				NickedPlayers.saveFile();
			}
			Bukkit.getPlayer(p).sendMessage(pl.getConfig().getString("Config.Messages.Nick Name Removed").replace("&", "§"));
		} else {
			NickAPI.createNickedPlayer(p);
			UnNick(p);
		}
	}
	public static void setSkin(UUID p, String pskin) {
		CraftPlayer cp = (CraftPlayer) Bukkit.getPlayer(p);
		GameProfile profile = cp.getProfile();
		profile = GameProfileBuilder.fetch(UUIDFetcher.getUUID(pskin));
		Collection<Property> properties = profile.getProperties().get("textures");
		cp.getProfile().getProperties().removeAll("textures");
		cp.getProfile().getProperties().putAll("textures", properties);
		Bukkit.getPlayer(p).sendMessage(pl.getConfig().getString("Config.Messages.Skin Set").replace("&", "§"));
		destroy(p);
		removeFromTablist(p);
		Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
			@Override
			public void run() {
				addToTablist(p);
				spawn(p);
			}
		}, 4);
	}
	public static void setRandomSkin(UUID p) {
		CraftPlayer cp = (CraftPlayer) Bukkit.getPlayer(p);
		GameProfile profile = cp.getProfile();
		@SuppressWarnings("rawtypes")
		List skins = pl.getConfig().getStringList("Config.Skins");
		Random r = new Random();
		int i = r.nextInt(skins.size());
		profile = GameProfileBuilder.fetch(UUIDFetcher.getUUID(skins.get(i).toString()));
		Collection<Property> properties = profile.getProperties().get("textures");
		cp.getProfile().getProperties().removeAll("textures");
		cp.getProfile().getProperties().putAll("textures", properties);
		Bukkit.getPlayer(p).sendMessage(pl.getConfig().getString("Config.Messages.Skin Set").replace("&", "§"));
		destroy(p);
		removeFromTablist(p);
		Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
			@Override
			public void run() {
				addToTablist(p);
				spawn(p);
			}
		}, 4);
	}
	public static void resetSkin(UUID p) {
		CraftPlayer cp = (CraftPlayer) Bukkit.getPlayer(p);
		GameProfile profile = cp.getProfile();
		if(NickAPI.NickedPlayerExists(p)) {
			profile = GameProfileBuilder.fetch(UUIDFetcher.getUUID(NickAPI.getNickName(p)));
			Collection<Property> properties = profile.getProperties().get("textures");
			cp.getProfile().getProperties().removeAll("textures");
			cp.getProfile().getProperties().putAll("textures", properties);
			Bukkit.getPlayer(p).sendMessage(pl.getConfig().getString("Config.Messages.Skin Set").replace("&", "§"));
			destroy(p);
			removeFromTablist(p);
			Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
				@Override
				public void run() {
					addToTablist(p);
					spawn(p);
				}
			}, 4);
		} else {
			NickAPI.createNickedPlayer(p);
			resetSkin(p);
		}
	}
	public static void sendActionBar(UUID p, String msg) {
	    PlayerConnection Connection = ((CraftPlayer)Bukkit.getPlayer(p)).getHandle().playerConnection;
	    IChatBaseComponent ABchat = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + msg + "\"}");
	    PacketPlayOutChat ABpacket = new PacketPlayOutChat(ABchat, (byte)2);
	    Connection.sendPacket(ABpacket);
	}
	public static void spawn(UUID p) {
		CraftPlayer cp = ((CraftPlayer)Bukkit.getPlayer(p));
		for(Player all : Bukkit.getOnlinePlayers()) {
        	if(!all.equals(Bukkit.getPlayer(p))) {
        		PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(cp.getHandle());
        		((CraftPlayer)all).getHandle().playerConnection.sendPacket(spawn);
        	}
        }
	}
	public static void destroy(UUID p) {
		CraftPlayer cp = ((CraftPlayer)Bukkit.getPlayer(p));
		PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(cp.getEntityId());
		sendPacket(destroy);
	}
	public static void addToTablist(UUID p) {
		CraftPlayer cp = ((CraftPlayer)Bukkit.getPlayer(p));
		if(Bukkit.getPlayer(p) != null) {
			PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, cp.getHandle());
			sendPacket(packet);
		}
	}
	public static void removeFromTablist(UUID p) {
		CraftPlayer cp = ((CraftPlayer)Bukkit.getPlayer(p));
		PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, cp.getHandle());
		sendPacket(packet);
	}
	public static void sendPacket(Packet<?> packet) {
		for(Player all : Bukkit.getOnlinePlayers()) {
			((CraftPlayer)all).getHandle().playerConnection.sendPacket(packet);
		}
	}
}
