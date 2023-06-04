package net.pitsim.sync.placeholders;

import dev.kyro.arcticapi.hooks.papi.APAPIPlaceholder;
import me.clip.placeholderapi.PlaceholderAPI;
import net.pitsim.sync.controllers.DuelManager;
import net.pitsim.sync.controllers.objects.Match;
import org.bukkit.entity.Player;

public class OpponentName implements APAPIPlaceholder {

	@Override
	public String getIdentifier() {
		return "opponent_name";
	}

	@Override
	public String getValue(Player player) {
		if(DuelManager.getMatch(player) == null) return null;
		Match match = DuelManager.getMatch(player);
		assert match != null;
		if(player == match.player1) return PlaceholderAPI.setPlaceholders(match.player2, "%luckperms_prefix%" + match.player2.getName());
		else return PlaceholderAPI.setPlaceholders(match.player1, "%luckperms_prefix%" + match.player1.getName());
	}
}
