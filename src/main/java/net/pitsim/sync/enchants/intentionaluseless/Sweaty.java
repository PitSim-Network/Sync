package net.pitsim.sync.enchants.intentionaluseless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.util.List;

public class Sweaty extends PitEnchant {

	public Sweaty() {
		super("Sweaty", false, ApplyType.ALL,
				"sweaty");
		isUseless = true;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		if(enchantLvl == 1) {
			return new ALoreBuilder("&b+" + getIncrease(enchantLvl) + " XP &7from streak XP bonus").getLore();
		}
		return new ALoreBuilder("&7Earn &b+" + getIncrease(enchantLvl) + " &7XP from streak XP",
				"&7bonus and &b+" + getCapIncrease(enchantLvl) + " XP &7 per kill").getLore();
	}

	public int getIncrease(int enchantLvl) {
		return enchantLvl * 20;
	}

	public int getCapIncrease(int enchantLvl) {
		return enchantLvl * 50 - 50;
	}
}