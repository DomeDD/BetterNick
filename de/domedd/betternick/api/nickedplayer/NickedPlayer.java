/*
 * All rights by DomeDD
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free 
 * You are NOT allowed to claim this plugin as your own
 * You are NOT allowed to publish this plugin or your modified version of this plugin
 * 
 */
package de.domedd.betternick.api.nickedplayer;

import java.net.InetSocketAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Achievement;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.GameMode;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.InventoryView.Property;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import de.domedd.betternick.BetterNick;
import de.domedd.betternick.files.NickedPlayersFile;
import de.domedd.betternick.packets.VersionChecker;
import de.domedd.betternick.packets.v1_10_R1;
import de.domedd.betternick.packets.v1_11_R1;
import de.domedd.betternick.packets.v1_12_R1;
import de.domedd.betternick.packets.v1_8_R2;
import de.domedd.betternick.packets.v1_8_R3;
import de.domedd.betternick.packets.v1_9_R1;
import de.domedd.betternick.packets.v1_9_R2;

public class NickedPlayer implements Player, Listener {
	
	
	private static HashMap<Player, Integer> taskID = new HashMap<Player, Integer>();
	private Player p;
	private String dpnPrefix;
	private String ntgPrefix;
	private String pllPrefix;
	private String nickname;
	private String skin;
	private static BetterNick pl;
	
	@SuppressWarnings("static-access")
	public NickedPlayer(BetterNick main) {
		this.pl = main;
	}
	
	public NickedPlayer(Player arg0) {
		this.p = arg0;
	}
	
