package net.pitsim.sync.enchants.intentionaluseless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.util.List;

public class StrikeGold extends PitEnchant {

	public StrikeGold() {
		super("Strike Gold", false, ApplyType.ALL,
				"strikegold");
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Earn &6+" + getGold(enchantLvl) + "g &7per hit (1s cooldown)").getLore();
	}

	public int getGold(int enchantLvl) {
		return enchantLvl;
	}
}