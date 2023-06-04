package net.pitsim.sync.enchants;

import net.pitsim.sync.controllers.EnchantManager;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.controllers.objects.PitPlayer;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.misc.Misc;
import net.pitsim.sync.misc.PitLoreBuilder;

import java.util.List;

public class GottaGoFast extends PitEnchant {
	public static GottaGoFast INSTANCE;

	public GottaGoFast() {
		super("Gotta go fast", false, ApplyType.PANTS,
				"gotta-go-fast", "gottagofast", "gtgf", "gotta", "fast");
		INSTANCE = this;
		isUncommonEnchant = true;
	}

	public static float getWalkSpeedIncrease(PitPlayer pitPlayer) {
		int enchantLvl = EnchantManager.getEnchantLevel(pitPlayer.player, INSTANCE);
		if(enchantLvl == 0) return 0;

		return getWalkSpeedIncrease(enchantLvl);
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new PitLoreBuilder(
				"&7Move &e" + Misc.roundString(getWalkSpeedIncrease(enchantLvl)) + "&e% faster &7at all times"
		).getLore();
	}

	public static int getWalkSpeedIncrease(int enchantLvl) {
		return enchantLvl * 5 + 5;
	}
}
