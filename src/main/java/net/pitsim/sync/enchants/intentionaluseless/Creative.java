package net.pitsim.sync.enchants.intentionaluseless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.util.List;

public class Creative extends PitEnchant {

	public Creative() {
		super("Creative", false, ApplyType.PANTS,
				"creative");
		isUncommonEnchant = true;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Spawn with &f" + getSpawnPlanks(enchantLvl) + " planks&7. Wood",
				"&7remains for 30 seconds. Gain &f+" + getKillPlanks(enchantLvl), "&fblocks &fon kill").getLore();
	}

	public int getSpawnPlanks(int enchantLvl) {
		return enchantLvl * 16;
	}

	public int getKillPlanks(int enchantLvl) {
		return enchantLvl * 6;
	}
}