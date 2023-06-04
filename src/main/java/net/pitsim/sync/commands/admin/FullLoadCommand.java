package net.pitsim.sync.commands.admin;

import dev.kyro.arcticapi.commands.ACommand;
import dev.kyro.arcticapi.commands.AMultiCommand;
import dev.kyro.arcticapi.misc.AOutput;
import net.pitsim.sync.hypixel.Loadout;
import net.pitsim.sync.hypixel.LoadoutManager;
import net.pitsim.sync.inventories.loadout.LoadoutGUI;
import net.pitsim.sync.misc.Misc;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class FullLoadCommand extends ACommand {
	public FullLoadCommand(AMultiCommand base, String executor) {
		super(base, executor);
	}

	@Override
	public void execute(CommandSender sender, Command command, String alias, List<String> args) {
		if(!(sender instanceof Player)) return;
		Player player = (Player) sender;

		if(args.size() < 2) {
			AOutput.error(player, "/fullload <player> <uuid>");
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

		Misc.clearInventory(target);
		Loadout loadout = LoadoutManager.getLoadout(loadUUID);
		loadout.loadoutGUI = new LoadoutGUI(target, loadout);
		loadout.partialLoad(target);
		AOutput.send(player, "&7Loaded the data of &6" + loadout.hypixelPlayer.name + " &7to the player &6" + target.getName());
	}

	@Override
	public List<String> getTabComplete(Player player, String current, List<String> args) {
		return null;
	}
}
