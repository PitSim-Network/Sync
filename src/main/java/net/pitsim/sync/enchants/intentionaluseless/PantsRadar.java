package net.pitsim.sync.enchants.intentionaluseless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.util.List;

public class PantsRadar extends PitEnchant {

	public PantsRadar() {
		super("Pants Radar", false, ApplyType.ALL,
				"pantsradar");
		isUncommonEnchant = true;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Pants, golden swords, and enchanted",
				"&7bows drop &d+" + getChance(enchantLvl) + " &7more frequently").getLore();
	}

	public int getChance(int enchantLvl) {
		return enchantLvl * 30;
	}
}