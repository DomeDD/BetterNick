/*
 * All rights by DomeDD
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free 
 * You are NOT allowed to claim this plugin as your own
 * You are NOT allowed to publish this plugin or your modified version of this plugin
 * 
 */
package de.domedd.betternick.addons;

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
import de.domedd.betternick.api.nickedplayer.NickedPlayer;

public class AutoNickItem implements Listener {
	
	private BetterNick pl;
	
	public AutoNickItem(BetterNick main) {
		this.pl = main;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if(pl.getConfig().getBoolean("Addons.AutoNickItem.Get On Server Join")) {
			if(p.hasPermission("BetterNick.JoinItem")) {
				String item = pl.getConfig().getString("Addons.AutoNickItem.Item");
				int ID = 0;
			    int subID = 0;
			    String name = "";
			    int slot = 0;
			    if(item.contains(":")) {
			    	String[] array = item.split(":");
			    	ID = Integer.valueOf(array[0]).intValue();
			    	String a = array[1];
			    	a = a.substring(0, 1);
			    	subID = Integer.valueOf(a).intValue();
			    }
			    String[] array = item.split(", ");
			    name = array[1];
			    slot = Integer.valueOf(array[2]);
				@SuppressWarnings("deprecation")
				ItemStack is = new ItemStack(ID, 1, (short) subID);
				ItemMeta im = is.getItemMeta();
				im.setDisplayName(name.replace("&", "§"));
				is.setItemMeta(im);
				p.getInventory().setItem(slot, is);
			}
		}
	}
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent e) {
		Player p = e.getPlayer();
		if(pl.getConfig().getStringList("Addons.AutoNickItem.Get On World Change").contains(p.getWorld().getName())) {
			if(p.hasPermission("BetterNick.JoinItem")) {
				String item = pl.getConfig().getString("Addons.AutoNickItem.Item");
				int ID = 0;
			    int subID = 0;
			    String name = "";
			    int slot = 0;
			    if(item.contains(":")) {
			    	String[] array = item.split(":");
			    	ID = Integer.valueOf(array[0]).intValue();
			    	String a = array[1];
			    	a = a.substring(0, 1);
			    	subID = Integer.valueOf(a).intValue();
			    }
			    String[] array = item.split(", ");
			    name = array[1];
			    slot = Integer.valueOf(array[2]);
				@SuppressWarnings("deprecation")
				ItemStack is = new ItemStack(ID, 1, (short) subID);
				ItemMeta im = is.getItemMeta();
				im.setDisplayName(name.replace("&", "§"));
				is.setItemMeta(im);
				p.getInventory().setItem(slot, is);
			}
		}
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		NickedPlayer p = new NickedPlayer(e.getPlayer());
		String[] autoNItemID = pl.getConfig().getString("Addons.AutoNickItem.Item").split(":");
		String[] autoNItem = pl.getConfig().getString("Addons.AutoNickItem.Item").split(", ");
		try {
			if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if(e.getItem().getTypeId() == Integer.valueOf(autoNItemID[0])) {
					if(e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(autoNItem[1].replace("&", "§"))) {
						if(p.hasPermission("BetterNick.JoinItem")) {
							e.setCancelled(true);
							if(p.hasAutoNick()) {
								openTrueNickInv(p);
							} else {
								openFalseNickInv(p);
							}
						}
					}
				}
			}
		} catch(NullPointerException ex) {}
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		String[] autoNItemID = pl.getConfig().getString("Addons.AutoNickItem.Item").split(":");
		String[] autoNItem = pl.getConfig().getString("Addons.AutoNickItem.Item").split(", ");
		if(e.getItemDrop().getItemStack().getTypeId() == Integer.valueOf(autoNItemID[0]) && e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase(autoNItem[1].replace("&", "§"))) {
			if(!pl.getConfig().getBoolean("Addons.AutoNickItem.Item Moveable")) {
				e.setCancelled(true);
			}
		}
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		NickedPlayer p = new NickedPlayer((Player)e.getWhoClicked());
		String[] autoNItemID = pl.getConfig().getString("Addons.AutoNickItem.Item").split(":");
		try {
			if(e.getCurrentItem().getTypeId() == Integer.valueOf(autoNItemID[0])) {
				if(!pl.getConfig().getBoolean("Addons.AutoNickItem.Item Moveable")) {
					e.setCancelled(true);
				}
			}
			if(e.getInventory().getName() != null && e.getInventory().getName().equalsIgnoreCase(pl.getConfig().getString("Addons.AutoNickItem.Inventory.Name").replace("&", "§"))) {
				e.setCancelled(true);
				String[] autoTrueID = pl.getConfig().getString("Addons.AutoNickItem.Inventory.AutoNick True").split(":");
				String[] autoTrue = pl.getConfig().getString("Addons.AutoNickItem.Inventory.AutoNick True").split(", ");
				String[] autoFalseID = pl.getConfig().getString("Addons.AutoNickItem.Inventory.AutoNick False").split(":");
				String[] autoFalse = pl.getConfig().getString("Addons.AutoNickItem.Inventory.AutoNick False").split(", ");
				if(e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(autoTrue[1].replace("&", "§"))) {
					p.setAutoNick(false);
					e.getCurrentItem().setTypeId(Integer.valueOf(autoFalseID[0]));
					ItemStack is = e.getCurrentItem();
					ItemMeta im = is.getItemMeta();
					im.setDisplayName(autoFalse[1].replace("&", "§"));
					is.setItemMeta(im);
					p.updateInventory();
					p.sendMessage(pl.getConfig().getString("Messages.AutoNick Turned Off").replace("&", "§"));
				} else if(e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(autoFalse[1].replace("&", "§"))) {
					p.setAutoNick(true);
					e.getCurrentItem().setTypeId(Integer.valueOf(autoTrueID[0]));
					ItemStack is1 = e.getCurrentItem();
					ItemMeta im1 = is1.getItemMeta();
					im1.setDisplayName(autoTrue[1].replace("&", "§"));
					is1.setItemMeta(im1);
					p.updateInventory();
					p.sendMessage(pl.getConfig().getString("Messages.AutoNick Turned On").replace("&", "§"));
				}
			}
			
		} catch(NullPointerException ex) {}
	}
	private void openFalseNickInv(Player p) {
		Inventory inv = Bukkit.createInventory(p, 9, pl.getConfig().getString("Addons.AutoNickItem.Inventory.Name").replace("&", "§"));
		ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
		String[] autoFalseID = pl.getConfig().getString("Addons.AutoNickItem.Inventory.AutoNick False").split(":");
		String[] autoFalse = pl.getConfig().getString("Addons.AutoNickItem.Inventory.AutoNick False").split(", ");
		@SuppressWarnings("deprecation")
		ItemStack isFalse = new ItemStack(Integer.valueOf(autoFalseID[0]));
		ItemMeta im = isFalse.getItemMeta();
		im.setDisplayName(autoFalse[1].replace("&", "§"));
		isFalse.setItemMeta(im);
		for(int i = 0; i != 9; i++) {
			inv.setItem(i, is);
		}
		inv.setItem(4, isFalse);
		p.openInventory(inv);
	}
	private void openTrueNickInv(Player p) {
		Inventory inv = Bukkit.createInventory(p, 9, pl.getConfig().getString("Addons.AutoNickItem.Inventory.Name").replace("&", "§"));		
		ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
		String[] autoTrueID = pl.getConfig().getString("Addons.AutoNickItem.Inventory.AutoNick True").split(":");
		String[] autoTrue = pl.getConfig().getString("Addons.AutoNickItem.Inventory.AutoNick True").split(", ");
		@SuppressWarnings("deprecation")
		ItemStack isTrue = new ItemStack(Integer.valueOf(autoTrueID[0]));
		ItemMeta im = isTrue.getItemMeta();
		im.setDisplayName(autoTrue[1].replace("&", "§"));
		isTrue.setItemMeta(im);
		for(int i = 0; i != 9; i++) {
			inv.setItem(i, is);
		}
		inv.setItem(4, isTrue);
		p.openInventory(inv);
	}
}
