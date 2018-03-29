/*
 * All rights by DomeDD (2018)
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free and as long as you mention me (DomeDD) 
 * You are NOT allowed to claim this plugin (BetterNick) as your own
 * You are NOT allowed to publish this plugin (BetterNick) or your modified version of this plugin (BetterNick)
 * 
 */
package de.domedd.betternick.packets;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.mojang.authlib.properties.Property;

import de.domedd.betternick.BetterNick;
import de.domedd.betternick.api.betternickapi.BetterNickAPI;
import net.minecraft.server.v1_9_R1.EnumDifficulty;
import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.Packet;
import net.minecraft.server.v1_9_R1.PacketPlayOutChat;
import net.minecraft.server.v1_9_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_9_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_9_R1.PacketPlayOutRespawn;
import net.minecraft.server.v1_9_R1.WorldSettings.EnumGamemode;
import net.minecraft.server.v1_9_R1.WorldType;

public class v1_9_R1 implements Listener {
	
	private static BetterNick pl;
	
	@SuppressWarnings("static-access")
	public v1_9_R1(BetterNick main) {
		this.pl = main;
	}
	
	public static void removeProfileProperties(Player player, String property) {
		((CraftPlayer) player).getProfile().getProperties().removeAll(property);
	}
	public static void putProfileProperties(Player player, String property, Collection<Property> properties) {
		((CraftPlayer) player).getProfile().getProperties().putAll(property, properties);
	}
	public static void setNameField(Player player, String name) throws IllegalArgumentException, IllegalAccessException {
		pl.nameField.set(((CraftPlayer) player).getProfile(), name);
	}
	public static void addToTablist(Player player) {
		if(player.isOnline()) {
			PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer) player).getHandle());
			sendPacket(packet);
		}
	}
	public static void removeFromTablist(Player player) {
		PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) player).getHandle());
		sendPacket(packet);
	}
	public static void respawn(Player player) {
		@SuppressWarnings("deprecation")
		PacketPlayOutRespawn packet = new PacketPlayOutRespawn(((CraftPlayer) player).getWorld().getEnvironment().getId(), EnumDifficulty.getById(((CraftPlayer) player).getWorld().getDifficulty().getValue()), WorldType.getType(((CraftPlayer) player).getWorld().getWorldType().getName()), EnumGamemode.getById(((CraftPlayer) player).getGameMode().getValue()));
		sendPacket(player, packet);
	}
	public static void sendActionBar(Player player, String message) {
		if(player.isOnline()) {
			PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}"), (byte)2);
		    sendPacket(player, packet);
		} else {
			BetterNickAPI.getApi().stopPlayerActionbar(player);
		}
	}
	
	private static void sendPacket(Packet<?> packet) {
		for(Player all : Bukkit.getOnlinePlayers()) {
			((CraftPlayer)all).getHandle().playerConnection.sendPacket(packet);
		}
	}
	private static void sendPacket(Player player, Packet<?> packet) {
		((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
	}
}