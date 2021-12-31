package net.pitsim.sync.commands.admin;

import dev.kyro.arcticapi.commands.ASubCommand;
import dev.kyro.arcticapi.misc.AOutput;
import net.pitsim.sync.hypixel.Loadout;
import net.pitsim.sync.hypixel.LoadoutManager;
import net.pitsim.sync.misc.Misc;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class LoadCommand extends ASubCommand {
	public LoadCommand(String executor) {
		super(executor);
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if(!(sender instanceof Player)) return;
		Player player = (Player) sender;

		if(args.size() < 2) {
			AOutput.error(player, "/load <player> <uuid>");
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

		UUID loadUUID;
		try {
			loadUUID = UUID.fromString(args.get(1));
		} catch(Exception ignored) {
			AOutput.error(player, "Invalid uuid");
			return;
		}

		Misc.clearInventory(player);
		Loadout loadout = LoadoutManager.getLoadout(loadUUID);
		loadout.partialLoad(player);
		AOutput.send(player, "&7Loaded the data of &6" + loadout.hypixelPlayer.name + " &7to the player &6" + target.getName());
	}
}
