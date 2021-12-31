package net.pitsim.sync.placeholders;

import dev.kyro.arcticapi.hooks.APAPIPlaceholder;
import net.pitsim.sync.controllers.CreditManager;
import net.pitsim.sync.controllers.objects.PitPlayer;
import org.bukkit.entity.Player;

public class CreditPlaceholder implements APAPIPlaceholder {

	@Override
	public String getIdentifier() {
		return "credits_full";
	}

	@Override
	public String getValue(Player player) {
		PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
		String chatColor;
		if(pitPlayer.credits == 0) {
			chatColor = "&c";
		} else if(pitPlayer.credits < CreditManager.LOW_CREDIT_THRESHOLD) {
			chatColor = "&6";
		} else {
			chatColor = "&a";
		}
		return chatColor + pitPlayer.credits + " &7/ " + pitPlayer.getMaxCredits();
	}
}
