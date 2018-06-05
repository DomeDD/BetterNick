/*
 * All rights by DomeDD (2018)
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free and as long as you mention me (DomeDD) 
 * You are NOT allowed to claim this plugin (BetterNick) as your own
 * You are NOT allowed to publish this plugin (BetterNick) or your modified version of this plugin (BetterNick)
 * 
 */
package de.domedd.betternick.addons.placeholderapi;

import org.bukkit.entity.Player;

import de.domedd.betternick.BetterNick;
import de.domedd.betternick.api.betternickapi.BetterNickAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PlaceholderAPIHook extends PlaceholderExpansion {

	
	private BetterNick pl;
	
	public PlaceholderAPIHook(BetterNick plugin) {
		this.pl =  plugin;
	}
	
	public String onPlaceholderRequest(Player p, String identifier) {
		if(p == null) {
			return "";
		}
		switch(identifier) {
		case "name":
			return BetterNickAPI.getApi().getNickName(p);
		case "prefix":
			if(BetterNickAPI.getApi().isPlayerNicked(p)) {
				return pl.getConfig().getString("Nick Options.Chat Prefix").replace("&", "§");
			} else {
				return pl.chat.getPlayerPrefix(p).replace("&", "§");
			}
		case "suffix":
			if(BetterNickAPI.getApi().isPlayerNicked(p)) {
				return pl.getConfig().getString("Nick Options.Chat Suffix").replace("&", "§");
			} else {
				return pl.chat.getPlayerSuffix(p).replace("&", "§");
			}
		}
		
		return null;
	}

	@Override
	public String getAuthor() {
		return "DomeDD";
	}

	@Override
	public String getIdentifier() {
		return "betternick";
	}

	@Override
	public String getPlugin() {
		return null;
	}

	@Override
	public String getVersion() {
		return "magic";
	}

}
