package net.pitsim.sync.builders;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;

public class PitLoreBuilder extends ALoreBuilder {

	public PitEnchant pitEnchant;

	public PitLoreBuilder(PitEnchant pitEnchant) {
		this.pitEnchant = pitEnchant;
	}
}
