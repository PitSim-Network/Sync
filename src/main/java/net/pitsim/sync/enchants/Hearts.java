package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.EnchantManager;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.controllers.objects.PitPlayer;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.misc.Misc;

import java.util.List;

public class Hearts extends PitEnchant {
	public static Hearts INSTANCE;

	public Hearts() {
		super("Hearts", false, ApplyType.PANTS,
				"hearts", "heart", "health");
		INSTANCE = this;
	}

	public double getExtraHealth(PitPlayer pitPlayer) {
		int enchantLvl = EnchantManager.getEnchantLevel(pitPlayer.player, this);
		return getExtraHealth(enchantLvl);
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Increase your max health by &c" + Misc.getHearts(getExtraHealth(enchantLvl))).getLore();
	}

	public double getExtraHealth(int enchantLvl) {
		if(enchantLvl == 1) return 0.5;
		return enchantLvl;
	}
}
