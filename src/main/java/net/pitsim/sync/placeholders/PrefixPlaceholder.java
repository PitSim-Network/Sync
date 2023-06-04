package net.pitsim.sync.placeholders;

import dev.kyro.arcticapi.hooks.papi.APAPIPlaceholder;
import me.clip.placeholderapi.PlaceholderAPI;
import net.pitsim.sync.controllers.objects.PitPlayer;
import org.bukkit.entity.Player;

public class PrefixPlaceholder implements APAPIPlaceholder {

	@Override
	public String getIdentifier() {
		return "prefix";
	}

	@Override
	public String getValue(Player player) {

		String message = "%luckperms_prefix%";

		PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
		return pitPlayer.prefix + PlaceholderAPI.setPlaceholders(player, message);
	}
}
