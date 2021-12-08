package net.pitsim.sync.enchants.intentionaluseless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.util.List;

public class Knockback extends PitEnchant {

	public Knockback() {
		super("Knockback", true, ApplyType.SWORDS,
				"knockback");
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Increases knockback taken by", "&7enemies by &f" + getBlocks(enchantLvl) + " blocks").getLore();
	}

	public int getBlocks(int enchantLvl) {
		return enchantLvl * 3;
	}
}