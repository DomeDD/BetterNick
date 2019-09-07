/*
 * All rights by DomeDD (2019)
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free and as long as you mention me (DomeDD) 
 * You are NOT allowed to claim this plugin (BetterNick) as your own
 * You are NOT allowed to publish this plugin (BetterNick) or your modified version of this plugin (BetterNick)
 * 
 */
package de.domedd.betternick.api.betternickapi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import de.domedd.betternick.BetterNick;

public class PlayerData implements Listener {
	
	private Player p;
	private Location loc, bedspawnloc;
	private double health, maxhealth,  oldbalance, newbalance;
	private String defname, defchatprefix, defchatsuffix, defdisplayname, deftablistname, chatprefix, chatsuffix, displayname, tablistname, nickname, defskin, skin, inv;
	private int food, totexp;
	private float exp, flyspeed, walkspeed, saturation;
	private boolean flying, allowflying;
	private GameMode gamemode;
	
	private static BetterNick pl;
	
	@SuppressWarnings("static-access")
	public PlayerData(BetterNick main) {
		this.pl = main;
	}
	
	public PlayerData(Player player) {
		p = player;
		defskin = player.getName();
		skin = player.getName();
		defname = p.getName();
		defdisplayname = p.getDisplayName();
		deftablistname = p.getPlayerListName();
		if(pl.econ != null) {
			oldbalance = pl.econ.getBalance(p);
			newbalance = pl.econ.getBalance(p);
		}
		if(pl.chat != null) {
			defchatsuffix = pl.chat.getPlayerSuffix(p);
			chatsuffix = pl.chat.getPlayerSuffix(p);
			defchatprefix = pl.chat.getPlayerPrefix(p);
			chatprefix = pl.chat.getPlayerPrefix(p);
		}
	}
	
	public void setNewBalance(double newBalance) {
		newbalance = newBalance;
	}
	public void createNewBalanceAccount() {
		if(!pl.econ.hasAccount(p)) {
			pl.econ.createPlayerAccount(p);
		}
		pl.econ.depositPlayer(p, oldbalance);
		if(pl.econ.getBalance(p) > oldbalance) {
			pl.econ.withdrawPlayer(p, pl.econ.getBalance(p) - oldbalance);
		}
	}
	public void deleteNewBalanceAccount() {
		pl.econ.deleteBank(p.getName());
	}
	public void updateOldBalance() {
		if(!pl.econ.hasAccount(p)) {
			pl.econ.createPlayerAccount(p);
		}
		double balance = newbalance;
		pl.econ.withdrawPlayer(p, pl.econ.getBalance(p));
		pl.econ.depositPlayer(p, balance);
	}
	
	public void setSkin(String skin) {
		this.skin = skin;
	}
	public boolean hasNewSkin() {
		return !defskin.equals(skin);
	}
	public void setNickName(String nickname) {
		this.nickname = nickname;
	}
	public void setChatPrefix(String prefix) {
		chatprefix = prefix;
		if(pl.chat != null) {
			pl.chat.setPlayerPrefix(p.getWorld().getName(), p, prefix);
		}
	}
	public void setChatSuffix(String suffix) {
		chatprefix = suffix;
		if(pl.chat != null) {
			pl.chat.setPlayerSuffix(p.getWorld().getName(), p, suffix);
		}
	}
	public void setDisplayName(String displayname) {
		this.displayname = displayname;
		p.setDisplayName(displayname);
	}
	public void setTablistName(String tablistname) {
		this.tablistname = tablistname;
		Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
			@Override
			public void run() {
				p.setPlayerListName(tablistname);
			}
		}, 20);
	}
	
	public String getSkin() {
		return skin;
	}
	public String getNickName() {
		return nickname;
	}
	public String getChatPrefix() {
		return chatprefix;
	}
	public String getChatSuffix() {
		return chatsuffix;
	}
	public String getDisplayName() {
		return displayname;
	}
	public String getTablistName() {
		return tablistname;
	}
	public String getDefaultName() {
		return defname;
	}
	public String getDefaultChatPrefix() {
		return defchatprefix;
	}
	public String getDefaultChatSuffix() {
		return defchatsuffix;
	}
	public String getDefaultDisplayName() {
		return defdisplayname;
	}
	public String getDefaultTablistName() {
		return deftablistname;
	}
	public GameMode getGameMode() {
		return gamemode;
	}
	public Location getLocation() {
		return loc;
	}
	
	public void saveData() {
		loc = p.getLocation();
		health = p.getHealth();
		maxhealth = p.getHealthScale();
		food = p.getFoodLevel();
		saturation = p.getSaturation();
		flying = p.isFlying();
		allowflying = p.getAllowFlight();
		bedspawnloc = p.getBedSpawnLocation();
		totexp = p.getTotalExperience();
		exp = p.getExp();
		flyspeed = p.getFlySpeed();
		walkspeed = p.getWalkSpeed();
		gamemode = p.getGameMode();
		inv = toBase64(p.getInventory());
	}
	public void setNickedPlayerData() {
		p.teleport(loc);
		p.setHealthScale(maxhealth);
		p.setHealth(health);
		p.setFoodLevel(food);
		p.setSaturation(saturation);
		p.setAllowFlight(allowflying);
		p.setFlying(flying);
		p.setBedSpawnLocation(bedspawnloc);
		p.setExp(exp);
		p.setTotalExperience(totexp);
		p.setFlySpeed(flyspeed);
		p.setWalkSpeed(walkspeed);
		p.setGameMode(gamemode);
		for(Map.Entry<Integer, ItemStack> map : fromBase64(inv).entrySet()) {
			p.getInventory().setItem(map.getKey(), map.getValue());
		}
	}
	
	private String toBase64(Inventory inv) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(inv.getSize());
            for (int i = 0; i < inv.getSize(); i++) {
                dataOutput.writeObject(inv.getItem(i));
            }
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks", e);
        }        
    }
	private Map<Integer, ItemStack> fromBase64(String from) {
		Map<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(from));
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			int size = dataInput.readInt();
			for(int i = 0; i < size; i++) {
				items.put(i, (ItemStack) dataInput.readObject());
			}
			dataInput.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return items;
    }
}
