/*
 * All rights by DomeDD (2019)
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free and as long as you mention me (DomeDD) 
 * You are NOT allowed to claim this plugin (BetterNick) as your own
 * You are NOT allowed to publish this plugin (BetterNick) or your modified version of this plugin (BetterNick)
 * 
 */
package de.domedd.betternick.addons.cloudnet;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.bridge.CloudServer;
import de.dytanic.cloudnet.bridge.event.bukkit.BukkitPlayerUpdateEvent;

public class CloudNetHook implements Listener {
	
	@EventHandler
    public void updatePlayer(BukkitPlayerUpdateEvent e) {
        if (Bukkit.getPlayer(e.getCloudPlayer().getUniqueId()) != null && e.getCloudPlayer().getServer() != null && e.getCloudPlayer().getServer().equalsIgnoreCase(CloudAPI.getInstance().getServerId())) {
            CloudServer.getInstance().updateNameTags(Bukkit.getPlayer(e.getCloudPlayer().getUniqueId()));
        }
    }
}
