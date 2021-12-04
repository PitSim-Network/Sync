package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.controllers.objects.PitPlayer;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.KillEvent;
import net.pitsim.sync.misc.Misc;
import org.bukkit.event.EventHandler;

import java.util.List;

public class Guts extends PitEnchant {

	public Guts() {
		super("Guts", false, ApplyType.SWORDS,
				"guts", "gut");
	}

	@EventHandler
	public void onAttack(KillEvent killEvent) {

		int enchantLvl = killEvent.getKillerEnchantLevel(this);
		if(enchantLvl == 0) return;

		PitPlayer pitPlayer = PitPlayer.getPitPlayer(killEvent.killer);
		pitPlayer.heal(getHealing(enchantLvl));
	}

	@Override
	public List<String> getDescription(int enchantLvl) {

		return new ALoreBuilder("&7Heal &c+" + Misc.getHearts(getHealing(enchantLvl)) + " &7on kill").getLore();
	}

	public double getHealing(int enchantLvl) {
		return enchantLvl * 0.5 + 0.5;
	}
}
