/*
 * All rights by DomeDD
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free 
 * You are NOT allowed to claim this plugin as your own
 * You are NOT allowed to publish this plugin or your modified version of this plugin
 * 
 */
package de.domedd.betternick.packets;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.gmail.filoghost.coloredtags.ColoredTags;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.nametagedit.plugin.NametagEdit;

import de.domedd.betternick.BetterNick;
import de.domedd.betternick.api.GameProfileBuilder;
import de.domedd.betternick.api.UUIDFetcher;
import de.domedd.betternick.api.events.PlayerNickEvent;
import de.domedd.betternick.api.events.PlayerSkinResetEvent;
import de.domedd.betternick.api.events.PlayerSkinSetEvent;
import de.domedd.betternick.api.events.PlayerUnNickEvent;
import de.domedd.betternick.api.nickedplayer.NickedPlayer;
import de.domedd.betternick.files.NickedPlayersFile;
import de.dytanic.cloudnet.bridge.CloudServer;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayInClientCommand;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_12_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_12_R1.PlayerConnection;

public class v1_12_R1 implements Listener {
	
	private static HashMap<Player, String> defaultPermsPrefix = new HashMap<Player, String>();
	private static HashMap<Player, Double> health = new HashMap<Player, Double>();
	private static HashMap<Player, Integer> food = new HashMap<Player, Integer>();
	private static HashMap<Player, Location> location = new HashMap<Player, Location>();
	private static BetterNick pl;
	
	@SuppressWarnings("static-access")
	public v1_12_R1(BetterNick main) {
		this.pl = main;
	}
	
