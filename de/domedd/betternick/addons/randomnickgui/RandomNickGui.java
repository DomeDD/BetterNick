/*
 * All rights by DomeDD
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free 
 * You are NOT allowed to claim this plugin as your own
 * You are NOT allowed to publish this plugin or your modified version of this plugin
 * 
 */
package de.domedd.betternick.addons.randomnickgui;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.domedd.betternick.BetterNick;
import de.domedd.betternick.api.nickedplayer.NickedPlayer;

public class RandomNickGui implements Listener {

private static BetterNick pl;
	
	@SuppressWarnings("static-access")
	public RandomNickGui(BetterNick main) {
		this.pl = main;
	}
	
	public static Inventory randomNicksInventory(Player p) {
		Inventory inv = Bukkit.createInventory(p, 9, pl.getConfig().getString("Addons.Random Nick Gui.Inventory.Name").replace("&", "§"));
		List<String> nickslist = pl.getConfig().getStringList("NickNames List");
		Random r = new Random();
		for(int i = 0; i < 9; i++) {
			inv.setItem(i, glassPane());
		}
		inv.setItem(1, nameTag(nickslist.get(r.nextInt(nickslist.size()))));
		inv.setItem(3, nameTag(nickslist.get(r.nextInt(nickslist.size()))));
		inv.setItem(5, nameTag(nickslist.get(r.nextInt(nickslist.size()))));
		inv.setItem(7, nameTag(nickslist.get(r.nextInt(nickslist.size()))));
		return inv;
	}
	@EventHandler
	public void onRNGInvClick(InventoryClickEvent e) {
		NickedPlayer p = new NickedPlayer((Player)e.getWhoClicked());
		try {
			if(e.getInventory().getName() != null && e.getInventory().getName().equalsIgnoreCase(pl.getConfig().getString("Addons.Random Nick Gui.Inventory.Name").replace("&", "§"))) {
				e.setCancelled(true);
				if(e.getCurrentItem().getType() == Material.NAME_TAG) {
					String nameprefix = pl.getConfig().getString("Config.Display Name Prefix").replace("&", "§");
					String nametagprefix = pl.getConfig().getString("Config.Name Tag Prefix").replace("&", "§");
					String tablistprefix = pl.getConfig().getString("Config.Tablist Name Prefix").replace("&", "§");
					p.setNickName(e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§((?i)[0-9a-fk-or])", ""), nameprefix, nametagprefix, tablistprefix);
					p.closeInventory();
				}
			}
		} catch(NullPointerException ex) {}
	}
	private static ItemStack glassPane() {
		ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName("§r");
		is.setItemMeta(im);
		return is;
	}
	private static ItemStack nameTag(String nick) {
		ItemStack is = new ItemStack(Material.NAME_TAG);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(pl.getConfig().getString("Addons.Random Nick Gui.Inventory.Nick Item Name").replace("[NAME]", nick).replace("&", "§"));
		im.setLore(Arrays.asList(pl.getConfig().getString("Addons.Random Nick Gui.Inventory.Nick Item Lore").replace("&", "§")));
		is.setItemMeta(im);
		return is;
	}
}
