package net.pitsim.sync.enchants.intentionaluseless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.util.List;

public class Eggs extends PitEnchant {

	public Eggs() {
		super("Eggs", false, ApplyType.PANTS,
				"eggs");
		isUncommonEnchant = true;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Spawn with &a8 eggs&7. Gain &a+" + getEggs(enchantLvl), "&aeggs &7on kill").getLore();
	}

	public int getEggs(int enchantLvl) {
		if(enchantLvl == 1) return 4;
		return enchantLvl * 8 - 8;
	}
}