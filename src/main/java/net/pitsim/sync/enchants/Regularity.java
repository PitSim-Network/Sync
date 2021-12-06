package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.PitSim;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import net.pitsim.sync.misc.Misc;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Regularity extends PitEnchant {
	public static Regularity INSTANCE;

	public static List<UUID> toReg = new ArrayList<>();
	public static List<UUID> regCooldown = new ArrayList<>();

	public Regularity() {
		super("Regularity", true, ApplyType.PANTS,
				"regularity", "reg");
		meleOnly = true;
		INSTANCE = this;
	}

	@EventHandler
	public void onAttack(AttackEvent.Post attackEvent) {
		if(!canApply(attackEvent)) return;
		if(!fakeHits && attackEvent.fakeHit) return;

		if(toReg.contains(attackEvent.defender.getUniqueId())) return;

		int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
		if(enchantLvl == 0) return;

		double finalDamage = attackEvent.event.getFinalDamage();
		if(finalDamage >= maxFinalDamage(enchantLvl)) return;

		toReg.add(attackEvent.defender.getUniqueId());
		regCooldown.add(attackEvent.defender.getUniqueId());
		new BukkitRunnable() {
			@Override
			public void run() {
				if(!toReg.contains(attackEvent.defender.getUniqueId())) return;

				double damage = attackEvent.event.getOriginalDamage(EntityDamageEvent.DamageModifier.BASE);
				attackEvent.defender.setNoDamageTicks(0);
				attackEvent.defender.damage(damage * secondHitDamage(enchantLvl) / 100, attackEvent.attacker);
			}
		}.runTaskLater(PitSim.INSTANCE, 3L);
		new BukkitRunnable() {
			@Override
			public void run() {
				toReg.remove(attackEvent.defender.getUniqueId());
			}
		}.runTaskLater(PitSim.INSTANCE, 4L);
		new BukkitRunnable() {
			@Override
			public void run() {
				regCooldown.remove(attackEvent.defender.getUniqueId());
			}
		}.runTaskLater(PitSim.INSTANCE, 11L);
	}

	public static boolean isRegHit(Player defender) {

		return toReg.contains(defender.getUniqueId());
	}

	public static int secondHitDamage(int enchantLvl) {
		return enchantLvl * 15 + 30;
	}

	public static double maxFinalDamage(int enchantLvl) {
		return enchantLvl * 0.4 + 1.8;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7If the final damage of your strike", "&7deals less than &c" +
				Misc.getHearts(maxFinalDamage(enchantLvl)) + " &7damage,",
				"&7strike again in &a0.1s &7for &c" + secondHitDamage(enchantLvl) + "%", "&7damage").getLore();
	}
}
