package net.pitsim.sync.enchants.useless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.util.List;

public class Martyrdom extends PitEnchant {

	public Martyrdom() {
		super("Martyrdom", true, ApplyType.PANTS,
				"martyrdom");
	}

	@Override
	public List<String> getDescription(int enchantLvl) {

		return new ALoreBuilder("&7pov your ericka").getLore();
	}
}