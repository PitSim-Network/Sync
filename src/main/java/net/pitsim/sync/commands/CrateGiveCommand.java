package net.pitsim.sync.commands;

import dev.kyro.arcticapi.misc.AOutput;
import net.pitsim.sync.controllers.CreditManager;
import net.pitsim.sync.controllers.objects.PitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CrateGiveCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player || args.length < 2) return false;

		Player player = null;
		for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			if(!args[0].equalsIgnoreCase(onlinePlayer.getName())) continue;
			player = onlinePlayer;
			break;
		}
		if(player == null) return false;
		PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);

		String item = args[1].toLowerCase();
		int amount = args.length < 3 ? 1 : Integer.parseInt(args[2]);

		switch(item) {
			case "credit":
				CreditManager.give(pitPlayer, amount, true);
				AOutput.send(player, "&7You have received &6" + amount + " &7credit" + (amount == 1 ? "" : "s"));
				return false;
		}
		return false;
	}
}