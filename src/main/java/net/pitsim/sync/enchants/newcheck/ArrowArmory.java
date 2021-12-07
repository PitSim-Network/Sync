package net.pitsim.sync.enchants.newcheck;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.PitSim;
import net.pitsim.sync.controllers.EnchantManager;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import net.pitsim.sync.misc.Misc;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ArrowArmory extends PitEnchant {
	public List<Arrow> trackedArrows = new ArrayList<>();

	public ArrowArmory() {
		super("Arrow Armory", false, ApplyType.BOWS,
				"arrowarmory", "armory");
		isUncommonEnchant = true;
	}

	@EventHandler
	public void onAttack(AttackEvent.Apply attackEvent) {
		if(!canApply(attackEvent)) return;

		int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
		if(enchantLvl == 0) return;

		for(Arrow trackedArrow : trackedArrows) {
			if(!trackedArrow.equals(attackEvent.arrow)) continue;
			attackEvent.increasePercent += getIncrease(enchantLvl) / 100.0;
			break;
		}
	}

	@EventHandler
	public void onShoot(EntityShootBowEvent event) {
		if(!(event.getEntity() instanceof Player) || !(event.getProjectile() instanceof Arrow)) return;
		Player player = (Player) event.getEntity();
		int enchantLvl = EnchantManager.getEnchantLevel(player, this);
		if(enchantLvl == 0) return;

		int arrowsToRemove = getArrows(enchantLvl) - 1;
		for(int i = 0; i < 36; i++) {
			ItemStack itemStack = player.getInventory().getItem(i);
			if(Misc.isAirOrNull(itemStack) || itemStack.getType() != Material.ARROW) continue;
			if(itemStack.getAmount() > arrowsToRemove) {
				itemStack.setAmount(itemStack.getAmount() - arrowsToRemove);
				player.getInventory().setItem(i, itemStack);
				player.updateInventory();
				break;
			} else if(itemStack.getAmount() == arrowsToRemove) {
				player.getInventory().setItem(i, new ItemStack(Material.AIR));
				player.updateInventory();
				break;
			} else {
				arrowsToRemove -= itemStack.getAmount();
				player.getInventory().setItem(i, new ItemStack(Material.AIR));
			}
		}

		trackedArrows.add((Arrow) event.getProjectile());
		new BukkitRunnable() {
			@Override
			public void run() {
				trackedArrows.remove((Arrow) event.getProjectile());
			}
		}.runTaskLater(PitSim.INSTANCE, 20 * 10);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onHit(ProjectileHitEvent event) {
		if(!(event.getEntity() instanceof Arrow) || !(event.getEntity().getShooter() instanceof Player)) return;
		for(Arrow trackedArrow : trackedArrows) {
			if(trackedArrow != event.getEntity()) continue;
			new BukkitRunnable() {
				@Override
				public void run() {
					trackedArrows.remove(trackedArrow);
				}
			}.runTaskLater(PitSim.INSTANCE, 1);
			break;
		}
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Deals &c+" + getIncrease(enchantLvl) + "&7damage but uses &f" + getArrows(enchantLvl),
				"&farrows &7per shot, if available").getLore();
	}

	public int getIncrease(int enchantLvl) {
		if(enchantLvl == 1) return 12;
		return enchantLvl * 35 - 35;
	}

	public int getArrows(int enchantLvl) {
		if(enchantLvl == 1) return 3;
		return enchantLvl * 3 - 1;
	}
}