	public static String setNickName(NickedPlayer arg0, String arg1, String arg2, String arg3, String arg4) {
		CraftPlayer cp = (CraftPlayer) arg0.getPlayer();
		List<String> blacklist = pl.getConfig().getStringList("NickNames Black List");
		if(arg0.exists()) {
			if(arg1.length() <= 14) {
				if(!blacklist.contains(arg1)) {
					if(arg0.isNicked()) {
						if(!pl.players.containsKey(arg0.getPlayer())) {
							pl.players.put(arg0.getPlayer(), arg1);
						} else {
							pl.players.remove(arg0.getPlayer());
							pl.players.put(arg0.getPlayer(), arg1);
						}
						try {
							if(pl.nte) {
								pl.nameField.set(cp.getProfile(), arg1);
							} else {
								pl.nameField.set(cp.getProfile(), arg4 + arg1);
							}
						} catch(IllegalArgumentException | IllegalAccessException e) {
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
						arg0.setDisplayName(arg2 + arg1);
						Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
							@Override
							public void run() {
								if(pl.nte) {
									NametagEdit.getApi().setPrefix(arg0.getDisplayName(), arg3);
								}
								arg0.setPlayerListName(arg4 + arg1);
							}
						}, 2);
						if(pl.chat != null) {
							if(!pl.getConfig().getBoolean("Config.Keep NickName On Quit")) {
								if(!defaultPermsPrefix.containsKey(arg0.getPlayer())) {
									defaultPermsPrefix.put(arg0.getPlayer(), pl.chat.getPlayerPrefix(arg0.getPlayer()));
								}
							} else {
								NickedPlayersFile.cfg.set("NickedPlayers." + arg0.getUniqueId() + ".DefaultPrefix", pl.chat.getPlayerPrefix(arg0.getPlayer()));
							}
							for(World w : Bukkit.getWorlds()) {
								pl.chat.setPlayerPrefix(w.getName(), arg0.getPlayer(), ((!pl.getConfig().getBoolean("Config.Keep NickName On Quit")) ? defaultPermsPrefix.get(arg0.getPlayer()) : NickedPlayersFile.cfg.getString("NickedPlayers." + arg0.getUniqueId() + ".DefaultPrefix")));
							}
						}
						if(pl.cloudnet) {
							CloudServer.getInstance().updateNameTags(arg0.getPlayer());
						}
						if(pl.coloredtags) {
							ColoredTags.updateNametag(arg0.getPlayer());
							ColoredTags.updateTab(arg0.getPlayer());
						}
						Bukkit.getPluginManager().callEvent(new PlayerNickEvent(arg0, arg1));
						return arg1;
					} else if(!arg0.isNickNameUsed(arg1)) {
						if(!pl.players.containsKey(arg0.getPlayer())) {
							pl.players.put(arg0.getPlayer(), arg1);
						} else {
							pl.players.remove(arg0.getPlayer());
							pl.players.put(arg0.getPlayer(), arg1);
						}
						try {
							if(pl.nte) {
								pl.nameField.set(cp.getProfile(), arg1);
							} else {
								pl.nameField.set(cp.getProfile(), arg4 + arg1);
							}
						} catch(IllegalArgumentException | IllegalAccessException e) {
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
						arg0.setDisplayName(arg2 + arg1);
						Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
							@Override
							public void run() {
								if(pl.nte) {
									NametagEdit.getApi().setPrefix(arg0.getDisplayName(), arg3);
								}
								arg0.setPlayerListName(arg4 + arg1);
							}
						}, 2);
						if(pl.chat != null) {
							if(!pl.getConfig().getBoolean("Config.Keep NickName On Quit")) {
								if(!defaultPermsPrefix.containsKey(arg0.getPlayer())) {
									defaultPermsPrefix.put(arg0.getPlayer(), pl.chat.getPlayerPrefix(arg0.getPlayer()));
								}
							} else {
								NickedPlayersFile.cfg.set("NickedPlayers." + arg0.getUniqueId() + ".DefaultPrefix", pl.chat.getPlayerPrefix(arg0.getPlayer()));
							}
							for(World w : Bukkit.getWorlds()) {
								pl.chat.setPlayerPrefix(w.getName(), arg0.getPlayer(), ((!pl.getConfig().getBoolean("Config.Keep NickName On Quit")) ? defaultPermsPrefix.get(arg0.getPlayer()) : NickedPlayersFile.cfg.getString("NickedPlayers." + arg0.getUniqueId() + ".DefaultPrefix")));
							}
						}
						if(pl.cloudnet) {
							CloudServer.getInstance().updateNameTags(arg0.getPlayer());
						}
						if(pl.coloredtags) {
							ColoredTags.updateNametag(arg0.getPlayer());
							ColoredTags.updateTab(arg0.getPlayer());
						}
						Bukkit.getPluginManager().callEvent(new PlayerNickEvent(arg0, arg1));
						return arg1;
					} else {
						setRandomNickName(arg0, arg1, arg2, arg3);
					}
				} else {
					setRandomNickName(arg0, arg1, arg2, arg3);
				}
			} else {
				if(pl.getConfig().getBoolean("Messages.Enabled")) {
					arg0.sendMessage(pl.getConfig().getString("Messages.Nick Set Error").replace("&", "§"));
				}
			}
		} else {
			arg0.create();
			setRandomNickName(arg0, arg1, arg2, arg3);
		}
		return null;
	}
	public static String setRandomNickName(NickedPlayer arg0, String arg1, String arg2, String arg3) {
		CraftPlayer cp = (CraftPlayer) arg0.getPlayer();
		List<String> namelist = pl.getConfig().getStringList("NickNames List");
		List<String> blacklist = pl.getConfig().getStringList("NickNames Black List");
		int i = new Random().nextInt(namelist.size());
		if(arg0.exists()) {
			if(namelist.get(i).length() <= 14) {
				if(!blacklist.contains(namelist.get(i))) {
					if(!arg0.isNickNameUsed(namelist.get(i))) {
						if(!pl.players.containsKey(arg0.getPlayer())) {
							pl.players.put(arg0.getPlayer(), namelist.get(i));
						} else {
							pl.players.remove(arg0.getPlayer());
							pl.players.put(arg0.getPlayer(), namelist.get(i));
						}
						try {
							if(pl.nte) {
								pl.nameField.set(cp.getProfile(), namelist.get(i));
							} else {
								pl.nameField.set(cp.getProfile(), arg2 + namelist.get(i));
							}
						} catch(IllegalArgumentException | IllegalAccessException e) {
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
						arg0.setDisplayName(arg1 + namelist.get(i));						Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
							@Override
							public void run() {
								if(pl.nte) {
									NametagEdit.getApi().setPrefix(arg0.getDisplayName(), arg2);
								}
								arg0.setPlayerListName(arg3 + namelist.get(i));
							}
						}, 2);
						if(pl.chat != null) {
							if(!pl.getConfig().getBoolean("Config.Keep NickName On Quit")) {
								if(!defaultPermsPrefix.containsKey(arg0.getPlayer())) {
									defaultPermsPrefix.put(arg0.getPlayer(), pl.chat.getPlayerPrefix(arg0.getPlayer()));
								}
							} else {
								NickedPlayersFile.cfg.set("NickedPlayers." + arg0.getUniqueId() + ".DefaultPrefix", pl.chat.getPlayerPrefix(arg0.getPlayer()));
							}
							for(World w : Bukkit.getWorlds()) {
								pl.chat.setPlayerPrefix(w.getName(), arg0.getPlayer(), ((!pl.getConfig().getBoolean("Config.Keep NickName On Quit")) ? defaultPermsPrefix.get(arg0.getPlayer()) : NickedPlayersFile.cfg.getString("NickedPlayers." + arg0.getUniqueId() + ".DefaultPrefix")));
							}
						}
						if(pl.cloudnet) {
							CloudServer.getInstance().updateNameTags(arg0.getPlayer());
						}
						if(pl.coloredtags) {
							ColoredTags.updateNametag(arg0.getPlayer());
							ColoredTags.updateTab(arg0.getPlayer());
						}
						Bukkit.getPluginManager().callEvent(new PlayerNickEvent(arg0, namelist.get(i)));
						return namelist.get(i);
					} else {
						setRandomNickName(arg0, arg1, arg2, arg3);
					}
				} else {
					setRandomNickName(arg0, arg1, arg2, arg3);
				}
			} else {
				if(pl.getConfig().getBoolean("Messages.Enabled")) {
					arg0.sendMessage(pl.getConfig().getString("Messages.Nick Set Error").replace("&", "§"));
				}
			}
		} else {
			arg0.create();
			setRandomNickName(arg0, arg1, arg2, arg3);
		}
		return null;
	}
	public static void unNick(NickedPlayer arg0) {
		CraftPlayer cp = (CraftPlayer) arg0.getPlayer();
		if(arg0.exists()) {
			if(pl.players.containsKey(arg0.getPlayer())) {
				pl.players.remove(arg0.getPlayer());
			}
			try {
				pl.nameField.set(cp.getProfile(), arg0.getRealName());
			} catch(IllegalArgumentException | IllegalAccessException e) {
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
			arg0.setDisplayName(arg0.getRealName());
			arg0.setPlayerListName(arg0.getRealName());
			if(pl.chat != null) {
				for(World w : Bukkit.getWorlds()) {
					pl.chat.setPlayerPrefix(w.getName(), arg0.getPlayer(), ((!pl.getConfig().getBoolean("Config.Keep NickName On Quit")) ? defaultPermsPrefix.get(arg0.getPlayer()) : NickedPlayersFile.cfg.getString("NickedPlayers." + arg0.getUniqueId() + ".DefaultPrefix")));
				}
				if(!pl.getConfig().getBoolean("Config.Keep NickName On Quit")) {
					if(defaultPermsPrefix.containsKey(arg0.getPlayer())) {
						defaultPermsPrefix.remove(arg0.getPlayer());
					}
				} else {
					NickedPlayersFile.cfg.set("NickedPlayers." + arg0.getUniqueId() + ".DefaultPrefix", null);
				}
			}
			if(pl.cloudnet) {
				CloudServer.getInstance().updateNameTags(arg0.getPlayer());
			}
			if(pl.coloredtags) {
				ColoredTags.updateNametag(arg0.getPlayer());
				ColoredTags.updateTab(arg0.getPlayer());
			}
			Bukkit.getPluginManager().callEvent(new PlayerUnNickEvent(arg0));
		} else {
			arg0.create();
			unNick(arg0);
		}
	}
	public static String setSkin(NickedPlayer arg0, String arg1) {
		CraftPlayer cp = (CraftPlayer) arg0.getPlayer();
		GameProfile profile = GameProfileBuilder.fetch(UUIDFetcher.getUUID(arg1));
		Collection<Property> properties = profile.getProperties().get("textures");
		cp.getProfile().getProperties().removeAll("textures");
		cp.getProfile().getProperties().putAll("textures", properties);
		if(pl.getConfig().getBoolean("Config.Skin Self Update")) {
			destroy(cp);
			removeFromTablist(cp);
			location.put(arg0.getPlayer(), arg0.getLocation().add(0, 1, 0));
			health.put(arg0.getPlayer(), arg0.getHealth());
			food.put(arg0.getPlayer(), arg0.getFoodLevel());
			arg0.setHealth(0);
			Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
				@Override
				public void run() {
					addToTablist(cp);
					spawn(cp);
					respawn(cp);
					arg0.setHealth(health.get(arg0.getPlayer()));
					arg0.setFoodLevel(food.get(arg0.getPlayer()));
					arg0.teleport(location.get(arg0.getPlayer()));
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
		Bukkit.getPluginManager().callEvent(new PlayerSkinSetEvent(arg0, arg1));
		return arg1;
	}
	public static String setRandomSkin(NickedPlayer arg0) {
		CraftPlayer cp = (CraftPlayer) arg0.getPlayer();
		List<String> skinlist = pl.getConfig().getStringList("Skins List");
		int i = new Random().nextInt(skinlist.size());
		GameProfile profile = GameProfileBuilder.fetch(UUIDFetcher.getUUID(skinlist.get(i)));
		Collection<Property> properties = profile.getProperties().get("textures");
		cp.getProfile().getProperties().removeAll("textures");
		cp.getProfile().getProperties().putAll("textures", properties);
		if(pl.getConfig().getBoolean("Config.Skin Self Update")) {
			destroy(cp);
			removeFromTablist(cp);
			location.put(arg0.getPlayer(), arg0.getLocation().add(0, 1, 0));
			health.put(arg0.getPlayer(), arg0.getHealth());
			food.put(arg0.getPlayer(), arg0.getFoodLevel());
			arg0.setHealth(0);
			Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
				@Override
				public void run() {
					addToTablist(cp);
					spawn(cp);
					respawn(cp);
					arg0.setHealth(health.get(arg0.getPlayer()));
					arg0.setFoodLevel(food.get(arg0.getPlayer()));
					arg0.teleport(location.get(arg0.getPlayer()));
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
		Bukkit.getPluginManager().callEvent(new PlayerSkinSetEvent(arg0, skinlist.get(i)));
		return skinlist.get(i);
	}
	public static void resetSkin(NickedPlayer arg0) {
		CraftPlayer cp = (CraftPlayer) arg0.getPlayer();
		GameProfile profile = GameProfileBuilder.fetch(UUIDFetcher.getUUID(arg0.getRealName()));
		Collection<Property> properties = profile.getProperties().get("textures");
		cp.getProfile().getProperties().removeAll("textures");
		cp.getProfile().getProperties().putAll("textures", properties);
		if(pl.getConfig().getBoolean("Config.Skin Self Update")) {
			destroy(cp);
			removeFromTablist(cp);
			location.put(arg0.getPlayer(), arg0.getLocation().add(0, 1, 0));
			health.put(arg0.getPlayer(), arg0.getHealth());
			food.put(arg0.getPlayer(), arg0.getFoodLevel());
			arg0.setHealth(0);
			Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
				@Override
				public void run() {
					addToTablist(cp);
					spawn(cp);
					respawn(cp);
					arg0.setHealth(health.get(arg0.getPlayer()));
					arg0.setFoodLevel(food.get(arg0.getPlayer()));
					arg0.teleport(location.get(arg0.getPlayer()));
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
		Bukkit.getPluginManager().callEvent(new PlayerSkinResetEvent(arg0));
	}
	public static void unNickOnLeave(NickedPlayer arg0) {
		CraftPlayer cp = (CraftPlayer) arg0.getPlayer();
		if(arg0.exists()) {
			if(pl.players.containsKey(arg0.getPlayer())) {
				pl.players.remove(arg0.getPlayer());
			}
			try {
				pl.nameField.set(cp.getProfile(), arg0.getRealName());
			} catch(IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			destroy(cp);
			removeFromTablist(cp);
			arg0.setDisplayName(arg0.getRealName());
			arg0.setPlayerListName(arg0.getRealName());
			if(pl.chat != null) {
				for(World w : Bukkit.getWorlds()) {
					pl.chat.setPlayerPrefix(w.getName(), arg0.getPlayer(), defaultPermsPrefix.get(arg0.getPlayer()));
				}
				if(defaultPermsPrefix.containsKey(arg0.getPlayer())) {
					defaultPermsPrefix.remove(arg0.getPlayer());
				}
			}
			Bukkit.getPluginManager().callEvent(new PlayerUnNickEvent(arg0));
		} else {
			arg0.create();
			unNickOnLeave(arg0);
		}
	}
	public static void sendActionBar(NickedPlayer arg0, String arg1) {
		CraftPlayer cp = (CraftPlayer) arg0.getPlayer();
		if(arg0.isOnline()) {
			PlayerConnection Connection = cp.getHandle().playerConnection;
		    PacketPlayOutTitle packet = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.ACTIONBAR, IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + arg1 + "\"}"));
		    Connection.sendPacket(packet); 
		} else {
			arg0.endActionbar();
		}
	}
	private static void spawn(CraftPlayer cp) {
		if(cp.isOnline()) {
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
		if(cp.isOnline()) {
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
