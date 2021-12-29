package net.pitsim.sync.enchants.intentionaluseless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.util.List;

public class Negotiator extends PitEnchant {

	public Negotiator() {
		super("Negotiator", false, ApplyType.PANTS,
				"negotiator");
		isUseless = true;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Earn &6+" + getIncrease(enchantLvl) + " gold &7from contracts").getLore();
	}

	public int getIncrease(int enchantLvl) {
		if(enchantLvl == 1) return 20;
		return enchantLvl * 40 - 20;
	}
}