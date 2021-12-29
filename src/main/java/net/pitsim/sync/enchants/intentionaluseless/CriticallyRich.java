package net.pitsim.sync.enchants.intentionaluseless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.util.List;

public class CriticallyRich extends PitEnchant {

	public CriticallyRich() {
		super("Critically Rich", false, ApplyType.SWORDS,
				"criticallyrich");
		isUseless = true;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Earn &6+" + getGold(enchantLvl) + " &7per critical strike").getLore();
	}

	public int getGold(int enchantLvl) {
		return enchantLvl * 2;
	}
}