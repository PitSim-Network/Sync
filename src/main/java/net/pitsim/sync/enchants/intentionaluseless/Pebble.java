package net.pitsim.sync.enchants.intentionaluseless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.misc.Misc;

import java.util.List;

public class Pebble extends PitEnchant {

	public Pebble() {
		super("Pebble", false, ApplyType.PANTS,
				"pebble");
		isUncommonEnchant = true;
		isUseless = true;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		if(enchantLvl <= 2) return new ALoreBuilder("&7Picked up gold rewards &6+" + getGold(enchantLvl) + "g").getLore();
		return new ALoreBuilder("&7Picked up gold grants &6" + Misc.getHearts(getAbsorption(enchantLvl)) + " &7and",
				"&7rewards &6+" + getGold(enchantLvl) + "g").getLore();
	}

	public int getGold(int enchantLvl) {
		return enchantLvl * 10;
	}

	public int getAbsorption(int enchantLvl) {
		return enchantLvl * 2 - 4;
	}
}