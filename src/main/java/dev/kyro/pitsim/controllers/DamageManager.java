package dev.kyro.pitsim.controllers;

import de.tr7zw.nbtapi.NBTItem;
import dev.kyro.arcticapi.misc.AOutput;
import dev.kyro.pitsim.PitSim;
import dev.kyro.pitsim.controllers.objects.PitEnchant;
import dev.kyro.pitsim.controllers.objects.PitPlayer;
import dev.kyro.pitsim.enchants.Regularity;
import dev.kyro.pitsim.enchants.Telebow;
import dev.kyro.pitsim.enchants.WolfPack;
import dev.kyro.pitsim.enums.NBTTag;
import dev.kyro.pitsim.events.AttackEvent;
import dev.kyro.pitsim.events.KillEvent;
import dev.kyro.pitsim.misc.*;
import me.clip.placeholderapi.PlaceholderAPI;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class DamageManager implements Listener {

	public static List<Player> hitCooldownList = new ArrayList<>();
	public static List<Player> hopperCooldownList = new ArrayList<>();
	public static List<Player> nonHitCooldownList = new ArrayList<>();
	public static Map<EntityShootBowEvent, Map<PitEnchant, Integer>> arrowMap = new HashMap<>();

	static {
		new BukkitRunnable() {
			@Override
			public void run() {
				List<EntityShootBowEvent> toRemove = new ArrayList<>();
				for(Map.Entry<EntityShootBowEvent, Map<PitEnchant, Integer>> entry : arrowMap.entrySet()) {

					if(entry.getKey().getProjectile().isDead()) toRemove.add(entry.getKey());
				}
				for(EntityShootBowEvent remove : toRemove) {
					arrowMap.remove(remove);
				}
			}
		}.runTaskTimer(PitSim.INSTANCE, 0L, 1L);
	}

	@EventHandler
	public void onWitherDamage(EntityDamageEvent event) {
		if(event.getCause() != EntityDamageEvent.DamageCause.WITHER || !(event.getEntity() instanceof Player)) return;
		Player player = (Player) event.getEntity();
		if(event.getFinalDamage() >= player.getHealth()) death(player);
	}

	@EventHandler
	public void onHeal(EntityRegainHealthEvent event) {
		if(!(event.getEntity() instanceof Player) || event.getRegainReason() == EntityRegainHealthEvent.RegainReason.CUSTOM) return;
		Player player = (Player) event.getEntity();
		event.setCancelled(true);

		PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
		pitPlayer.heal(event.getAmount());
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onBowShoot(EntityShootBowEvent event) {

		if(!(event.getEntity() instanceof Player) || !(event.getProjectile() instanceof Arrow)) return;
		Player shooter = (Player) event.getEntity();
		Arrow arrow = (Arrow) event.getProjectile();
		arrowMap.put(event, EnchantManager.getEnchantsOnPlayer(shooter));
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onAttack(EntityDamageByEntityEvent event) {

		if(!(event.getEntity() instanceof Player)) return;
		Player attacker = getAttacker(event.getDamager());
		Player defender = (Player) event.getEntity();

		Map<PitEnchant, Integer> defenderEnchantMap = EnchantManager.getEnchantsOnPlayer(defender);
		boolean fakeHit = false;

		if(nonHitCooldownList.contains(defender) && !Regularity.toReg.contains(defender.getUniqueId()) &&
				!(event.getDamager() instanceof Arrow)) {
			event.setCancelled(true);
			return;
		}
//		Regular player to player hit
		if(!Regularity.toReg.contains(defender.getUniqueId())) {
			fakeHit = hitCooldownList.contains(defender);
			if(hopperCooldownList.contains(defender)) {
				event.setCancelled(true);
				return;
			}
		}

		if(Regularity.regCooldown.contains(defender.getUniqueId()) && !Regularity.toReg.contains(defender.getUniqueId())) {
			event.setCancelled(true);
			return;
		}

		if(!fakeHit) {
//			if(attackingNon == null) attacker.setHealth(Math.min(attacker.getHealth() + 1, attacker.getMaxHealth()));
			hitCooldownList.add(defender);
			hopperCooldownList.add(defender);
			nonHitCooldownList.add(defender);
			new BukkitRunnable() {
				int count = 0;

				@Override
				public void run() {
					if(++count == 15) cancel();

					if(count == 8) DamageManager.hitCooldownList.remove(defender);
					if(count == 10) DamageManager.hopperCooldownList.remove(defender);
					if(count == 15) DamageManager.nonHitCooldownList.remove(defender);
				}
			}.runTaskTimer(PitSim.INSTANCE, 0L, 1L);
		}


		AttackEvent.Pre preEvent = null;
		if(event.getDamager() instanceof Player) {

			preEvent = new AttackEvent.Pre(event, EnchantManager.getEnchantsOnPlayer(attacker), defenderEnchantMap, fakeHit);
		} else if(event.getDamager() instanceof Arrow) {

			for(Map.Entry<EntityShootBowEvent, Map<PitEnchant, Integer>> entry : arrowMap.entrySet()) {

				if(!entry.getKey().getProjectile().equals(event.getDamager())) continue;
				preEvent = new AttackEvent.Pre(event, arrowMap.get(entry.getKey()), defenderEnchantMap, fakeHit);
			}
		} else if(event.getDamager() instanceof Slime) {

			preEvent = new AttackEvent.Pre(event, new HashMap<>(), defenderEnchantMap, fakeHit);
		} else if(event.getDamager() instanceof Wolf) {

			preEvent = new AttackEvent.Pre(event, new HashMap<>(), defenderEnchantMap, fakeHit);
		}
		if(preEvent == null) return;

		Bukkit.getServer().getPluginManager().callEvent(preEvent);
		if(preEvent.isCancelled()) {
			event.setCancelled(true);
			return;
		}
		AttackEvent.Apply applyEvent = new AttackEvent.Apply(preEvent);
			Bukkit.getServer().getPluginManager().callEvent(applyEvent);
//		if(applyEvent.fakeHit) {
//			applyEvent.event.setDamage(0);
//			return;
//		}
		handleAttack(applyEvent);
		Bukkit.getServer().getPluginManager().callEvent(new AttackEvent.Post(applyEvent));
	}

	public static void handleAttack(AttackEvent.Apply attackEvent) {
//		AOutput.send(attackEvent.attacker, "Initial Damage: " + attackEvent.event.getDamage());

//		As strong as iron
		if(attackEvent.defender.getInventory().getLeggings() != null && attackEvent.defender.getInventory().getLeggings().getType() == Material.LEATHER_LEGGINGS) {
			NBTItem pants = new NBTItem(attackEvent.defender.getInventory().getLeggings());
			if(pants.hasKey(NBTTag.ITEM_UUID.getRef())) {
				attackEvent.multiplier.add(0.86956521);
			}
		}
		if(attackEvent.defender.getInventory().getHelmet() != null && attackEvent.defender.getInventory().getHelmet().getType() == Material.GOLD_HELMET) {
			attackEvent.multiplier.add(0.95652173913);
		}

		double damage = attackEvent.getFinalDamage();
		attackEvent.event.setDamage(damage);

		if(attackEvent.trueDamage != 0 || attackEvent.veryTrueDamage != 0) {
			double finalHealth = attackEvent.defender.getHealth() - attackEvent.trueDamage - attackEvent.veryTrueDamage;
			if(finalHealth <= 0) {
				attackEvent.event.setCancelled(true);
				kill(attackEvent, attackEvent.attacker, attackEvent.defender, false);
				return;
			} else {
				attackEvent.defender.setHealth(Math.max(finalHealth, 0));
			}
		}

		if(attackEvent.selfTrueDamage != 0 || attackEvent.selfVeryTrueDamage != 0) {
			double finalHealth = attackEvent.attacker.getHealth() - attackEvent.selfTrueDamage - attackEvent.selfVeryTrueDamage;
			if(finalHealth <= 0) {
				attackEvent.event.setCancelled(true);
				kill(attackEvent, attackEvent.defender, attackEvent.attacker, false);
				return;
			} else {
				attackEvent.attacker.setHealth(Math.max(finalHealth, 0));
//				attackEvent.attacker.damage(0);
			}
		}

		PitPlayer pitPlayer = PitPlayer.getPitPlayer(attackEvent.defender);
		pitPlayer.addDamage(attackEvent.attacker, attackEvent.event.getFinalDamage() + attackEvent.trueDamage);

//		AOutput.send(attackEvent.attacker, "Final Damage: " + attackEvent.event.getDamage());
//		AOutput.send(attackEvent.attacker, "Final Damage: " + attackEvent.event.getFinalDamage());

		if(attackEvent.event.getFinalDamage() >= attackEvent.defender.getHealth()) {

			attackEvent.event.setCancelled(true);
			kill(attackEvent, attackEvent.attacker, attackEvent.defender, false);
		} else if(attackEvent.event.getFinalDamage() + attackEvent.executeUnder >= attackEvent.defender.getHealth()) {

			attackEvent.event.setCancelled(true);
			kill(attackEvent, attackEvent.attacker, attackEvent.defender, true);
		}

		DamageIndicator.onAttack(attackEvent);
	}

	public static Player getAttacker(Entity damager) {

		if(damager instanceof Player) return (Player) damager;
		if(damager instanceof Arrow) return (Player) ((Arrow) damager).getShooter();
//		if(damager instanceof Slime) return PitBlob.getOwner((Slime) damager);
		if(damager instanceof Wolf) return WolfPack.getOwner((Wolf) damager);

		return null;
	}

	public static void kill(AttackEvent attackEvent, Player killer, Player dead, boolean exeDeath) {

		KillEvent killEvent = new KillEvent(attackEvent, killer, dead, exeDeath);
		Bukkit.getServer().getPluginManager().callEvent(killEvent);

		PitPlayer pitAttacker = PitPlayer.getPitPlayer(killer);
		PitPlayer pitDefender = PitPlayer.getPitPlayer(dead);
		EntityPlayer nmsPlayer = ((CraftPlayer) dead).getHandle();
		nmsPlayer.setAbsorptionHearts(0);

		Telebow.teleShots.removeIf(teleShot -> teleShot.getShooter().equals(dead));

		dead.setHealth(dead.getMaxHealth());
		dead.playEffect(EntityEffect.HURT);
		Sounds.DEATH_FALL.play(dead);
		Sounds.DEATH_FALL.play(dead);
		Regularity.toReg.remove(dead.getUniqueId());

		Misc.multiKill(killer);

		Location spawnLoc = Bukkit.getWorld("lobby").getSpawnLocation();
		dead.teleport(spawnLoc);
		for(PotionEffect potionEffect : dead.getActivePotionEffects()) {
			dead.removePotionEffect(potionEffect.getType());
		}

		String kill = "&a&lKILL!&7 on %luckperms_prefix%" + "%player_name%";
		String death = "&c&lDEATH! &7by %luckperms_prefix%" + "%player_name%";
		String killActionBar = "&7%luckperms_prefix%" + "%player_name%" + " &a&lKILL!";

		AOutput.send(killEvent.killer, PlaceholderAPI.setPlaceholders(killEvent.dead, kill));
		AOutput.send(killEvent.dead, PlaceholderAPI.setPlaceholders(killEvent.killer, death));
		String actionBarPlaceholder = PlaceholderAPI.setPlaceholders(killEvent.dead, killActionBar);
		new BukkitRunnable() {
			@Override
			public void run() {

				Misc.sendActionBar(killEvent.killer, actionBarPlaceholder);
			}
		}.runTaskLater(PitSim.INSTANCE, 1L);

		double finalDamage = 0;
		for(Map.Entry<UUID, Double> entry : pitDefender.recentDamageMap.entrySet()) {

			finalDamage += entry.getValue();


			pitDefender.recentDamageMap.clear();

			String message = "%luckperms_prefix%";
		}
	}

	public static void death(Player dead) {
		Telebow.teleShots.removeIf(teleShot -> teleShot.getShooter().equals(dead));

		dead.setHealth(dead.getMaxHealth());
		dead.playEffect(EntityEffect.HURT);
		EntityPlayer nmsPlayer = ((CraftPlayer) dead).getHandle();
		nmsPlayer.setAbsorptionHearts(0);
		Sounds.DEATH_FALL.play(dead);
		Sounds.DEATH_FALL.play(dead);
		Regularity.toReg.remove(dead.getUniqueId());
		CombatManager.taggedPlayers.remove(dead.getUniqueId());

		PitPlayer pitPlayer = PitPlayer.getPitPlayer(dead);

		Location spawnLoc = Bukkit.getWorld("lobby").getSpawnLocation();
		dead.teleport(spawnLoc);

		PitPlayer pitDefender = PitPlayer.getPitPlayer(dead);

		for(PotionEffect potionEffect : dead.getActivePotionEffects()) {
			dead.removePotionEffect(potionEffect.getType());
		}
		AOutput.send(dead, "&c&lDEATH!");
		String message = "%luckperms_prefix%";

	}

	public static void fakeKill(AttackEvent attackEvent, Player killer, Player dead, boolean exeDeath) {

		KillEvent killEvent = new KillEvent(attackEvent, killer, dead, exeDeath);
		Bukkit.getServer().getPluginManager().callEvent(killEvent);

		PitPlayer pitAttacker = PitPlayer.getPitPlayer(killer);
		PitPlayer pitDefender = PitPlayer.getPitPlayer(dead);

		Misc.multiKill(killer);

		String kill = "&a&lKILL!&7 on %luckperms_prefix%" + "%player_name%";
		String killActionBar = "&7%luckperms_prefix%" + "%player_name%" + " &a&lKILL!";

		AOutput.send(killEvent.killer, PlaceholderAPI.setPlaceholders(killEvent.dead, kill));
		String actionBarPlaceholder = PlaceholderAPI.setPlaceholders(killEvent.dead, killActionBar);
		new BukkitRunnable() {
			@Override
			public void run() {

				Misc.sendActionBar(killEvent.killer, actionBarPlaceholder);
			}
		}.runTaskLater(PitSim.INSTANCE, 1L);
	}

	public static boolean isNaked(Player player) {
		if(!Misc.isAirOrNull(player.getInventory().getHelmet())) return false;
		if(!Misc.isAirOrNull(player.getInventory().getChestplate())) return false;
		if(!Misc.isAirOrNull(player.getInventory().getLeggings())) return false;
		return Misc.isAirOrNull(player.getInventory().getBoots());
	}
}
