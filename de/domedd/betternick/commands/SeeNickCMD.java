package de.domedd.betternick.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.domedd.betternick.BetterNick;
import de.domedd.betternick.api.nickedplayer.NickedPlayer;

public class SeeNickCMD implements CommandExecutor {

	private BetterNick pl;
	
	public SeeNickCMD(BetterNick main) {
		this.pl = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdlabel, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			NickedPlayer np = new NickedPlayer((Player) sender);
			if(args.length == 0) {
				if(p.hasPermission("BetterNick.SeeNick")) {
					p.sendMessage(pl.getConfig().getString("Messages.See Nick").replace("[NAME]", np.getNickName()).replace("&", "§"));
				}
			}
		}
		return false;
	}

}
