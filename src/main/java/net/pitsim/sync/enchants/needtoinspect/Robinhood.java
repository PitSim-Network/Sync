package net.pitsim.sync.enchants.needtoinspect;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.PitSim;
import net.pitsim.sync.controllers.EnchantManager;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import net.pitsim.sync.events.VolleyShootEvent;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.*;

public class Robinhood extends PitEnchant {
	public static Map<Arrow, Integer> robinMap = new HashMap<>();

	public Robinhood() {
		super("Robinhood", true, ApplyType.BOWS,
				"robinhood", "robin");
	}

	@EventHandler
	public void onAttack(AttackEvent.Apply attackEvent) {
		if(!canApply(attackEvent) || attackEvent.arrow == null) return;
		if(!robinMap.containsKey(attackEvent.arrow)) return;

		attackEvent.decreasePercent += getReduction(robinMap.get(attackEvent.arrow));
	}

	@EventHandler
	public void onHit(ProjectileHitEvent event) {
		if(!(event.getEntity() instanceof Arrow)) return;
		new BukkitRunnable() {
			@Override
			public void run() {
				robinMap.remove((Arrow) event.getEntity());
			}
		}.runTaskLater(PitSim.INSTANCE, 1L);
	}

	@EventHandler
	public void onBowShoot(EntityShootBowEvent event) {

		if(!(event.getEntity() instanceof Player) || !(event.getProjectile() instanceof Arrow)) return;
		Player player = ((Player) event.getEntity()).getPlayer();
		Arrow arrow = (Arrow) event.getProjectile();
		if(event instanceof VolleyShootEvent) return;

		if(!arrow.isCritical()) return;

		int enchantLvl = EnchantManager.getEnchantLevel(player, this);
		if(enchantLvl == 0) return;
		robinMap.put(arrow, enchantLvl);

		new BukkitRunnable() {
			@Override
			public void run() {
				if(!robinMap.containsKey(arrow)) {
					cancel();
					return;
				}

				Map.Entry<Player, Double> targetInfo = null;
				for(Entity nearbyEntity : arrow.getWorld().getNearbyEntities(arrow.getLocation(), 16, 16, 16)) {

					if(!(nearbyEntity instanceof Player) || nearbyEntity.equals(player)) continue;
					Player target = (Player) nearbyEntity;
					double distance = arrow.getLocation().distance(target.getLocation());
					if(targetInfo == null || distance < targetInfo.getValue()) {
						targetInfo = new AbstractMap.SimpleEntry<>(target, distance);
					}
				}

				if(targetInfo == null) return;

				Vector arrowVector = arrow.getLocation().toVector();
				Vector targetVector = targetInfo.getKey().getLocation().toVector();
				targetVector.setY(targetVector.getY() + 2);

				Vector direction = targetVector.subtract(arrowVector).normalize();
				arrow.setVelocity(direction);
			}
		}.runTaskTimer(PitSim.INSTANCE, 0L, 1L);
	}

	public double getRange(int enchantLvl) {
		return enchantLvl * 0.5 + 0.5;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		DecimalFormat format = new DecimalFormat("0.#");
		return new ALoreBuilder("&7Your shots &ehome &7from &e" + format.format(getRange(enchantLvl)) + " &7block" + (getRange(enchantLvl) == 1 ? "" : "s"),
				"&7away and deal &c-" + getReduction(enchantLvl) + "% &7damage", "&7(3s cooldown)").getLore();
	}

	public int getReduction(int enchantLvl) {
		return Math.max(70 - enchantLvl * 10, 0);
	}
}
