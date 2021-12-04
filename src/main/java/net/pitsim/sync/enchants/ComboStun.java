package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.Cooldown;
import net.pitsim.sync.controllers.HitCounter;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.controllers.objects.PitPlayer;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import net.pitsim.sync.misc.Misc;
import net.pitsim.sync.misc.Sounds;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class ComboStun extends PitEnchant {

	public ComboStun() {
		super("Combo: Stun", true, ApplyType.SWORDS,
				"combostun", "stun", "combo-stun", "cstun");
	}

	@EventHandler
	public void onAttack(AttackEvent.Apply attackEvent) {
		if(!canApply(attackEvent)) return;

		int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
		if(enchantLvl == 0) return;

		int regLvl = attackEvent.getAttackerEnchantLevel(Regularity.INSTANCE);
		if(Regularity.isRegHit(attackEvent.defender) && Regularity.skipIncrement(regLvl)) return;

		PitPlayer pitPlayer = PitPlayer.getPitPlayer(attackEvent.attacker);
		HitCounter.incrementCounter(pitPlayer.player, this);
		if(!HitCounter.hasReachedThreshold(pitPlayer.player, this, 5)) return;

		Cooldown cooldown = getCooldown(attackEvent.attacker, 8 * 20);
		if(cooldown.isOnCooldown()) return; else cooldown.reset();

		Misc.applyPotionEffect(attackEvent.defender, PotionEffectType.SLOW, (int) getDuration(enchantLvl) * 20, 7, true, false);
		Misc.applyPotionEffect(attackEvent.defender, PotionEffectType.JUMP, (int) getDuration(enchantLvl) * 20, 254, true, false);
		Misc.applyPotionEffect(attackEvent.defender, PotionEffectType.SLOW_DIGGING, (int) getDuration(enchantLvl) * 20, 99, true, false);

		Misc.sendTitle(attackEvent.defender, "&cSTUNNED", (int) getDuration(enchantLvl) * 20);
		Misc.sendSubTitle(attackEvent.defender, "&eYou cannot move!", (int) getDuration(enchantLvl) * 20);

		Sounds.COMBO_STUN.play(attackEvent.attacker);
		Sounds.COMBO_STUN.play(attackEvent.defender);
	}

	@Override
	public List<String> getDescription(int enchantLvl) {

		return new ALoreBuilder("&7The &efifth &7strike on an enemy", "&7stuns them for " + getDuration(enchantLvl) + " &7seconds",
				"&7&o(Can only be stunned every 8s)").getLore();
	}

	public double getDuration(int enchantLvl) {
		return enchantLvl * 0.3 + 0.5;
	}
}
