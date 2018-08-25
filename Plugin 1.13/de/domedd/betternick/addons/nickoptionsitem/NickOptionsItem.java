/*
 * All rights by DomeDD (2018)
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free and as long as you mention me (DomeDD) 
 * You are NOT allowed to claim this plugin (BetterNick) as your own
 * You are NOT allowed to publish this plugin (BetterNick) or your modified version of this plugin (BetterNick)
 * 
 */
package de.domedd.betternick.addons.nickoptionsitem;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.domedd.betternick.BetterNick;
import de.domedd.betternick.api.betternickapi.BetterNickAPI;

public class NickOptionsItem implements Listener {
	
	private BetterNick pl;
	
	public NickOptionsItem(BetterNick main) {
		this.pl = main;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if(pl.getConfig().getBoolean("Addons.Nick Options Item.Get On Server Join")) {
			if(p.hasPermission("BetterNick.JoinItem")) {
				String[] item = pl.getConfig().getString("Addons.Nick Options Item.Item").split(", ");
				Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
					@Override
					public void run() {
						p.getInventory().setItem(Integer.valueOf(item[2]), getItemFromString(pl.getConfig().getString("Addons.Nick Options Item.Item")));
					}
				}, 10);
			}
		}
	}
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent e) {
		Player p = e.getPlayer();
		if(pl.getConfig().getStringList("Addons.Nick Options Item.Get On World Change").contains(p.getWorld().getName())) {
			if(p.hasPermission("BetterNick.JoinItem")) {
				String[] item = pl.getConfig().getString("Addons.Nick Options Item.Item").split(", ");
				Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
					@Override
					public void run() {
						p.getInventory().setItem(Integer.valueOf(item[2]), getItemFromString(pl.getConfig().getString("Addons.Nick Options Item.Item")));
					}
				}, 10);
			}
		}
	}
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		String[] autoNItem = pl.getConfig().getString("Addons.Nick Options Item.Item").split(", ");
		try {
			if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if(pl.getConfig().getStringList("Addons.Nick Options Item.Enabled Worlds").contains(p.getWorld().getName())) {
					if(e.getItem().hasItemMeta() && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(autoNItem[1].replace("&", "§"))) {
						if(p.hasPermission("BetterNick.JoinItem")) {
							e.setCancelled(true);
							openNickOptionsInv(p);
						}
					}
				}
			}
		} catch(NullPointerException ex) {}
	}
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		String[] autoNItem = pl.getConfig().getString("Addons.Nick Options Item.Item").split(", ");
		if(e.getItemDrop().getItemStack().getType() == Material.getMaterial(autoNItem[0]) && e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase(autoNItem[1].replace("&", "§"))) {
			if(!pl.getConfig().getBoolean("Addons.Nick Options Item.Item Moveable")) {
				e.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onANIInvClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		String[] nickSettingsItem = pl.getConfig().getString("Addons.Nick Options Item.Item").split(", ");
		try {
			if(e.getCurrentItem().getType() == Material.getMaterial(nickSettingsItem[0])) {
				if(!pl.getConfig().getBoolean("Addons.Nick Options Item.Item Moveable")) {
					e.setCancelled(true);
				}
			}
			if(e.getInventory().getName() != null && e.getInventory().getName().equalsIgnoreCase(pl.getConfig().getString("Addons.Nick Options Item.Inventory.Name").replace("&", "§"))) {
				e.setCancelled(true);
				String[] autoNTrue = pl.getConfig().getString("Addons.Nick Options Item.Inventory.AutoNick True").split(", ");
				String[] autoNFalse = pl.getConfig().getString("Addons.Nick Options Item.Inventory.AutoNick False").split(", ");
				String[] keepNTrue = pl.getConfig().getString("Addons.Nick Options Item.Inventory.KeepNick True").split(", ");
				String[] keepNFalse = pl.getConfig().getString("Addons.Nick Options Item.Inventory.KeepNick False").split(", ");
				if(e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(autoNTrue[1].replace("&", "§"))) {
					BetterNickAPI.getApi().setPlayerAutoNick(p, false);
					e.setCurrentItem(getItemFromString(pl.getConfig().getString("Addons.Nick Options Item.Inventory.AutoNick False")));
					ItemStack is = e.getCurrentItem();
					ItemMeta im = is.getItemMeta();
					im.setDisplayName(autoNFalse[1].replace("&", "§"));
					is.setItemMeta(im);
					p.updateInventory();
					p.sendMessage(pl.prefix + pl.getConfig().getString("Messages.AutoNick Turned Off").replace("&", "§"));
				} else if(e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(autoNFalse[1].replace("&", "§"))) {
					BetterNickAPI.getApi().setPlayerAutoNick(p, true);
					e.setCurrentItem(getItemFromString(pl.getConfig().getString("Addons.Nick Options Item.Inventory.AutoNick True")));
					ItemStack is1 = e.getCurrentItem();
					ItemMeta im1 = is1.getItemMeta();
					im1.setDisplayName(autoNTrue[1].replace("&", "§"));
					is1.setItemMeta(im1);
					p.updateInventory();
					p.sendMessage(pl.prefix + pl.getConfig().getString("Messages.AutoNick Turned On").replace("&", "§"));
				} else if(e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(keepNTrue[1].replace("&", "§"))) {
					BetterNickAPI.getApi().setPlayerKeepNick(p, false);
					e.setCurrentItem(getItemFromString(pl.getConfig().getString("Addons.Nick Options Item.Inventory.KeepNick False")));
					ItemStack is1 = e.getCurrentItem();
					ItemMeta im1 = is1.getItemMeta();
					im1.setDisplayName(keepNFalse[1].replace("&", "§"));
					is1.setItemMeta(im1);
					p.updateInventory();
					p.sendMessage(pl.prefix + pl.getConfig().getString("Messages.KeepNick Turned Off").replace("&", "§"));
				} else if(e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(keepNFalse[1].replace("&", "§"))) {
					BetterNickAPI.getApi().setPlayerKeepNick(p, true);
					e.setCurrentItem(getItemFromString(pl.getConfig().getString("Addons.Nick Options Item.Inventory.KeepNick True")));
					ItemStack is1 = e.getCurrentItem();
					ItemMeta im1 = is1.getItemMeta();
					im1.setDisplayName(keepNTrue[1].replace("&", "§"));
					is1.setItemMeta(im1);
					p.updateInventory();
					p.sendMessage(pl.prefix + pl.getConfig().getString("Messages.KeepNick Turned On").replace("&", "§"));
				}
			}
		} catch(NullPointerException ex) {}
	}
	private void openNickOptionsInv(Player p) {
		Inventory inv = Bukkit.createInventory(p, 9, pl.getConfig().getString("Addons.Nick Options Item.Inventory.Name").replace("&", "§"));
		for(int i = 0; i != 9; i++) {
			inv.setItem(i, glassPane());
		}
		ItemStack autoNick = null;
		ItemStack keepNick = null;
		if(BetterNickAPI.getApi().hasPlayerAutoNick(p)) {
			String[] name = pl.getConfig().getString("Addons.Nick Options Item.Inventory.AutoNick True").split(", ");
			autoNick = getItemFromString(pl.getConfig().getString("Addons.Nick Options Item.Inventory.AutoNick True"));
			ItemMeta im = autoNick.getItemMeta();
			im.setDisplayName(name[1].replace("&", "§"));
			autoNick.setItemMeta(im);
			inv.setItem(Integer.valueOf(name[2]), autoNick);
		} else {
			String[] name = pl.getConfig().getString("Addons.Nick Options Item.Inventory.AutoNick False").split(", ");
			autoNick = getItemFromString(pl.getConfig().getString("Addons.Nick Options Item.Inventory.AutoNick False"));
			ItemMeta im = autoNick.getItemMeta();
			im.setDisplayName(name[1].replace("&", "§"));
			autoNick.setItemMeta(im);
			inv.setItem(Integer.valueOf(name[2]), autoNick);
		}
		if(BetterNickAPI.getApi().hasPlayerKeepNick(p)) {
			String[] name = pl.getConfig().getString("Addons.Nick Options Item.Inventory.KeepNick True").split(", ");
			keepNick = getItemFromString(pl.getConfig().getString("Addons.Nick Options Item.Inventory.KeepNick True"));
			ItemMeta im = keepNick.getItemMeta();
			im.setDisplayName(name[1].replace("&", "§"));
			keepNick.setItemMeta(im);
			inv.setItem(Integer.valueOf(name[2]), keepNick);
		} else {
			String[] name = pl.getConfig().getString("Addons.Nick Options Item.Inventory.KeepNick False").split(", ");
			keepNick = getItemFromString(pl.getConfig().getString("Addons.Nick Options Item.Inventory.KeepNick False"));
			ItemMeta im = keepNick.getItemMeta();
			im.setDisplayName(name[1].replace("&", "§"));
			keepNick.setItemMeta(im);
			inv.setItem(Integer.valueOf(name[2]), keepNick);
		}
		p.openInventory(inv);
	}
	private ItemStack getItemFromString(String from) {
	    String[] array = from.split(", ");
	    String mat = array[0];
	    String name = array[1];
		ItemStack is = new ItemStack(Material.getMaterial(mat), 1);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name.replace("&", "§"));
		is.setItemMeta(im);
		return is;
	}
	private static ItemStack glassPane() {
		ItemStack is = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName("§r");
		is.setItemMeta(im);
		return is;
	}
}
