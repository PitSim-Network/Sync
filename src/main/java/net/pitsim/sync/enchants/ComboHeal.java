package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.HitCounter;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.controllers.objects.PitPlayer;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import net.pitsim.sync.events.HealEvent;
import net.pitsim.sync.misc.Misc;
import net.pitsim.sync.misc.Sounds;
import org.bukkit.event.EventHandler;

import java.util.List;

public class ComboHeal extends PitEnchant {

	public ComboHeal() {
		super("Combo: Heal", false, ApplyType.SWORDS,
				"comboheal", "ch", "combo-heal", "cheal");
		isUncommonEnchant = true;
	}

	@EventHandler
	public void onAttack(AttackEvent.Apply attackEvent) {
		if(!canApply(attackEvent)) return;
		PitPlayer pitAttacker = PitPlayer.getPitPlayer(attackEvent.attacker);

		int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
		if(enchantLvl == 0) return;

		int regLvl = attackEvent.getAttackerEnchantLevel(Regularity.INSTANCE);
		if(Regularity.isRegHit(attackEvent.defender) && Regularity.skipIncrement(regLvl)) return;

		PitPlayer pitPlayer = PitPlayer.getPitPlayer(attackEvent.attacker);
		HitCounter.incrementCounter(pitPlayer.player, this);
		if(!HitCounter.hasReachedThreshold(pitPlayer.player, this, 4)) return;

		pitAttacker.heal(getHealing(enchantLvl));
		pitAttacker.heal(getHealing(enchantLvl), HealEvent.HealType.ABSORPTION, 8);

		Sounds.COMBO_PROC.play(attackEvent.attacker);
	}

	@Override
	public List<String> getDescription(int enchantLvl) {

		return new ALoreBuilder("&7Every &efourth &7strike heals",
				"&c" + Misc.getHearts(getHealing(enchantLvl)) + " &7and grants &6" + Misc.getHearts(getHealing(enchantLvl))).getLore();
	}

	public double getHealing(int enchantLvl) {

		return enchantLvl * 0.8;
	}
}
