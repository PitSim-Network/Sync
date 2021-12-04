package net.pitsim.sync.enchants.useless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.util.List;

public class SpeedyKill extends PitEnchant {

	public SpeedyKill() {
		super("Speedy Kill", false, ApplyType.SWORDS,
				"speedykill");
	}

	@Override
	public List<String> getDescription(int enchantLvl) {

		return new ALoreBuilder("&7More useless than Minikloon").getLore();
	}
}