package net.pitsim.sync.commands;

import dev.kyro.arcticapi.misc.AOutput;
import net.pitsim.sync.controllers.objects.PitPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VoidCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) return false;
		Player player = (Player) sender;

		PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
		if(pitPlayer.loadout == null) {
			AOutput.error(player, "For some reason you do not have anything loaded");
			return false;
		} else if(pitPlayer.loadout.loadoutGUI == null) {
			AOutput.error(player, "You can only make changes to your own layout");
			return false;
		}

		pitPlayer.loadout.loadoutGUI.getHomePanel().openPanel(pitPlayer.loadout.loadoutGUI.voidMenuPanel);
		return false;
	}
}
