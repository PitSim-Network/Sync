package net.pitsim.sync.enchants.intentionaluseless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.misc.Misc;

import java.util.List;

public class Duelist extends PitEnchant {

	public Duelist() {
		super("Duelist", false, ApplyType.SWORDS,
				"duelist");
		isUncommonEnchant = true;
		isUseless = true;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Blocking two hits from the same", "&7player empowers your next strike",
				"&7against them for &c+" + getDamage(enchantLvl) + "% &7damage", "&7and heals &c" + Misc.getHearts(getHealing(enchantLvl))).getLore();
	}

	public int getDamage(int enchantLvl) {
		if(enchantLvl == 1) return 20;
		return enchantLvl * 35 - 30;
	}

	public int getHealing(int enchantLvl) {
		return enchantLvl;
	}
}