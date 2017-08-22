package BetterNick.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import BetterNick.Main;

public class PlayerDeath implements Listener {
	
	private Main pl;
	public PlayerDeath(Main main) {
		this.pl = main;
	}
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		if(pl.getConfig().getBoolean("Config.Skin Self Update")) {
			e.setDeathMessage(null);
		}
	}
}
