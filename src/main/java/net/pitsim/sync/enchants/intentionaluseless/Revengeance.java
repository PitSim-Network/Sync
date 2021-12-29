package net.pitsim.sync.enchants.intentionaluseless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.util.List;

public class Revengeance extends PitEnchant {

	public Revengeance() {
		super("Revengeance", false, ApplyType.SWORDS,
				"revengeance");
		isUseless = true;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Deal &c+ " + getDamage(enchantLvl) + "% &7damage vs. the last", "&7player who killed you").getLore();
	}

	public int getDamage(int enchantLvl) {
		if(enchantLvl == 1) return 8;
		return enchantLvl * 10 - 5;
	}
}