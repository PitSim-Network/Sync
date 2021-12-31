package net.pitsim.sync.commands.admin;

import dev.kyro.arcticapi.commands.ASubCommand;
import dev.kyro.arcticapi.misc.AOutput;
import net.pitsim.sync.controllers.objects.PitPlayer;
import net.pitsim.sync.misc.Misc;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class UnloadCommand extends ASubCommand {
	public UnloadCommand(String executor) {
		super(executor);
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if(!(sender instanceof Player)) return;
		Player player = (Player) sender;

		if(args.size() < 1) {
			AOutput.error(player, "/unload <player>");
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

		PitPlayer pitPlayer = PitPlayer.getPitPlayer(target);
		if(pitPlayer.loadout == null) {
			AOutput.error(player, "That player does not have a player connected");
			return;
		}

		pitPlayer.loadout = null;
		Misc.clearInventory(target);
		AOutput.send(player, "&7Disconnected data connected to &6" + target.getName());
	}
}
