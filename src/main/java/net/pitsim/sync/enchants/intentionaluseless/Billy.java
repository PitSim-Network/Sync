package net.pitsim.sync.enchants.intentionaluseless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.util.List;

public class Billy extends PitEnchant {

	public Billy() {
		super("Billy", false, ApplyType.PANTS,
				"billy");
		isUseless = true;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Receive &9-" + getReduction(enchantLvl) + "% &7damage per",
				"&61,000g bounty").getLore();
	}

	public int getReduction(int enchantLvl) {
		return enchantLvl + 1;
	}
}