package net.pitsim.sync.commands;

import dev.kyro.arcticapi.misc.AOutput;
import dev.kyro.arcticapi.misc.ASound;
import net.pitsim.sync.PitSim;
import net.pitsim.sync.controllers.MapManager;
import net.pitsim.sync.controllers.objects.PitPlayer;
import net.pitsim.sync.hypixel.Loadout;
import net.pitsim.sync.hypixel.LoadoutManager;
import net.pitsim.sync.inventories.PremiumGUI;
import net.pitsim.sync.misc.Misc;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SyncCommand implements CommandExecutor {
	public static List<UUID> cooldownList = new ArrayList<>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) return false;
		Player player = (Player) sender;

		if(player.getWorld() != MapManager.getLobby()) {
			AOutput.error(player, "You can only use this command while in the lobby");
			return false;
		}

		if(cooldownList.contains(player.getUniqueId())) {
			AOutput.error(player, "This command is on cooldown");
			return false;
		}

		cooldownList.add(player.getUniqueId());
		new BukkitRunnable() {
			@Override
			public void run() {
				cooldownList.remove(player.getUniqueId());
			}
		}.runTaskLater(PitSim.INSTANCE, 20 * 60);

		player.closeInventory();
		Misc.clearInventory(player);
		player.teleport(MapManager.getLobbySpawn());

		if(LoadoutManager.hasHypixelPlayer(player.getUniqueId())) LoadoutManager.hypixelPlayers.remove(LoadoutManager.getHypixelPlayer(player.getUniqueId()));
		if(LoadoutManager.hasLoadout(player.getUniqueId())) LoadoutManager.loadouts.remove(LoadoutManager.getLoadout(player.getUniqueId()));

		PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
		pitPlayer.premiumGUI = new PremiumGUI(player);
		Loadout loadout = LoadoutManager.getLoadout(player.getUniqueId());
		loadout.fullLoad(player);

		ASound.play(player, Sound.LEVEL_UP, 1, 1);
		AOutput.send(player, "&6&lSYNC! &7Synced your data");
		Misc.sendTitle(player, "&6&lSYNC!", 20);
		return false;
	}
}
