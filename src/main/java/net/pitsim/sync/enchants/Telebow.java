package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.PitSim;
import net.pitsim.sync.controllers.*;
import net.pitsim.sync.controllers.Cooldown;
import net.pitsim.sync.controllers.EnchantManager;
import net.pitsim.sync.controllers.SpawnManager;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.controllers.objects.PitPlayer;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import net.pitsim.sync.misc.Misc;
import net.pitsim.sync.misc.Sounds;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Telebow extends PitEnchant {

	public static List<Arrow> teleShots = new ArrayList<>();

	public Telebow() {
		super("Telebow", true, ApplyType.BOWS,
				"telebow", "tele");
	}

	@EventHandler
	public void onAttack(AttackEvent.Apply attackEvent) {
		if(!canApply(attackEvent)) return;
		int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
		if(enchantLvl == 0) return;
		if(attackEvent.arrow == null) return;

		Cooldown cooldown = getCooldown(attackEvent.attacker, getCooldown(enchantLvl) * 20);
		cooldown.reduceCooldown(60);

		if(cooldown.isOnCooldown()) {
			new BukkitRunnable() {
				@Override
				public void run() {
					Misc.sendActionBar(attackEvent.attacker, "&eTelebow: &c" + cooldown.getTicksLeft() / 20 + "&cs cooldown!");
				}
			}.runTaskLater(PitSim.INSTANCE, 1L);
		} else {
			new BukkitRunnable() {
				@Override
				public void run() {
					Misc.sendActionBar(attackEvent.attacker, "&eTelebow: &aReady!");
				}
			}.runTaskLater(PitSim.INSTANCE, 1L);
		}
	}

	static {
		new BukkitRunnable() {
			@Override
			public void run() {
				for(Arrow arrow : teleShots) {
					for(int j = 0; j < 10; j++)
						arrow.getWorld().playEffect(arrow.getLocation(), Effect.POTION_SWIRL, 0, 30);
				}
			}
		}.runTaskTimer(PitSim.INSTANCE, 0L, 1L);
	}

	@EventHandler(ignoreCancelled = true)
	public void onBowShoot(EntityShootBowEvent event) {

		if(!(event.getEntity() instanceof Player) || !(event.getProjectile() instanceof Arrow)) return;

		Player player = ((Player) event.getEntity()).getPlayer();
		Arrow arrow = (Arrow) event.getProjectile();

		int enchantLvl = EnchantManager.getEnchantLevel(player, this);
		if(enchantLvl == 0 || !player.isSneaking()) return;


		Cooldown cooldown = getCooldown(player, getCooldown(enchantLvl) * 20);
		if(cooldown.isOnCooldown()) {


			if(player.isSneaking()) Misc.sendActionBar(player, "&eTelebow: &c" + cooldown.getTicksLeft() / 20 + "&cs cooldown!");

			return;
		}
		if(cooldown.isOnCooldown()) return; else cooldown.reset();

		if(player.isSneaking() && !SpawnManager.isInSpawn(player.getLocation())) {
			teleShots.add(arrow);
		}
	}

	@EventHandler
	public void onHit(ProjectileHitEvent event) {
		if(!(event.getEntity() instanceof Arrow) || !(event.getEntity().getShooter() instanceof Player)) return;
		Player player = (Player) event.getEntity().getShooter();

		if(teleShots.size() == 0) return;
		try {
			for(Arrow teleShot : teleShots) {
				if(teleShot.equals(event.getEntity())) {

					Arrow teleArrow = (Arrow) event.getEntity();
					if(teleArrow.equals(teleShot)) {

						Location teleportLoc = teleArrow.getLocation().clone();
						teleportLoc.setYaw(-teleArrow.getLocation().getYaw());
						teleportLoc.setPitch(-teleArrow.getLocation().getPitch());


						player.teleport(teleportLoc);
						Sounds.TELEBOW.play(teleArrow.getLocation());

						teleShots.remove(teleShot);

						PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
						return;
					}
				}
			}
		} catch(Exception ignored) {}
	}

	@Override
	public List<String> getDescription(int enchantLvl) {

		return new ALoreBuilder("&7Sneak to shoot a teleportation &f", "&7arrow (" + getCooldown(enchantLvl) + "s cooldown, -3s per bow", "&7hit)").getLore();
	}

	public static int getCooldown(int enchantLvl) {

		switch(enchantLvl) {
			case 1:
				return 90;
			case 2:
				return 45;
			case 3:
				return 20;
			case 4:
				return 10;
			case 5:
				return 3;
			case 6:
				return 1;
		}

		return 0;
	}
}

