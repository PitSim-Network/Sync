package net.pitsim.sync.enchants.newcheck;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.EnchantManager;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import net.pitsim.sync.misc.Misc;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;

public class McSwimmer extends PitEnchant {

	public McSwimmer() {
		super("McSwimmer", false, ApplyType.PANTS,
				"mcswimmer");
		isUncommonEnchant = true;
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if(event.getCause() != EntityDamageEvent.DamageCause.DROWNING || !(event.getEntity() instanceof Player)) return;
		Player player = (Player) event.getEntity();

		int enchantLvl = EnchantManager.getEnchantLevel(player, this);
		if(enchantLvl == 0) return;

		event.setCancelled(true);
	}

	@EventHandler
	public void onAttack(AttackEvent.Apply attackEvent) {
		if(!canApply(attackEvent)) return;
		int enchantLvl = attackEvent.getDefenderEnchantLevel(this);
		if(enchantLvl == 0) return;

		Block inside = attackEvent.attacker.getLocation().getBlock();
		if(inside == null) return;

		Material material = inside.getType();
		if(material != Material.LAVA && material != Material.STATIONARY_LAVA && material != Material.WATER && material != Material.STATIONARY_WATER) return;
		attackEvent.multiplier.add(Misc.getReductionMultiplier(getReduction(enchantLvl)));
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Receive &9-" + getReduction(enchantLvl) + "% &7melee damage",
				"&7while swimming in water or lava", "&7and cannot drown").getLore();
	}

	public int getReduction(int enchantLvl) {
		if(enchantLvl == 1) return 25;
		return enchantLvl * 20;
	}
}