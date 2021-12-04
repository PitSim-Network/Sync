package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.KillEvent;
import org.bukkit.event.EventHandler;

import java.util.List;

public class Moctezuma extends PitEnchant {

	public Moctezuma() {
		super("Moctezuma", false, ApplyType.ALL,
				"moctezuma", "moct", "moc");
		levelStacks = true;
	}

	@EventHandler
	public void onKill(KillEvent killEvent) {

		int enchantLvl = killEvent.getKillerEnchantLevel(this);
		if(enchantLvl == 0) return;

		killEvent.goldReward += getGoldIncrease(enchantLvl);
	}

	@Override
	public List<String> getDescription(int enchantLvl) {

		return new ALoreBuilder("&7Earn &6+" + getGoldIncrease(enchantLvl) + "g &7on kill (assists", "&7excluded)").getLore();
	}

	public int getGoldIncrease(int enchantLvl) {

		return enchantLvl * 6;
	}
}
