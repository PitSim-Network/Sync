package net.pitsim.sync.enchants.intentionaluseless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.util.List;

public class DevilChicks extends PitEnchant {

	public DevilChicks() {
		super("Devil Chicks!", true, ApplyType.BOWS,
				"devilchicks");
		isUseless = true;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		if(enchantLvl == 1) return new ALoreBuilder("&7Arrows spawn an explosive chicken").getLore();
		if(enchantLvl == 2) return new ALoreBuilder("&7Arrows spawn many explosive", "&7chickens").getLore();
		return new ALoreBuilder("&7Arrows spawn too many explosive", "&7chickens").getLore();
	}
}