	public NickedPlayer setNickName(String arg0, String arg1, String arg2, String arg3) {
		this.dpnPrefix = arg1;
		this.ntgPrefix = arg2;
		this.pllPrefix = arg3;
		switch(VersionChecker.getBukkitVersion()) {
		case v1_8_R1:
			break;
		case v1_8_R2:
			this.nickname = v1_8_R2.setNickName(getNickedPlayer(), arg0, arg1, arg2, arg3);
			break;
		case v1_8_R3:
			this.nickname = v1_8_R3.setNickName(getNickedPlayer(), arg0, arg1, arg2, arg3);
			break;
		case v1_9_R1:
			this.nickname = v1_9_R1.setNickName(getNickedPlayer(), arg0, arg1, arg2, arg3);
			break;
		case v1_9_R2:
			this.nickname = v1_9_R2.setNickName(getNickedPlayer(), arg0, arg1, arg2, arg3);
			break;	
		case v1_10_R1:
			this.nickname = v1_10_R1.setNickName(getNickedPlayer(), arg0, arg1, arg2, arg3);
			break;
		case v1_11_R1:
			this.nickname = v1_11_R1.setNickName(getNickedPlayer(), arg0, arg1, arg2, arg3);
			break;
		case v1_12_R1:
			this.nickname = v1_12_R1.setNickName(getNickedPlayer(), arg0, arg1, arg2, arg3);
			break;
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
			@Override
			public void run() {
				if(pl.getConfig().getBoolean("MySQL.Enabled")) {
					pl.mysql.update("UPDATE BetterNick SET NICKNAME='" + arg0 + "' WHERE UUID='" + p.getUniqueId() + "'");
					pl.mysql.update("UPDATE BetterNick SET NICKED='true' WHERE UUID='" + p.getUniqueId() + "'");
				} else {
					NickedPlayersFile.cfg.set("NickedPlayers." + p.getUniqueId() + ".NickName", arg0);
					NickedPlayersFile.cfg.set("NickedPlayers." + p.getUniqueId() + ".Nicked", true);
					NickedPlayersFile.save();
				}
			}
		}, 2);
		return this;
	}
	public NickedPlayer setRandomNickName(String arg0, String arg1, String arg2) {
		this.dpnPrefix = arg0;
		this.ntgPrefix = arg1;
		this.pllPrefix = arg2;
		switch(VersionChecker.getBukkitVersion()) {
		case v1_8_R1:
			break;
		case v1_8_R2:
			this.nickname = v1_8_R2.setRandomNickName(getNickedPlayer(), arg0, arg1, arg2);
			break;
		case v1_8_R3:
			this.nickname = v1_8_R3.setRandomNickName(getNickedPlayer(), arg0, arg1, arg2);
			break;
		case v1_9_R1:
			this.nickname = v1_9_R1.setRandomNickName(getNickedPlayer(), arg0, arg1, arg2);
			break;
		case v1_9_R2:
			this.nickname = v1_9_R2.setRandomNickName(getNickedPlayer(), arg0, arg1, arg2);
			break;	
		case v1_10_R1:
			this.nickname = v1_10_R1.setRandomNickName(getNickedPlayer(), arg0, arg1, arg2);
			break;
		case v1_11_R1:
			this.nickname = v1_11_R1.setRandomNickName(getNickedPlayer(), arg0, arg1, arg2);
			break;
		case v1_12_R1:
			this.nickname = v1_12_R1.setRandomNickName(getNickedPlayer(), arg0, arg1, arg2);
			break;
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
			@Override
			public void run() {
				if(pl.getConfig().getBoolean("MySQL.Enabled")) {
					pl.mysql.update("UPDATE BetterNick SET NICKNAME='" + nickname + "' WHERE UUID='" + p.getUniqueId() + "'");
					pl.mysql.update("UPDATE BetterNick SET NICKED='true' WHERE UUID='" + p.getUniqueId() + "'");
				} else {
					NickedPlayersFile.cfg.set("NickedPlayers." + p.getUniqueId() + ".NickName", nickname);
					NickedPlayersFile.cfg.set("NickedPlayers." + p.getUniqueId() + ".Nicked", true);
					NickedPlayersFile.save();
				}
			}
		}, 4);
		return this;
	}
	public NickedPlayer setSkin(String arg0) {
		switch(VersionChecker.getBukkitVersion()) {
		case v1_8_R1:
			break;
		case v1_8_R2:
			this.skin = v1_8_R2.setSkin(getNickedPlayer(), arg0);
			break;
		case v1_8_R3:
			this.skin = v1_8_R3.setSkin(getNickedPlayer(), arg0);
			break;
		case v1_9_R1:
			this.skin = v1_9_R1.setSkin(getNickedPlayer(), arg0);
			break;
		case v1_9_R2:
			this.skin = v1_9_R2.setSkin(getNickedPlayer(), arg0);
			break;	
		case v1_10_R1:
			this.skin = v1_10_R1.setSkin(getNickedPlayer(), arg0);
			break;
		case v1_11_R1:
			this.skin = v1_11_R1.setSkin(getNickedPlayer(), arg0);
			break;
		case v1_12_R1:
			this.skin = v1_12_R1.setSkin(getNickedPlayer(), arg0);
			break;
		}
		return this;
	}
	public NickedPlayer setRandomSkin() {
		switch(VersionChecker.getBukkitVersion()) {
		case v1_8_R1:
			break;
		case v1_8_R2:
			this.skin = v1_8_R2.setRandomSkin(getNickedPlayer());
			break;
		case v1_8_R3:
			this.skin = v1_8_R3.setRandomSkin(getNickedPlayer());
			break;
		case v1_9_R1:
			this.skin = v1_9_R1.setRandomSkin(getNickedPlayer());
			break;
		case v1_9_R2:
			this.skin = v1_9_R2.setRandomSkin(getNickedPlayer());
			break;	
		case v1_10_R1:
			this.skin = v1_10_R1.setRandomSkin(getNickedPlayer());
			break;
		case v1_11_R1:
			this.skin = v1_11_R1.setRandomSkin(getNickedPlayer());
			break;
		case v1_12_R1:
			this.skin = v1_12_R1.setRandomSkin(getNickedPlayer());
			break;
		}
		return this;
	}
	public NickedPlayer unNick() {
		switch(VersionChecker.getBukkitVersion()) {
		case v1_8_R1:
			break;
		case v1_8_R2:
			v1_8_R2.unNick(getNickedPlayer());
			break;
		case v1_8_R3:
			v1_8_R3.unNick(getNickedPlayer());
			break;
		case v1_9_R1:
			v1_9_R1.unNick(getNickedPlayer());
			break;
		case v1_9_R2:
			v1_9_R2.unNick(getNickedPlayer());
			break;	
		case v1_10_R1:
			v1_10_R1.unNick(getNickedPlayer());
			break;
		case v1_11_R1:
			v1_11_R1.unNick(getNickedPlayer());
			break;
		case v1_12_R1:
			v1_12_R1.unNick(getNickedPlayer());
			break;
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
			@Override
			public void run() {
				if(pl.getConfig().getBoolean("MySQL.Enabled")) {
					pl.mysql.update("UPDATE BetterNick SET NICKNAME='" + p.getName() + "' WHERE UUID='" + p.getUniqueId() + "'");
					pl.mysql.update("UPDATE BetterNick SET NICKED='false' WHERE UUID='" + p.getUniqueId() + "'");
				} else {
					NickedPlayersFile.cfg.set("NickedPlayers." + p.getUniqueId() + ".NickName", p.getName());
					NickedPlayersFile.cfg.set("NickedPlayers." + p.getUniqueId() + ".Nicked", false);
					NickedPlayersFile.save();
				}
			}
		}, 4);
		return this;
	}
	public NickedPlayer resetSkin() {
		switch(VersionChecker.getBukkitVersion()) {
		case v1_8_R1:
			break;
		case v1_8_R2:
			v1_8_R2.resetSkin(getNickedPlayer());
			break;
		case v1_8_R3:
			v1_8_R3.resetSkin(getNickedPlayer());
			break;
		case v1_9_R1:
			v1_9_R1.resetSkin(getNickedPlayer());
			break;
		case v1_9_R2:
			v1_9_R2.resetSkin(getNickedPlayer());
			break;	
		case v1_10_R1:
			v1_10_R1.resetSkin(getNickedPlayer());
			break;
		case v1_11_R1:
			v1_11_R1.resetSkin(getNickedPlayer());
			break;
		case v1_12_R1:
			v1_12_R1.resetSkin(getNickedPlayer());
			break;
		}
		return this;
	}
	public NickedPlayer unNickOnLeave() {
		switch(VersionChecker.getBukkitVersion()) {
		case v1_8_R1:
			break;
		case v1_8_R2:
			v1_8_R2.unNickOnLeave(getNickedPlayer());
			break;
		case v1_8_R3:
			v1_8_R3.unNickOnLeave(getNickedPlayer());
			break;
		case v1_9_R1:
			v1_9_R1.unNickOnLeave(getNickedPlayer());
			break;
		case v1_9_R2:
			v1_9_R2.unNickOnLeave(getNickedPlayer());
			break;	
		case v1_10_R1:
			v1_10_R1.unNickOnLeave(getNickedPlayer());
			break;
		case v1_11_R1:
			v1_11_R1.unNickOnLeave(getNickedPlayer());
			break;
		case v1_12_R1:
			v1_12_R1.unNickOnLeave(getNickedPlayer());
			break;
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
			@Override
			public void run() {
				if(pl.getConfig().getBoolean("MySQL.Enabled")) {
					pl.mysql.update("UPDATE BetterNick SET NICKNAME='" + p.getName() + "' WHERE UUID='" + p.getUniqueId() + "'");
					pl.mysql.update("UPDATE BetterNick SET NICKED='false' WHERE UUID='" + p.getUniqueId() + "'");
				} else {
					NickedPlayersFile.cfg.set("NickedPlayers." + p.getUniqueId() + ".NickName", p.getName());
					NickedPlayersFile.cfg.set("NickedPlayers." + p.getUniqueId() + ".Nicked", false);
					NickedPlayersFile.save();
				}
			}
		}, 4);
		return this;
	}
	public NickedPlayer setAutoNick(boolean arg0) {
		if(pl.getConfig().getBoolean("MySQL.Enabled")) {
			pl.mysql.update("UPDATE BetterNick SET AUTONICK='" + arg0 + "' WHERE UUID='" + this.p.getUniqueId() + "'");
		} else {
			NickedPlayersFile.cfg.set("NickedPlayers." + this.p.getUniqueId() + ".AutoNick", arg0);
			NickedPlayersFile.save();
		}
		return this;
	}
	public boolean hasAutoNick() {
		if(pl.getConfig().getBoolean("MySQL.Enabled")) {
			try {
				ResultSet rs = pl.mysql.result("SELECT AUTONICK FROM BetterNick WHERE UUID='" + this.p.getUniqueId() + "'");
				if(rs.next()) {
					return rs.getBoolean("AUTONICK");
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		} else {
			return NickedPlayersFile.cfg.getBoolean("NickedPlayers." + this.p.getUniqueId() + ".AutoNick");
		}
		return false;
	}
	public boolean isNicked() {
		if(pl.getConfig().getBoolean("MySQL.Enabled")) {
			try {
				ResultSet rs = pl.mysql.result("SELECT NICKED FROM BetterNick WHERE UUID='" + this.p.getUniqueId() + "'");
				if(rs.next()) {
					return rs.getBoolean("NICKED");
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		} else {
			return NickedPlayersFile.cfg.getBoolean("NickedPlayers." + this.p.getUniqueId() + ".Nicked");
		}
		return false;
	}
	public boolean isNickNameUsed(String arg0) {
		if(pl.getConfig().getBoolean("MySQL.Enabled")) {
			try {
				ResultSet rs = pl.mysql.result("SELECT NICKNAME FROM BetterNick WHERE NICKNAME='" + arg0 + "'");
				if(rs.next()) {
					return true;
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		} else {
			for(Player all : Bukkit.getOnlinePlayers()) {
				if(NickedPlayersFile.cfg.getString("NickedPlayers." + all.getUniqueId() + ".NickName") != null && NickedPlayersFile.cfg.getString("NickedPlayers." + all.getUniqueId() + ".NickName").equals(arg0)) {
					return true;
				}
			}
		}
		return false;
	}
	public NickedPlayer sendActionbar(String arg0) {
		int tid = 0;
		tid = Bukkit.getScheduler().scheduleSyncRepeatingTask(pl, new Runnable() {
			@Override
			public void run() {
				switch(VersionChecker.getBukkitVersion()) {
				case v1_8_R1:
					break;
				case v1_8_R2:
					v1_8_R2.sendActionBar(getNickedPlayer(), arg0);
					break;
				case v1_8_R3:
					v1_8_R3.sendActionBar(getNickedPlayer(), arg0);
					break;
				case v1_9_R1:
					v1_9_R1.sendActionBar(getNickedPlayer(), arg0);
					break;
				case v1_9_R2:
					v1_9_R2.sendActionBar(getNickedPlayer(), arg0);
					break;	
				case v1_10_R1:
					v1_10_R1.sendActionBar(getNickedPlayer(), arg0);
					break;
				case v1_11_R1:
					v1_11_R1.sendActionBar(getNickedPlayer(), arg0);
					break;
				case v1_12_R1:
					v1_12_R1.sendActionBar(getNickedPlayer(), arg0);
					break;
				}
			}
		}, 0, 40);
		taskID.put(this.p, tid);
		return this;
	}
	public NickedPlayer endActionbar() {
		if(taskID.containsKey(this.p)) {
			int tid = taskID.get(this.p);
			pl.getServer().getScheduler().cancelTask(tid);
			taskID.remove(this.p);
		}
		return this;
	}
	public NickedPlayer create() {
		if(pl.getConfig().getBoolean("MySQL.Enabled")) {
			pl.mysql.update("INSERT INTO BetterNick (UUID, NAME, NICKNAME, NICKED, AUTONICK) VALUES ('" + this.p.getUniqueId() + "', '" + this.p.getName() + "', '" + this.p.getName() + "', 'false', 'false');");
		} else {
			NickedPlayersFile.cfg.set("NickedPlayers." + this.p.getUniqueId() + ".Name", this.p.getName());
			NickedPlayersFile.cfg.set("NickedPlayers." + this.p.getUniqueId() + ".NickName", this.p.getName());
			NickedPlayersFile.cfg.set("NickedPlayers." + this.p.getUniqueId() + ".Nicked", false);
			NickedPlayersFile.cfg.set("NickedPlayers." + this.p.getUniqueId() + ".AutoNick", false);
			NickedPlayersFile.save();
		}
		return this;
	}
	public boolean exists() {
		if(pl.getConfig().getBoolean("MySQL.Enabled")) {
			try {
		    	ResultSet rs = pl.mysql.result("SELECT NAME FROM BetterNick WHERE UUID='" + this.p.getUniqueId() + "'");
		    	if(rs.next()) {
		    		return true;
		    	}
		    } catch (SQLException e) {
		    	e.printStackTrace();
		    }
		} else {
			if(NickedPlayersFile.cfg.contains("NickedPlayers." + this.p.getUniqueId())) {
				return true;
			}
		}
		return false;
	}
	public NickedPlayer getNickedPlayer() {
		return this;
	}
	public String getNickName() {
		if(pl.getConfig().getBoolean("MySQL.Enabled")) {
			try {
		    	ResultSet rs = pl.mysql.result("SELECT NICKNAME FROM BetterNick WHERE UUID='" + this.p.getUniqueId() + "'");
		    	if(rs.next() && rs.getString("NICKNAME") == null);
				return rs.getString("NICKNAME");
		    } catch (SQLException e) {
		    	e.printStackTrace();
		    }
		} else {
			return NickedPlayersFile.cfg.getString("NickedPlayers." + this.p.getUniqueId() + ".NickName");
		}
		return null;
	}
	public String getRealName() {
		if(pl.getConfig().getBoolean("MySQL.Enabled")) {
			try {
		    	ResultSet rs = pl.mysql.result("SELECT NAME FROM BetterNick WHERE UUID='" + this.p.getUniqueId() + "'");
		    	if(rs.next() && rs.getString("NAME") == null);
				return rs.getString("NAME");
		    } catch (SQLException e) {
		    	e.printStackTrace();
		    }
		} else {
			return NickedPlayersFile.cfg.getString("NickedPlayers." + this.p.getUniqueId() + ".Name");
		}
		return null;
	}
	public String getSkin() {
		return this.skin;
	}
	public String getDisplayNamePrefix() {
		return this.dpnPrefix;
	}
	public String getNameTagPrefix() {
		return this.ntgPrefix;
	}
	public String getPlayerListPrefix() {
		return this.pllPrefix;
	}
	
	/////////////////////////////////////
	
	@Override
	public void closeInventory() {
		this.p.closeInventory();
	}
	@Override
	public Inventory getEnderChest() {
		return this.p.getEnderChest();
	}
	@Override
	public int getExpToLevel() {
		return this.p.getExpToLevel();
	}
	@Override
	public GameMode getGameMode() {
		return this.p.getGameMode();
	}
	@Override
	public PlayerInventory getInventory() {
		return this.p.getInventory();
	}
	@Override
	public ItemStack getItemInHand() {
		return this.p.getItemInHand();
	}
	@Override
	public ItemStack getItemOnCursor() {
		return this.p.getItemOnCursor();
	}
	@Override
	public String getName() {
		return this.p.getName();
	}
	@Override
	public InventoryView getOpenInventory() {
		return this.p.getOpenInventory();
	}
	@Override
	public int getSleepTicks() {
		return this.p.getSleepTicks();
	}
	@Override
	public boolean isBlocking() {
		return this.p.isBlocking();
	}
	@Override
	public boolean isSleeping() {
		return this.p.isSleeping();
	}
	@Override
	public InventoryView openEnchanting(Location arg0, boolean arg1) {
		return this.p.openEnchanting(arg0, arg1);
	}
	@Override
	public InventoryView openInventory(Inventory arg0) {
		return this.p.openInventory(arg0);
	}
	@Override
	public void openInventory(InventoryView arg0) {
		this.p.openInventory(arg0);
	}
	@Override
	public InventoryView openWorkbench(Location arg0, boolean arg1) {
		return this.p.openWorkbench(arg0, arg1);
	}
	@Override
	public void setGameMode(GameMode arg0) {
		this.p.setGameMode(arg0);
	}
	@Override
	public void setItemInHand(ItemStack arg0) {
		this.p.setItemInHand(arg0);
	}
	@Override
	public void setItemOnCursor(ItemStack arg0) {
		this.p.setItemOnCursor(arg0);
	}
	@Override
	public boolean setWindowProperty(Property arg0, int arg1) {
		return this.p.setWindowProperty(arg0, arg1);
	}
	@Override
	public boolean addPotionEffect(PotionEffect arg0) {
		return this.p.addPotionEffect(arg0);
	}
	@Override
	public boolean addPotionEffect(PotionEffect arg0, boolean arg1) {
		return this.p.addPotionEffect(arg0, arg1);
	}

	@Override
	public boolean addPotionEffects(Collection<PotionEffect> arg0) {
		return this.p.addPotionEffects(arg0);
	}
	@Override
	public Collection<PotionEffect> getActivePotionEffects() {
		return this.p.getActivePotionEffects();
	}
	@Override
	public boolean getCanPickupItems() {
		return this.p.getCanPickupItems();
	}
	@Override
	public EntityEquipment getEquipment() {
		return this.p.getEquipment();
	}
	@Override
	public double getEyeHeight() {
		return this.p.getEyeHeight();
	}
	@Override
	public double getEyeHeight(boolean arg0) {
		return this.p.getEyeHeight(arg0);
	}
	@Override
	public Location getEyeLocation() {
		return this.p.getEyeLocation();
	}
	@Override
	public Player getKiller() {
		return this.p.getKiller();
	}
	@Override
	public double getLastDamage() {
		return this.p.getLastDamage();
	}
	@Deprecated
	@Override
	public List<Block> getLastTwoTargetBlocks(HashSet<Byte> arg0, int arg1) {
		return this.p.getLastTwoTargetBlocks(arg0, arg1);
	}
	@Override
	public List<Block> getLastTwoTargetBlocks(Set<Material> arg0, int arg1) {
		return this.p.getLastTwoTargetBlocks(arg0, arg1);
	}
	@Override
	public Entity getLeashHolder() throws IllegalStateException {
		return this.p.getLeashHolder();
	}
	@Deprecated
	@Override
	public List<Block> getLineOfSight(HashSet<Byte> arg0, int arg1) {
		return this.p.getLineOfSight(arg0, arg1);
	}
	@Override
	public List<Block> getLineOfSight(Set<Material> arg0, int arg1) {
		return this.p.getLineOfSight(arg0, arg1);
	}
	@Override
	public int getMaximumAir() {
		return this.p.getMaximumAir();
	}
	@Override
	public int getMaximumNoDamageTicks() {
		return this.p.getMaximumNoDamageTicks();
	}
	@Override
	public int getNoDamageTicks() {
		return this.p.getNoDamageTicks();
	}
	@Override
	public int getRemainingAir() {
		return this.p.getRemainingAir();
	}
	@Override
	public boolean getRemoveWhenFarAway() {
		return this.p.getRemoveWhenFarAway();
	}
	@Deprecated
	@Override
	public Block getTargetBlock(HashSet<Byte> arg0, int arg1) {
		return this.p.getTargetBlock(arg0, arg1);
	}
	@Override
	public Block getTargetBlock(Set<Material> arg0, int arg1) {
		return this.p.getTargetBlock(arg0, arg1);
	}
	@Override
	public boolean hasLineOfSight(Entity arg0) {
		return this.p.hasLineOfSight(arg0);
	}
	@Override
	public boolean hasPotionEffect(PotionEffectType arg0) {
		return this.p.hasPotionEffect(arg0);
	}
	@Override
	public boolean isLeashed() {
		return this.p.isLeashed();
	}
	@Override
	public void removePotionEffect(PotionEffectType arg0) {
		this.p.removePotionEffect(arg0);
	}
	@Override
	public void setCanPickupItems(boolean arg0) {
		this.p.setCanPickupItems(arg0);
	}
	@Override
	public void setLastDamage(double arg0) {
		this.p.setLastDamage(arg0);
	}
	@Override
	public boolean setLeashHolder(Entity arg0) {
		return this.p.setLeashHolder(arg0);
	}
	@Override
	public void setMaximumAir(int arg0) {
		this.p.setMaximumAir(arg0);
	}
	@Override
	public void setMaximumNoDamageTicks(int arg0) {
		this.p.setMaximumNoDamageTicks(arg0);
	}
	@Override
	public void setNoDamageTicks(int arg0) {
		this.p.setNoDamageTicks(arg0);
	}
	@Override
	public void setRemainingAir(int arg0) {
		this.p.setRemainingAir(arg0);
	}
	@Override
	public void setRemoveWhenFarAway(boolean arg0) {
		this.p.setRemoveWhenFarAway(arg0);
	}
	@Deprecated
	@Override
	public Arrow shootArrow() {
		return this.p.shootArrow();
	}
	@Deprecated
	@Override
	public Egg throwEgg() {
		return this.p.throwEgg();
	}
	@Deprecated
	@Override
	public Snowball throwSnowball() {
		return this.p.throwSnowball();
	}
	@Override
	public boolean eject() {
		return this.p.eject();
	}
	@Override
	public String getCustomName() {
		return this.p.getCustomName();
	}
	@Override
	public int getEntityId() {
		return this.p.getEntityId();
	}
	@Override
	public float getFallDistance() {
		return this.p.getFallDistance();
	}
	@Override
	public int getFireTicks() {
		return this.p.getFireTicks();
	}
	@Override
	public EntityDamageEvent getLastDamageCause() {
		return this.p.getLastDamageCause();
	}
	@Override
	public Location getLocation() {
		return this.p.getLocation();
	}
	@Override
	public Location getLocation(Location arg0) {
		return this.p.getLocation(arg0);
	}
	@Override
	public int getMaxFireTicks() {
		return this.p.getMaxFireTicks();
	}
	@Override
	public List<Entity> getNearbyEntities(double arg0, double arg1, double arg2) {
		return this.p.getNearbyEntities(arg0, arg1, arg2);
	}
	@Override
	public Entity getPassenger() {
		return this.p.getPassenger();
	}
	@Override
	public Server getServer() {
		return this.p.getServer();
	}
	@Override
	public int getTicksLived() {
		return this.p.getTicksLived();
	}
	@Override
	public EntityType getType() {
		return this.p.getType();
	}
	@Override
	public UUID getUniqueId() {
		return this.p.getUniqueId();
	}
	@Override
	public Entity getVehicle() {
		return this.p.getVehicle();
	}
	@Override
	public Vector getVelocity() {
		return this.p.getVelocity();
	}
	@Override
	public World getWorld() {
		return this.p.getWorld();
	}
	@Override
	public boolean isCustomNameVisible() {
		return this.p.isCustomNameVisible();
	}
	@Override
	public boolean isDead() {
		return this.p.isDead();
	}
	@Override
	public boolean isEmpty() {
		return this.p.isEmpty();
	}
	@Override
	public boolean isInsideVehicle() {
		return this.p.isInsideVehicle();
	}
	@Override
	public boolean isValid() {
		return this.p.isValid();
	}
	@Override
	public boolean leaveVehicle() {
		return this.p.leaveVehicle();
	}
	@Override
	public void playEffect(EntityEffect arg0) {
		this.p.playEffect(arg0);
	}
	@Override
	public void remove() {
		this.p.remove();
	}
	@Override
	public void setCustomName(String arg0) {
		this.p.setCustomName(arg0);
	}
	@Override
	public void setCustomNameVisible(boolean arg0) {
		this.p.setCustomNameVisible(arg0);
	}
	@Override
	public void setFallDistance(float arg0) {
		this.p.setFallDistance(arg0);
	}
	@Override
	public void setFireTicks(int arg0) {
		this.p.setFireTicks(arg0);
	}
	@Override
	public void setLastDamageCause(EntityDamageEvent arg0) {
		this.p.setLastDamageCause(arg0);
	}
	@Override
	public boolean setPassenger(Entity arg0) {
		return this.p.setPassenger(arg0);
	}
	@Override
	public void setTicksLived(int arg0) {
		this.p.setTicksLived(arg0);
	}
	@Override
	public void setVelocity(Vector arg0) {
		this.p.setVelocity(arg0);
	}
	@Override
	public boolean teleport(Location arg0) {
		return this.p.teleport(arg0);
	}
	@Override
	public boolean teleport(Entity arg0) {
		return this.p.teleport(arg0);
	}
	@Override
	public boolean teleport(Location arg0, TeleportCause arg1) {
		return this.p.teleport(arg0, arg1);
	}
	@Override
	public boolean teleport(Entity arg0, TeleportCause arg1) {
		return this.p.teleport(arg0, arg1);
	}
	@Override
	public List<MetadataValue> getMetadata(String arg0) {
		return this.p.getMetadata(arg0);
	}
	@Override
	public boolean hasMetadata(String arg0) {
		return this.p.hasMetadata(arg0);
	}
	@Override
	public void removeMetadata(String arg0, Plugin arg1) {
		this.p.removeMetadata(arg0, arg1);
	}
	@Override
	public void setMetadata(String arg0, MetadataValue arg1) {
		this.p.setMetadata(arg0, arg1);
	}
	@Override
	public void sendMessage(String arg0) {
		this.p.sendMessage(arg0);
	}
	@Override
	public void sendMessage(String[] arg0) {
		this.p.sendMessage(arg0);
	}
	@Override
	public PermissionAttachment addAttachment(Plugin arg0) {
		return this.p.addAttachment(arg0);
	}
	@Override
	public PermissionAttachment addAttachment(Plugin arg0, int arg1) {
		return this.p.addAttachment(arg0, arg1);
	}
	@Override
	public PermissionAttachment addAttachment(Plugin arg0, String arg1, boolean arg2) {
		return this.p.addAttachment(arg0, arg1, arg2);
	}
	@Override
	public PermissionAttachment addAttachment(Plugin arg0, String arg1, boolean arg2, int arg3) {
		return this.p.addAttachment(arg0, arg1, arg2, arg3);
	}
	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		return this.p.getEffectivePermissions();
	}
	@Override
	public boolean hasPermission(String arg0) {
		return this.p.hasPermission(arg0);
	}
	@Override
	public boolean hasPermission(Permission arg0) {
		return this.p.hasPermission(arg0);
	}
	@Override
	public boolean isPermissionSet(String arg0) {
		return this.p.isPermissionSet(arg0);
	}
	@Override
	public boolean isPermissionSet(Permission arg0) {
		return this.p.isPermissionSet(arg0);
	}
	@Override
	public void recalculatePermissions() {
		this.p.recalculatePermissions();
	}
	@Override
	public void removeAttachment(PermissionAttachment arg0) {
		this.p.removeAttachment(arg0);
	}
	@Override
	public boolean isOp() {
		return this.p.isOp();
	}
	@Override
	public void setOp(boolean arg0) {
		this.p.setOp(arg0);
	}
	@Override
	public void damage(double arg0) {
		this.p.damage(arg0);
	}
	@Override
	public void damage(double arg0, Entity arg1) {
		this.p.damage(arg0, arg1);
	}
	@Override
	public double getHealth() {
		return this.p.getHealth();
	}
	@Override
	public double getMaxHealth() {
		return this.p.getMaxHealth();
	}
	@Override
	public void resetMaxHealth() {
		this.p.resetMaxHealth();
	}
	@Override
	public void setHealth(double arg0) {
		this.p.setHealth(arg0);
	}
	@Override
	public void setMaxHealth(double arg0) {
		this.p.setMaxHealth(arg0);
	}
	@Override
	public <T extends Projectile> T launchProjectile(Class<? extends T> arg0) {
		return this.p.launchProjectile(arg0);
	}
	@Override
	public <T extends Projectile> T launchProjectile(Class<? extends T> arg0, Vector arg1) {
		return this.p.launchProjectile(arg0, arg1);
	}
	@Override
	public void abandonConversation(Conversation arg0) {
		this.p.abandonConversation(arg0);
	}
	@Override
	public void abandonConversation(Conversation arg0, ConversationAbandonedEvent arg1) {
		this.p.abandonConversation(arg0, arg1);
	}
	@Override
	public void acceptConversationInput(String arg0) {
		this.p.acceptConversationInput(arg0);
	}
	@Override
	public boolean beginConversation(Conversation arg0) {
		return this.p.beginConversation(arg0);
	}
	@Override
	public boolean isConversing() {
		return this.p.isConversing();
	}
	@Override
	public long getFirstPlayed() {
		return this.p.getFirstPlayed();
	}
	@Override
	public long getLastPlayed() {
		return this.p.getLastPlayed();
	}
	@Override
	public Player getPlayer() {
		return this.p.getPlayer();
	}
	@Override
	public boolean hasPlayedBefore() {
		return this.p.hasPlayedBefore();
	}
	@Override
	public boolean isBanned() {
		return this.p.isBanned();
	}
	@Override
	public boolean isOnline() {
		return this.p.isOnline();
	}
	@Override
	public boolean isWhitelisted() {
		return this.p.isWhitelisted();
	}
	@Deprecated
	@Override
	public void setBanned(boolean arg0) {
		this.p.setBanned(arg0);
	}
	@Override
	public void setWhitelisted(boolean arg0) {
		this.p.isWhitelisted();
	}
	@Override
	public Map<String, Object> serialize() {
		return this.p.serialize();
	}
	@Override
	public Set<String> getListeningPluginChannels() {
		return this.p.getListeningPluginChannels();
	}
	@Override
	public void sendPluginMessage(Plugin arg0, String arg1, byte[] arg2) {
		this.p.sendPluginMessage(arg0, arg1, arg2);
	}
	@Override
	public void awardAchievement(Achievement arg0) {
		this.p.awardAchievement(arg0);
	}
	@Override
	public boolean canSee(Player arg0) {
		return this.p.canSee(arg0);
	}
	@Override
	public void chat(String arg0) {
		this.p.chat(arg0);
	}
	@Override
	public void decrementStatistic(Statistic arg0) throws IllegalArgumentException {
		this.p.decrementStatistic(arg0);
	}
	@Override
	public void decrementStatistic(Statistic arg0, int arg1) throws IllegalArgumentException {
		this.p.decrementStatistic(arg0, arg1);
	}
	@Override
	public void decrementStatistic(Statistic arg0, Material arg1) throws IllegalArgumentException {
		this.p.decrementStatistic(arg0, arg1);
	}
	@Override
	public void decrementStatistic(Statistic arg0, EntityType arg1) throws IllegalArgumentException {
		this.p.decrementStatistic(arg0, arg1);
	}
	@Override
	public void decrementStatistic(Statistic arg0, Material arg1, int arg2) throws IllegalArgumentException {
		this.p.decrementStatistic(arg0, arg1, arg2);
	}
	@Override
	public void decrementStatistic(Statistic arg0, EntityType arg1, int arg2) {
		this.p.decrementStatistic(arg0, arg1, arg2);
	}
	@Override
	public InetSocketAddress getAddress() {
		return this.p.getAddress();
	}
	@Override
	public boolean getAllowFlight() {
		return this.p.getAllowFlight();
	}
	@Override
	public Location getBedSpawnLocation() {
		return this.p.getBedSpawnLocation();
	}
	@Override
	public Location getCompassTarget() {
		return this.p.getCompassTarget();
	}
	@Override
	public String getDisplayName() {
		return this.p.getDisplayName();
	}
	@Override
	public float getExhaustion() {
		return this.p.getExhaustion();
	}
	@Override
	public float getExp() {
		return this.p.getExp();
	}
	@Override
	public float getFlySpeed() {
		return this.p.getFlySpeed();
	}
	@Override
	public int getFoodLevel() {
		return this.p.getFoodLevel();
	}
	@Override
	public double getHealthScale() {
		return this.p.getHealthScale();
	}
	@Override
	public int getLevel() {
		return this.p.getLevel();
	}
	@Override
	public String getPlayerListName() {
		return this.p.getPlayerListName();
	}
	@Override
	public long getPlayerTime() {
		return this.p.getPlayerTime();
	}
	@Override
	public long getPlayerTimeOffset() {
		return this.p.getPlayerTimeOffset();
	}
	@Override
	public WeatherType getPlayerWeather() {
		return this.p.getPlayerWeather();
	}
	@Override
	public float getSaturation() {
		return this.p.getSaturation();
	}
	@Override
	public Scoreboard getScoreboard() {
		return this.p.getScoreboard();
	}
	@Override
	public int getStatistic(Statistic arg0) throws IllegalArgumentException {
		return this.p.getStatistic(arg0);
	}
	@Override
	public int getStatistic(Statistic arg0, Material arg1) throws IllegalArgumentException {
		return this.p.getStatistic(arg0, arg1);
	}
	@Override
	public int getStatistic(Statistic arg0, EntityType arg1) throws IllegalArgumentException {
		return this.p.getStatistic(arg0, arg1);
	}
	@Override
	public int getTotalExperience() {
		return this.p.getTotalExperience();
	}
	@Override
	public float getWalkSpeed() {
		return this.p.getWalkSpeed();
	}
	@Override
	public void giveExp(int arg0) {
		this.p.giveExp(arg0);
	}
	@Override
	public void giveExpLevels(int arg0) {
		this.p.giveExpLevels(arg0);
	}
	@Override
	public boolean hasAchievement(Achievement arg0) {
		return this.p.hasAchievement(arg0);
	}
	@Override
	public void hidePlayer(Player arg0) {
		this.p.hidePlayer(arg0);
	}
	@Override
	public void incrementStatistic(Statistic arg0) throws IllegalArgumentException {
		this.p.incrementStatistic(arg0);
	}
	@Override
	public void incrementStatistic(Statistic arg0, int arg1) throws IllegalArgumentException {
		this.p.incrementStatistic(arg0, arg1);
	}
	@Override
	public void incrementStatistic(Statistic arg0, Material arg1) throws IllegalArgumentException {
		this.p.incrementStatistic(arg0, arg1);
	}
	@Override
	public void incrementStatistic(Statistic arg0, EntityType arg1) throws IllegalArgumentException {
		this.p.incrementStatistic(arg0, arg1);
	}
	@Override
	public void incrementStatistic(Statistic arg0, Material arg1, int arg2) throws IllegalArgumentException {
		this.p.incrementStatistic(arg0, arg1, arg2);
	}
	@Override
	public void incrementStatistic(Statistic arg0, EntityType arg1, int arg2) throws IllegalArgumentException {
		this.p.incrementStatistic(arg0, arg1, arg2);
	}
	@Override
	public boolean isFlying() {
		return this.p.isFlying();
	}
	@Override
	public boolean isHealthScaled() {
		return this.p.isHealthScaled();
	}
	@Deprecated
	@Override
	public boolean isOnGround() {
		return this.p.isOnGround();
	}
	@Override
	public boolean isPlayerTimeRelative() {
		return this.p.isPlayerTimeRelative();
	}
	@Override
	public boolean isSleepingIgnored() {
		return this.p.isSleepingIgnored();
	}
	@Override
	public boolean isSneaking() {
		return this.p.isSneaking();
	}
	@Override
	public boolean isSprinting() {
		return this.p.isSprinting();
	}
	@Override
	public void kickPlayer(String arg0) {
		this.p.kickPlayer(arg0);
	}
	@Override
	public void loadData() {
		this.p.loadData();
	}
	@Override
	public boolean performCommand(String arg0) {
		return this.p.performCommand(arg0);
	}
	@Deprecated
	@Override
	public void playEffect(Location arg0, Effect arg1, int arg2) {
		this.p.playEffect(arg0, arg1, arg2);
	}
	@Override
	public <T> void playEffect(Location arg0, Effect arg1, T arg2) {
		this.p.playEffect(arg0, arg1, arg2);
	}
	@Deprecated
	@Override
	public void playNote(Location arg0, byte arg1, byte arg2) {
		this.p.playNote(arg0, arg1, arg2);
	}
	@Override
	public void playNote(Location arg0, Instrument arg1, Note arg2) {
		this.p.playNote(arg0, arg1, arg2);
	}
	@Override
	public void playSound(Location arg0, Sound arg1, float arg2, float arg3) {
		this.p.playSound(arg0, arg1, arg2, arg3);
	}
	@Deprecated
	@Override
	public void playSound(Location arg0, String arg1, float arg2, float arg3) {
		this.p.playSound(arg0, arg1, arg2, arg3);
	}
	@Override
	public void removeAchievement(Achievement arg0) {
		this.p.removeAchievement(arg0);
	}
	@Override
	public void resetPlayerTime() {
		this.p.resetPlayerTime();
	}
	@Override
	public void resetPlayerWeather() {
		this.p.resetPlayerWeather();
	}
	@Override
	public void saveData() {
		this.p.saveData();
	}
	@Deprecated
	@Override
	public void sendBlockChange(Location arg0, Material arg1, byte arg2) {
		this.p.sendBlockChange(arg0, arg1, arg2);
	}
	@Deprecated
	@Override
	public void sendBlockChange(Location arg0, int arg1, byte arg2) {
		this.p.sendBlockChange(arg0, arg1, arg2);
	}
	@Deprecated
	@Override
	public boolean sendChunkChange(Location arg0, int arg1, int arg2, int arg3, byte[] arg4) {
		return this.p.sendChunkChange(arg0, arg1, arg2, arg3, arg4);
	}
	@Override
	public void sendMap(MapView arg0) {
		this.p.sendMap(arg0);
	}
	@Override
	public void sendRawMessage(String arg0) {
		this.p.sendRawMessage(arg0);
	}
	@Override
	public void sendSignChange(Location arg0, String[] arg1) throws IllegalArgumentException {
		this.p.sendSignChange(arg0, arg1);
	}
	@Override
	public void setAllowFlight(boolean arg0) {
		this.p.setAllowFlight(arg0);
	}
	@Override
	public void setBedSpawnLocation(Location arg0) {
		this.p.setBedSpawnLocation(arg0);
	}
	@Override
	public void setBedSpawnLocation(Location arg0, boolean arg1) {
		this.p.setBedSpawnLocation(arg0, arg1);
	}
	@Override
	public void setCompassTarget(Location arg0) {
		this.p.setCompassTarget(arg0);
	}
	@Override
	public void setDisplayName(String arg0) {
		this.p.setDisplayName(arg0);
	}
	@Override
	public void setExhaustion(float arg0) {
		this.p.setExhaustion(arg0);
	}
	@Override
	public void setExp(float arg0) {
		this.p.setExp(arg0);
	}
	@Override
	public void setFlySpeed(float arg0) throws IllegalArgumentException {
		this.p.setFlySpeed(arg0);
	}
	@Override
	public void setFlying(boolean arg0) {
		this.p.setFlying(arg0);
	}
	@Override
	public void setFoodLevel(int arg0) {
		this.p.setFoodLevel(arg0);
	}
	@Override
	public void setHealthScale(double arg0) throws IllegalArgumentException {
		this.p.setHealthScale(arg0);
	}
	@Override
	public void setHealthScaled(boolean arg0) {
		this.p.setHealthScaled(arg0);
	}
	@Override
	public void setLevel(int arg0) {
		this.p.setLevel(arg0);
	}
	@Override
	public void setPlayerListName(String arg0) {
		this.p.setPlayerListName(arg0);
	}
	@Override
	public void setPlayerTime(long arg0, boolean arg1) {
		this.p.setPlayerTime(arg0, arg1);
	}
	@Override
	public void setPlayerWeather(WeatherType arg0) {
		this.p.setPlayerWeather(arg0);
	}
	@Override
	public void setResourcePack(String arg0) {
		this.p.setResourcePack(arg0);
	}
	@Override
	public void setSaturation(float arg0) {
		this.p.setSaturation(arg0);
	}
	@Override
	public void setScoreboard(Scoreboard arg0) throws IllegalArgumentException, IllegalStateException {
		this.p.setScoreboard(arg0);
	}
	@Override
	public void setSleepingIgnored(boolean arg0) {
		this.p.setSleepingIgnored(arg0);
	}
	@Override
	public void setSneaking(boolean arg0) {
		this.p.setSneaking(arg0);
	}
	@Override
	public void setSprinting(boolean arg0) {
		this.p.setSprinting(arg0);
	}
	@Override
	public void setStatistic(Statistic arg0, int arg1) throws IllegalArgumentException {
		this.p.setStatistic(arg0, arg1);
	}
	@Override
	public void setStatistic(Statistic arg0, Material arg1, int arg2) throws IllegalArgumentException {
		this.p.setStatistic(arg0, arg1, arg2);
	}
	@Override
	public void setStatistic(Statistic arg0, EntityType arg1, int arg2) {
		this.p.setStatistic(arg0, arg1, arg2);
	}
	@Deprecated
	@Override
	public void setTexturePack(String arg0) {
		this.p.setTexturePack(arg0);
	}
	@Override
	public void setTotalExperience(int arg0) {
		this.p.setTotalExperience(arg0);
	}
	@Override
	public void setWalkSpeed(float arg0) throws IllegalArgumentException {
		this.p.setWalkSpeed(arg0);
	}
	@Override
	public void showPlayer(Player arg0) {
		this.p.showPlayer(arg0);
	}
	@Override
	public Spigot spigot() {
		return this.p.spigot();
	}
	@Override
	public void updateInventory() {
		this.p.updateInventory();
	}
}