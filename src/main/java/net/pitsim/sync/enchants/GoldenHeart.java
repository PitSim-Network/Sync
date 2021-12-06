package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.controllers.objects.PitPlayer;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.HealEvent;
import net.pitsim.sync.events.KillEvent;
import net.pitsim.sync.misc.Misc;
import org.bukkit.event.EventHandler;

import java.util.List;

public class GoldenHeart extends PitEnchant {

	public GoldenHeart() {
		super("Golden Heart", false, ApplyType.PANTS,
				"goldenheart", "golden-heart", "gheart", "golden-hearts", "goldenhearts");
		isUncommonEnchant = true;
	}

	@EventHandler
	public void onAttack(KillEvent killEvent) {

		int enchantLvl = killEvent.getKillerEnchantLevel(this);
		if(enchantLvl == 0) return;

		PitPlayer pitKiller = PitPlayer.getPitPlayer(killEvent.killer);
		pitKiller.heal(getHealing(enchantLvl), HealEvent.HealType.ABSORPTION, 12);
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7gain &6+" + Misc.getHearts(getHealing(enchantLvl)) + " &7absorption on kill",
				"&7(max &6" + Misc.getHearts(12) + "&7)").getLore();
	}

	public double getHealing(int enchantLvl) {
		return Math.floor(Math.pow(enchantLvl, 1.4));
	}
}
