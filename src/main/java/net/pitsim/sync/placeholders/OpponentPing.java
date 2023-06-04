package net.pitsim.sync.placeholders;

import dev.kyro.arcticapi.hooks.papi.APAPIPlaceholder;
import net.pitsim.sync.controllers.DuelManager;
import net.pitsim.sync.controllers.objects.Match;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class OpponentPing implements APAPIPlaceholder {

	@Override
	public String getIdentifier() {
		return "opponent_ping";
	}

	@Override
	public String getValue(Player player) {
		if(DuelManager.getMatch(player) == null) return null;
		Match match = DuelManager.getMatch(player);
		assert match != null;
		Player opponent;
		if(player == match.player1) opponent = match.player2;
		else opponent = match.player1;

		int ping = ((CraftPlayer) opponent).getHandle().ping;

		if(ping < 51) return ChatColor.GREEN + "" +  ping + "ms";
		if(ping < 151) return ChatColor.YELLOW + "" +  ping + "ms";
		return ChatColor.RED + "" +  ping + "ms";

	}
}
