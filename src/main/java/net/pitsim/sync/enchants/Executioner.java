package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import net.pitsim.sync.events.KillEvent;
import net.pitsim.sync.misc.Misc;
import net.pitsim.sync.misc.Sounds;
import org.bukkit.Effect;
import org.bukkit.event.EventHandler;

import java.util.List;

public class Executioner extends PitEnchant {

	public Executioner() {
		super("Executioner", true, ApplyType.SWORDS,
				"executioner", "exe");
	}

	@EventHandler
	public void onKill(KillEvent killEvent) {
		if(!killEvent.exeDeath) return;

		Sounds.EXE.play(killEvent.killer);
		killEvent.dead.getWorld().playEffect(killEvent.dead.getLocation().add(0, 1, 0), Effect.STEP_SOUND, 152);
	}

	@EventHandler
	public void onAttack(AttackEvent.Apply attackEvent) {
		if(!canApply(attackEvent)) return;

		int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
		if(enchantLvl == 0) return;

		attackEvent.executeUnder = getExecuteHealth(enchantLvl);
	}

	@Override
	public List<String> getDescription(int enchantLvl) {

		return new ALoreBuilder("&7Hitting an enemy to below &c" + Misc.getHearts(getExecuteHealth(enchantLvl)),
				"&7instantly kills them").getLore();
	}

	public double getExecuteHealth(int enchantLvl) {
		return enchantLvl + 1;
	}
}
