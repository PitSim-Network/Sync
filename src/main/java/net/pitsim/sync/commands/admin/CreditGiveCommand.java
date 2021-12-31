package net.pitsim.sync.commands.admin;

import dev.kyro.arcticapi.commands.ASubCommand;
import dev.kyro.arcticapi.misc.AOutput;
import net.pitsim.sync.controllers.CreditManager;
import net.pitsim.sync.controllers.objects.PitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CreditGiveCommand extends ASubCommand {
	public CreditGiveCommand(String executor) {
		super(executor);
		List<String> aliases = new ArrayList<>();
		aliases.add("credit");
		setAliases(aliases);
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
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
		AOutput.send(player, "&7Gave " + target.getName() + " &6" + credits + " &7credits");
		AOutput.send(target, "&7You have received &6" + credits + " &7credits");
	}
}
