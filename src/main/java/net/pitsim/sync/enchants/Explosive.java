package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.Cooldown;
import net.pitsim.sync.controllers.EnchantManager;
import net.pitsim.sync.controllers.SpawnManager;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.controllers.objects.PitPlayer;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import net.pitsim.sync.misc.Sounds;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

import java.util.List;

public class Explosive extends PitEnchant {

	public Explosive() {
		super("Explosive", true, ApplyType.BOWS,
				"explosive", "explo", "ex", "explode");
	}

	@EventHandler
	public void onAttack(AttackEvent.Apply attackEvent) {
		if(!canApply(attackEvent)) return;

		int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
		if(enchantLvl == 0) return;

	}

	@EventHandler
	public void onShoot(ProjectileHitEvent event) {

		if(!(event.getEntity() instanceof Arrow) || !(event.getEntity().getShooter() instanceof Player)) return;

		Arrow arrow = (Arrow) event.getEntity();
		Player shooter = (Player) arrow.getShooter();

		int enchantLvl = EnchantManager.getEnchantLevel(shooter, this);
		if(enchantLvl == 0) return;

		Cooldown cooldown = getCooldown(shooter, getCooldown(enchantLvl));
		if(cooldown.isOnCooldown()) return; else cooldown.reset();

		if(SpawnManager.isInSpawn(arrow.getLocation())) return;

		for (Entity entity : arrow.getNearbyEntities(getRange(enchantLvl), getRange(enchantLvl), getRange(enchantLvl))) {
			if(entity instanceof Player) {
				Player player = (Player) entity;

				if(SpawnManager.isInSpawn(player.getLocation())) continue;

				if(player != shooter) {

					Vector force = player.getLocation().toVector().subtract(arrow.getLocation().toVector())
							.setY(1).normalize().multiply(1.15);
						player.setVelocity(force);
				}
			}
		}

		playSound(arrow.getLocation(), enchantLvl);
		arrow.getWorld().playEffect(arrow.getLocation(), getEffect(enchantLvl),
				getEffect(enchantLvl).getData(), 100);

		PitPlayer pitPlayer = PitPlayer.getPitPlayer(shooter);
	}

	@Override
	public List<String> getDescription(int enchantLvl) {

		if(enchantLvl < 3) {
			return new ALoreBuilder("&7Arrows fo POP! (" + getCooldown(enchantLvl) / 20 + "s cooldown)").getLore();
		} else {
			return new ALoreBuilder("&7Arrows fo BOOM! (" + getCooldown(enchantLvl) / 20 + "s cooldown)").getLore();
		}

	}

	public double getRange(int enchantLvl) {

		switch(enchantLvl) {
			case 1:
			case 2:
				return 2.5;
			case 3:
				return 6;

		}
		return 0;
	}

	public void playSound(Location location, int enchantLvl) {

		switch(enchantLvl) {
			case 1:
				Sounds.EXPLOSIVE_1.play(location);
			case 2:
				Sounds.EXPLOSIVE_2.play(location);
			case 3:
				Sounds.EXPLOSIVE_3.play(location);
		}
	}

	public Effect getEffect(int enchantLvl) {

		switch(enchantLvl) {
			case 1:
				return Effect.EXPLOSION_LARGE;
			case 2:
			case 3:
				return Effect.EXPLOSION_HUGE;

		}
		return null;
	}

	public int getCooldown(int enchantLvl) {

		switch(enchantLvl) {
			case 1:
				return 100;
			case 2:
				return 60;
			case 3:
				return 100;

		}
		return 0;
	}

}
