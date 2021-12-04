package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.Cooldown;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.controllers.objects.PitPlayer;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.util.Vector;

import java.util.List;

public class Pullbow extends PitEnchant {

	public Pullbow() {
		super("Pullbow", true, ApplyType.BOWS,
				"pullbow", "pull");
	}

	@EventHandler
	public void onAttack(AttackEvent.Apply attackEvent) {
		if(!canApply(attackEvent)) return;

		int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
		if(enchantLvl == 0) return;

		Cooldown cooldown = getCooldown(attackEvent.attacker, 160);
		if(cooldown.isOnCooldown()) return; else cooldown.reset();

		PitPlayer pitDefender = PitPlayer.getPitPlayer(attackEvent.defender);
		Vector dirVector = attackEvent.attacker.getLocation().toVector().subtract(attackEvent.defender.getLocation().toVector()).setY(0);
		Vector pullVector = dirVector.clone().normalize().setY(0.5).multiply(2.5).add(dirVector.clone().multiply(0.03));
		attackEvent.defender.setVelocity(pullVector.multiply(getMultiplier(enchantLvl)));

		PitPlayer pitAttacker = PitPlayer.getPitPlayer(attackEvent.attacker);
	}

	@Override
	public List<String> getDescription(int enchantLvl) {

		return new ALoreBuilder("&7Hitting a player pulls them toward", "&7you (8s cooldown)").getLore();
	}

	public static double getMultiplier(int enchantLvl) {

		return (enchantLvl * 0.2) + 1.15;
	}
}
