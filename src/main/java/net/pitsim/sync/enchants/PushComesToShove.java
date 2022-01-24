package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import dev.kyro.arcticapi.misc.AUtil;
import net.pitsim.sync.controllers.Cooldown;
import net.pitsim.sync.controllers.HitCounter;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.controllers.objects.PitPlayer;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.util.Vector;

import java.util.List;

public class PushComesToShove extends PitEnchant {

	public PushComesToShove() {
		super("Push comes to shove", false, ApplyType.BOWS,
				"pushcomestoshove", "push-comes-to-shove", "pcts");
		isUncommonEnchant = true;
	}

	@EventHandler
	public void onAttack(AttackEvent.Apply attackEvent) {
		if(!canApply(attackEvent)) return;

		if(attackEvent.arrow == null) return;
		int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
		if(enchantLvl == 0) return;
		PitPlayer pitAttacker = PitPlayer.getPitPlayer(attackEvent.attacker);

		HitCounter.incrementCounter(pitAttacker.player, this);
		if(!HitCounter.hasReachedThreshold(pitAttacker.player, this, 3)) return;

		Cooldown cooldown = getCooldown(attackEvent.attacker, 20 * 20);
		if(cooldown.isOnCooldown()) return; else cooldown.reset();

		Vector velocity = attackEvent.arrow.getVelocity().normalize().multiply(getPunchMultiplier(enchantLvl) / 2.35);
		velocity.setY(0);

		attackEvent.defender.setVelocity(velocity);

	}

	@Override
	public List<String> getDescription(int enchantLvl) {

		return new ALoreBuilder("&7Every 3rd shot on a player has",
				"&bPunch " + AUtil.toRoman(getPunchLevel(enchantLvl))).getLore();
//		return new ALoreBuilder("&7Every 3rd shot on a player has",
//				"&bPunch " + AUtil.toRoman(getPunchLevel(enchantLvl)) + " &7(5s cooldown)").getLore();
	}

	public int getPunchMultiplier(int enchantLvl) {
		return (int) Math.floor(Math.pow(enchantLvl, 0.67) * 22) - 10;
	}

	public int getPunchLevel(int enchantLvl) {
		return enchantLvl * 2 + 1;
	}
}
