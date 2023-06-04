package net.pitsim.sync.commands.admin;

import dev.kyro.arcticapi.commands.ACommand;
import dev.kyro.arcticapi.commands.AMultiCommand;
import dev.kyro.arcticapi.misc.AOutput;
import net.pitsim.sync.controllers.CreditManager;
import net.pitsim.sync.controllers.objects.PitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CreditGiveCommand extends ACommand {
	public CreditGiveCommand(AMultiCommand base, String executor) {
		super(base, executor);
	}

	@Override
	public void execute(CommandSender sender, Command command, String alias, List<String> args) {
		if(!(sender instanceof Player)) return;
		Player player = (Player) sender;

		if(args.size() < 2) {
			AOutput.error(player, "Usage: /credit <player> <amount>");
			return;
		}

		Player target = null;
		for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			if(!onlinePlayer.getName().equalsIgnoreCase(args.get(0))) continue;
			target = onlinePlayer;
			break;
		}
		if(target == null) {
			AOutput.error(player, "Could not find that player");
			return;
		}

		int credits;
		try {
			credits = Integer.parseInt(args.get(1));
		} catch(Exception ignored) {
			AOutput.error(player, "Invalid amount of credits");
			return;
		}

		PitPlayer pitPlayer = PitPlayer.getPitPlayer(target);
		CreditManager.give(pitPlayer, credits, true);
		AOutput.send(player, "&7Gave " + target.getName() + " &6" + credits + " &7credit" + (credits == 1 ? "" : "s"));
		AOutput.send(target, "&7You have received &6" + credits + " &7credit" + (credits == 1 ? "" : "s"));
	}

	@Override
	public List<String> getTabComplete(Player player, String current, List<String> args) {
		return null;
	}
}
