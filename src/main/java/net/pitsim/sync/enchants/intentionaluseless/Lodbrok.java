package net.pitsim.sync.enchants.intentionaluseless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.util.List;

public class Lodbrok extends PitEnchant {

	public Lodbrok() {
		super("Lodbrok", false, ApplyType.PANTS,
				"lodbrok");
		isUseless = true;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Increases the chance for armor", "&7pieces to drop to " + getChance(enchantLvl) + "% (normally", "&730%)").getLore();
	}

	public int getChance(int enchantLvl) {
		if(enchantLvl == 1) return 40;
		return enchantLvl * 20 + 15;
	}
}