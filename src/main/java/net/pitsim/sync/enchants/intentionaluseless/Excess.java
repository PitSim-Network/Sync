package net.pitsim.sync.enchants.intentionaluseless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.util.List;

public class Excess extends PitEnchant {

	public Excess() {
		super("Excess", false, ApplyType.PANTS,
				"excess");
		isUncommonEnchant = true;
		isUseless = true;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Can hol &a+" + getHealingItemIncrease(enchantLvl) + " healing &7item").getLore();
	}

	public int getHealingItemIncrease(int enchantLvl) {
		return enchantLvl;
	}
}