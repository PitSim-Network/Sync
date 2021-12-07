package net.pitsim.sync.enchants.intentionaluseless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.util.List;

public class Paparazzi extends PitEnchant {

	public Paparazzi() {
		super("Paparazzi", true, ApplyType.PANTS,
				"paparazzi");
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Earn &d+" + getRewards(enchantLvl) + " major event &7bonus",
				"&7rewards. Consume &c2 lives &of", "&7this item per event.").getLore();
	}

	public int getRewards(int enchantLvl) {
		return enchantLvl * 50;
	}
}