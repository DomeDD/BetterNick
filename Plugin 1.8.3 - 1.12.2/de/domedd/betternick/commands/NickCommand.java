/*
 * All rights by DomeDD (2018)
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free and as long as you mention me (DomeDD) 
 * You are NOT allowed to claim this plugin (BetterNick) as your own
 * You are NOT allowed to publish this plugin (BetterNick) or your modified version of this plugin (BetterNick)
 * 
 */
package de.domedd.betternick.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.domedd.betternick.BetterNick;
import de.domedd.betternick.addons.randomnickgui.RandomNickGui;
import de.domedd.betternick.api.betternickapi.BetterNickAPI;
import de.domedd.betternick.api.events.PlayerCallNickEvent;
import de.domedd.betternick.api.events.PlayerCallRandomNickEvent;
import de.domedd.betternick.api.events.PlayerCallRandomSkinEvent;
import de.domedd.betternick.files.NickedPlayersFile;

public class NickCommand implements CommandExecutor {

	private BetterNick pl;
	
	public NickCommand(BetterNick main) {
		this.pl = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdlable, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(args.length == 0) {
				if(p.hasPermission("BetterNick.RandomNick")) {
					if(!pl.getConfig().getBoolean("Addons.Random Nick Gui.Enabled")) {
						Bukkit.getPluginManager().callEvent(new PlayerCallRandomNickEvent(p));
						if(pl.getConfig().getBoolean("Config.Nick And Skin Combination")) {
							Bukkit.getPluginManager().callEvent(new PlayerCallRandomSkinEvent(p));
						}
					} else {
						p.openInventory(RandomNickGui.randomNicksInventory(p));
					}
				} else {
					if(pl.getConfig().getBoolean("Messages.Enabled")) {
						p.sendMessage(pl.prefix + pl.getConfig().getString("Messages.No Permissions").replace("&", "§"));
					}
				}
			} else if(args.length == 1) {
				if(args[0].equalsIgnoreCase("reload")) {
					if(p.hasPermission("BetterNick.Reload")) {
						pl.reloadConfig();
						NickedPlayersFile.reload();
						if(pl.getConfig().getBoolean("Messages.Enabled")) {
							p.sendMessage(pl.prefix + pl.getConfig().getString("Messages.Files Reloaded").replace("&", "§"));
						}
					} else {
						if(pl.getConfig().getBoolean("Messages.Enabled")) {
							p.sendMessage(pl.prefix + pl.getConfig().getString("Messages.No Permissions").replace("&", "§"));
						}
					}
				} else if(args[0].equalsIgnoreCase("gui")) {
					if(p.hasPermission("BetterNick.OptionsGUI")) {
						openNickOptionsInv(p);
					} else {
						if(pl.getConfig().getBoolean("Messages.Enabled")) {
							p.sendMessage(pl.prefix + pl.getConfig().getString("Messages.No Permissions").replace("&", "§"));
						}
					}
				} else {
					Player t = Bukkit.getPlayer(args[0]);
					if(t != null && t.isOnline()) {
						if(!pl.getConfig().getBoolean("Addons.Random Nick Gui.Enabled")) {
							if(p.hasPermission("BetterNick.RandomNick")) {
								Bukkit.getPluginManager().callEvent(new PlayerCallRandomNickEvent(t));
							} else {
								if(pl.getConfig().getBoolean("Messages.Enabled")) {
									p.sendMessage(pl.prefix + pl.getConfig().getString("Messages.No Permissions").replace("&", "§"));
								}
							}
							if(pl.getConfig().getBoolean("Config.Nick And Skin Combination")) {
								Bukkit.getPluginManager().callEvent(new PlayerCallRandomSkinEvent(t));
							}
						} else {
							if(p.hasPermission("BetterNick.RandomNick")) {
								p.openInventory(RandomNickGui.randomNicksInventory(t));
							} else {
								if(pl.getConfig().getBoolean("Messages.Enabled")) {
									p.sendMessage(pl.prefix + pl.getConfig().getString("Messages.No Permissions").replace("&", "§"));
								}
							}
						}
					} else {
						if(p.hasPermission("BetterNick.Nick")) {
							Bukkit.getPluginManager().callEvent(new PlayerCallNickEvent(p, args[0]));
						} else {
							if(pl.getConfig().getBoolean("Messages.Enabled")) {
								p.sendMessage(pl.prefix + pl.getConfig().getString("Messages.No Permissions").replace("&", "§"));
							}
						}
					}
				}
			} else if(args.length == 2) {
				if(p.hasPermission("BetterNick.Nick")) {
					Player t = Bukkit.getPlayer(args[0]);
					if(t.isOnline()) {
						Bukkit.getPluginManager().callEvent(new PlayerCallNickEvent(t, args[1]));
					} else {
						p.sendMessage(pl.prefix + pl.getConfig().getString("Config.Messages.See Real Name Error").replace("&", "§"));
					}
				} else {
					if(pl.getConfig().getBoolean("Messages.Enabled")) {
						p.sendMessage(pl.prefix + pl.getConfig().getString("Messages.No Permissions").replace("&", "§"));
					}
				}
			}
		} else {
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("reload")) {
					pl.reloadConfig();
					NickedPlayersFile.reload();
					Bukkit.getConsoleSender().sendMessage(pl.prefix + pl.getConfig().getString("Messages.Files Reloaded").replace("&", "§"));
				}
			}
		}
		return true;
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
	private static ItemStack glassPane() {
		ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName("§r");
		is.setItemMeta(im);
		return is;
	}
	private ItemStack getItemFromString(String from) {
		int ID = 0;
	    int subID = 0;
	    String name = "";
	    if(from.contains(":")) {
	    	String[] array = from.split(":");
	    	ID = Integer.valueOf(array[0]);
	    	String a = array[1];
	    	if(a.startsWith("10") || a.startsWith("11") || a.startsWith("12") || a.startsWith("13") || a.startsWith("14") || a.startsWith("15") || a.startsWith("16") || a.startsWith("17") || a.startsWith("18") || a.startsWith("19")) {
	    		a = a.substring(0, 2);
	    	} else {
	    		a = a.substring(0, 1);
	    	}
	    	subID = Integer.valueOf(a);
	    }
	    String[] array = from.split(", ");
	    name = array[1];
		@SuppressWarnings("deprecation")
		ItemStack is = new ItemStack(ID, 1, (short) subID);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name.replace("&", "§"));
		is.setItemMeta(im);
		return is;
	}
}
