package net.pitsim.sync.placeholders;

import dev.kyro.arcticapi.hooks.papi.APAPIPlaceholder;
import net.pitsim.sync.controllers.DuelManager;
import net.pitsim.sync.controllers.objects.Match;
import net.pitsim.sync.misc.Misc;
import org.bukkit.entity.Player;

public class OpponentHealth implements APAPIPlaceholder {

	@Override
	public String getIdentifier() {
		return "opponent_health";
	}

	@Override
	public String getValue(Player player) {
		if(DuelManager.getMatch(player) == null) return null;
		Match match = DuelManager.getMatch(player);
		assert match != null;
		Player opponent;
		if(player == match.player1) opponent = match.player2;
		else opponent = match.player1;

		if(match.deadPlayer != null && match.deadPlayer == opponent) return "&c&lDEAD";

		return " &c" + (int) Misc.round(opponent.getHealth() / 2, 1) + "&7/&c" + (int) Misc.round(opponent.getMaxHealth() / 2, 1) + "\u2764";
	}
}
