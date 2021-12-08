package net.pitsim.sync.enchants.intentionaluseless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.util.List;

public class XPBoost extends PitEnchant {

	public XPBoost() {
		super("XP Boost", false, ApplyType.ALL,
				"xpboost");
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Earn &b+" + getIncrease(enchantLvl) + "% XP &7from kills").getLore();
	}

	public int getIncrease(int enchantLvl) {
		return enchantLvl * 10;
	}
}