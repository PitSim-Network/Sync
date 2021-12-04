package net.pitsim.sync.enchants.useless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.util.List;

public class Revengeance extends PitEnchant {

	public Revengeance() {
		super("Revengeance", false, ApplyType.SWORDS,
				"revengeance");
	}

	@Override
	public List<String> getDescription(int enchantLvl) {

		return new ALoreBuilder("&7More useless than Minikloon").getLore();
	}
}