package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.PitSim;
import net.pitsim.sync.controllers.EnchantManager;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.controllers.objects.PitPlayer;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Booboo extends PitEnchant {
	public static Booboo INSTANCE;

	public Booboo() {
		super("Boo-boo", false, ApplyType.PANTS,
				"boo-boo", "boo", "bb", "booboo");
		isUncommonEnchant = true;
		INSTANCE = this;
	}

	@EventHandler
	public void onAttack(AttackEvent.Apply attackEvent	) {
		if(!canApply(attackEvent)) return;

		int enchantLvl = attackEvent.getDefenderEnchantLevel(this);
		if(enchantLvl == 0) return;

	}
	public static int counter = 0;

	static {
		new BukkitRunnable() {
			@Override
			public void run() {
				for(Player player : Bukkit.getOnlinePlayers()) {
					int level = EnchantManager.getEnchantLevel(player, INSTANCE);
					PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
					if(ComboVenom.isVenomed(player)) continue;

					if(level == 1 && counter % 5 == 0) {
						pitPlayer.heal(2);
					} else if(level == 2 && counter % 4 == 0) {
						pitPlayer.heal(2);
					} else if(level == 3 && counter % 3 == 0) {
						pitPlayer.heal(2);
					} else if(level == 4 && counter % 2 == 0) {
						pitPlayer.heal(2);
					} else if(level > 4) {
						pitPlayer.heal(2);
					}
				}

				counter++;
			}
		}.runTaskTimer(PitSim.INSTANCE, 0L, 20L);
	}


	@Override
	public List<String> getDescription(int enchantLvl) {

		return new ALoreBuilder("&7Passively regain &c1\u2764 &7 every " + getSeconds(enchantLvl),
				"&7seconds").getLore();
	}

	public int getSeconds(int enchantLvl) {

		return Math.max(6 - enchantLvl, 1);
	}
}
