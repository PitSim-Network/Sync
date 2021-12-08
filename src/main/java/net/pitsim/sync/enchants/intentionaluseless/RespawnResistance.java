package net.pitsim.sync.enchants.intentionaluseless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import dev.kyro.arcticapi.misc.AUtil;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.misc.Misc;

import java.util.List;

public class RespawnResistance extends PitEnchant {

	public RespawnResistance() {
		super("Respawn: Resistance", false, ApplyType.PANTS,
				"respawnresistance");
		isUncommonEnchant = true;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Spawn with &9Resistance " + AUtil.toRoman(getAmplifier(enchantLvl) + 1) +
				" &7(" + getSeconds(enchantLvl) + "s)").getLore();
	}

	public int getSeconds(int enchantLvl) {
		return enchantLvl * 10 + 10;
	}

	public int getAmplifier(int enchantLvl) {
		return Misc.linearEnchant(enchantLvl, 0.5, 1);
	}
}