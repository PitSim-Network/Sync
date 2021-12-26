package net.pitsim.sync.commands;

import dev.kyro.arcticapi.misc.AOutput;
import net.pitsim.sync.controllers.HopperManager;
import net.pitsim.sync.controllers.MapManager;
import net.pitsim.sync.controllers.objects.Hopper;
import net.pitsim.sync.inventories.HopperGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HopperGUICommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) return false;
		Player player = (Player) sender;

		if(player.getWorld() != MapManager.getFFA()) {
			AOutput.error(player, "You can only use this command in the FFA world");
			return false;
		}

		boolean hasHopperAlready = false;
		for(Hopper hopper : HopperManager.hopperList) {
			if(hopper.target == player) {
				hasHopperAlready = true;
				break;
			}
		}
		if(hasHopperAlready) {
			AOutput.error(player, "You can only have one hopper spawned on you at a time");
			return false;
		}

		new HopperGUI(player).open();
		return false;
	}
}
