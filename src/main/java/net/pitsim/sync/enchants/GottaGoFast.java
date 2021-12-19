package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import dev.kyro.arcticapi.events.armor.AChangeEquipmentEvent;
import net.pitsim.sync.controllers.EnchantManager;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.misc.Misc;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.List;
import java.util.Map;

public class GottaGoFast extends PitEnchant {
	public static GottaGoFast INSTANCE;

	public GottaGoFast() {
		super("Gotta go fast", false, ApplyType.PANTS,
				"gotta-go-fast", "gottagofast", "gtgf", "gotta", "fast");
		INSTANCE = this;
		isUncommonEnchant = true;
	}

	@EventHandler
	public void onArmorEquip(AChangeEquipmentEvent event) {
		Player player = event.getPlayer();

		Map<PitEnchant, Integer> enchantMap = EnchantManager.getEnchantsOnPlayer(player);
		int enchantLvl = enchantMap.getOrDefault(this, 0);

		Map<PitEnchant, Integer> oldEnchantMap = EnchantManager.getEnchantsOnPlayer(event.getPreviousArmor());
		int oldEnchantLvl = oldEnchantMap.getOrDefault(this, 0);

		if(enchantLvl != oldEnchantLvl) {
			player.setWalkSpeed(getWalkSpeed(enchantLvl));
		}
	}

//	static {
//		new BukkitRunnable() {
//			@Override
//			public void run() {
//				for(Player player : Bukkit.getOnlinePlayers()) {
//					int level = EnchantManager.getEnchantLevel(player, INSTANCE);
//					if(level != 0) {
//						player.getWorld().spigot().playEffect(player.getLocation(),
//								Effect.CLOUD, 0, 1, 0.5F, 0.5F, 0.5F,0.01F, 5, 25);
//					}
//				}
//			}
//		}.runTaskTimer(PitSim.INSTANCE, 0L, 4L);
//	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Move &e" + Misc.roundString(getWalkSpeedLore(enchantLvl)) + "&e% faster &7at all times").getLore();
	}

	public float getWalkSpeed(int enchantLvl) {
		return 0.2F + (0.2F * (getWalkSpeedLore(enchantLvl) / 100));
	}

	public float getWalkSpeedLore(int enchantLvl) {
		return (int) (Math.pow(enchantLvl, 2.3) * 1.43 + 3);
	}
}
