package net.pitsim.sync.enchants.intentionaluseless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.util.List;

public class SpeedyKill extends PitEnchant {

	public SpeedyKill() {
		super("Speedy Kill", false, ApplyType.SWORDS,
				"speedykill");
		isUseless = true;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Gain &eSpeed I &7(" + getSeconds(enchantLvl) + "s) on kill").getLore();
	}

	public int getSeconds(int enchantLvl) {
		if(enchantLvl == 1) return 4;
		return enchantLvl * 5 - 3;
	}
}