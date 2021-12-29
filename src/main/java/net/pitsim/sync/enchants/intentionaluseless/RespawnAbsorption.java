package net.pitsim.sync.enchants.intentionaluseless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.misc.Misc;

import java.util.List;

public class RespawnAbsorption extends PitEnchant {

	public RespawnAbsorption() {
		super("Respawn: Resistance", false, ApplyType.PANTS,
				"respawnresistance");
		isUncommonEnchant = true;
		isUseless = true;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Spawn with &6" + Misc.getHearts(getAbsorption(enchantLvl)) + " &7absorption").getLore();
	}

	public int getAbsorption(int enchantLvl) {
		return enchantLvl * 10;
	}
}