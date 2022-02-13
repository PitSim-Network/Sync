package net.pitsim.sync.hypixel;

import de.tr7zw.nbtapi.NBTItem;
import dev.kyro.arcticapi.misc.AOutput;
import net.pitsim.sync.PitSim;
import net.pitsim.sync.controllers.DuelManager;
import net.pitsim.sync.controllers.HopperManager;
import net.pitsim.sync.controllers.MapManager;
import net.pitsim.sync.controllers.PremiumItem;
import net.pitsim.sync.controllers.objects.PitPlayer;
import net.pitsim.sync.enums.NBTTag;
import net.pitsim.sync.enums.PremiumType;
import net.pitsim.sync.events.KillEvent;
import net.pitsim.sync.events.OofEvent;
import net.pitsim.sync.misc.Misc;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LoadoutManager implements Listener {
	public static List<HypixelPlayer> hypixelPlayers = new ArrayList<>();
	public static List<Loadout> loadouts = new ArrayList<>();

	static {
		new BukkitRunnable() {
			@Override
			public void run() {
				for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
					if(onlinePlayer.getWorld() != MapManager.getLobby()) continue;
					PitPlayer pitPlayer = PitPlayer.getPitPlayer(onlinePlayer);
					int premiumItems = pitPlayer.premiumGUI.premiumItems.size();
					int totalCredits = getPremiumCost(onlinePlayer);
					if(premiumItems == 0) continue;
					Misc.sendActionBar(onlinePlayer, "&fUsing &6" + premiumItems + " &fpremium item" + (premiumItems == 1 ? "" : "s") +
							" costing &6" + totalCredits + " &fcredit" + (totalCredits == 1 ? "" : "s"));
				}
			}
		}.runTaskTimer(PitSim.INSTANCE, 0L, 20 * 5);
	}

	public static void load(Player player) {
		if(HopperManager.isHopper(player)) return;

		PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
		if(pitPlayer.loadout != null) {
			pitPlayer.loadout.partialLoad(player);
			return;
		}

		Loadout loadout = getLoadout(player.getUniqueId());
		loadout.partialLoad(player);
	}

	public static void loadPremium(Player player) {
		PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
		for(Map.Entry<Integer, PremiumItem> entry : pitPlayer.premiumGUI.premiumItemMap.entrySet()) {
			int slot = entry.getKey();
			ItemStack itemStack = entry.getValue().getItemStack();
			switch(slot) {
				case -4:
					player.getEquipment().setBoots(itemStack);
					continue;
				case -3:
					player.getEquipment().setLeggings(itemStack);
					continue;
				case -2:
					player.getEquipment().setChestplate(itemStack);
					continue;
				case -1:
					player.getEquipment().setHelmet(itemStack);
					continue;
				default:
					player.getInventory().setItem(slot, itemStack);
			}
		}
	}

	public static void deathPremium(Player player) {
		PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
		if(pitPlayer.premiumGUI.premiumItems.size() == 0) return;
		int creditCost = LoadoutManager.getPremiumCost(pitPlayer.player);
		pitPlayer.credits -= creditCost;
		AOutput.send(player, "&7Premium items costed &6" + creditCost + " &7credit" + (creditCost == 1 ? "" : "s"));
		if(pitPlayer.credits < creditCost) {
			if(PremiumItem.isPremium(player.getEquipment().getBoots())) player.getEquipment().setBoots(new ItemStack(Material.AIR));
			if(PremiumItem.isPremium(player.getEquipment().getLeggings())) player.getEquipment().setLeggings(new ItemStack(Material.AIR));
			if(PremiumItem.isPremium(player.getEquipment().getChestplate())) player.getEquipment().setChestplate(new ItemStack(Material.AIR));
			if(PremiumItem.isPremium(player.getEquipment().getHelmet())) player.getEquipment().setHelmet(new ItemStack(Material.AIR));
			for(int i = 0; i < 36; i++) {
				ItemStack itemStack = player.getInventory().getItem(i);
				if(!PremiumItem.isPremium(itemStack)) continue;
				player.getInventory().setItem(i, new ItemStack(Material.AIR));
			}
			player.updateInventory();
			AOutput.error(player, "Not enough funds to continue using premium items");
			pitPlayer.premiumGUI.premiumItems.clear();
			pitPlayer.premiumGUI.premiumItemMap.clear();
		}
	}

	public static void save(Player player) {
		PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);

//		Premium item save
		pitPlayer.premiumGUI.premiumItemMap.clear();
		for(int i = -4; i < 36; i++) {
			ItemStack itemStack;
			switch(i) {
				case -4:
					itemStack = player.getEquipment().getBoots();
					break;
				case -3:
					itemStack = player.getEquipment().getLeggings();
					break;
				case -2:
					itemStack = player.getEquipment().getChestplate();
					break;
				case -1:
					itemStack = player.getEquipment().getHelmet();
					break;
				default:
					itemStack = player.getInventory().getItem(i);
			}
			if(Misc.isAirOrNull(itemStack)) continue;
			NBTItem nbtItem = new NBTItem(itemStack);
			if(!nbtItem.hasKey(NBTTag.PREMIUM_TYPE.getRef())) continue;
			PremiumType premiumType = PremiumType.getType(nbtItem.getString(NBTTag.PREMIUM_TYPE.getRef()));
			PremiumItem premiumItem = null;
			for(PremiumItem item : pitPlayer.premiumGUI.premiumItems) {
				if(item.premiumType != premiumType) continue;
				premiumItem = item;
				break;
			}
			if(premiumItem == null) {
				System.out.println("premium item was null");
				continue;
			}
			pitPlayer.premiumGUI.premiumItemMap.put(i, premiumItem);
		}

		if(pitPlayer.loadout == null || pitPlayer.loadout.loadoutGUI == null) {
			System.out.println("YOU SHOULD NEVER SAVE THIS");
			return;
		}
		pitPlayer.loadout.save();
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		Loadout loadout = getLoadout(player.getUniqueId());
		loadout.fullLoad(player);
	}

	@EventHandler
	public void onDeath(KillEvent killEvent) {
		if(HopperManager.isHopper(killEvent.dead)) return;
		if(DuelManager.getMatch(killEvent.dead) != null) return;
		Misc.clearInventory(killEvent.dead);
		LoadoutManager.load(killEvent.dead);
		killEvent.dead.teleport(MapManager.getLobbySpawn());
		deathPremium(killEvent.dead);
	}

	@EventHandler
	public void onDeath(OofEvent event) {
		Player player = event.getPlayer();
		if(DuelManager.getMatch(player) != null) return;
		Misc.clearInventory(player);
		LoadoutManager.load(player);
		player.teleport(MapManager.getLobbySpawn());
		deathPremium(player);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if(DuelManager.getMatch(player) != null) return;
		Misc.clearInventory(player);
		player.teleport(MapManager.getLobbySpawn());
		deathPremium(player);
	}

	public static boolean hasHypixelPlayer(UUID uuid) {
		for(HypixelPlayer hypixelPlayer : hypixelPlayers) {
			if(hypixelPlayer == null || hypixelPlayer.uuid == null) continue;
			if(hypixelPlayer.uuid.equals(uuid)) return true;
		}
		return false;
	}

	public static HypixelPlayer getHypixelPlayer(UUID uuid) {
		for(HypixelPlayer hypixelPlayer : hypixelPlayers) {
			if(hypixelPlayer == null || hypixelPlayer.uuid == null) continue;
			if(hypixelPlayer.uuid.equals(uuid)) return hypixelPlayer;
		}
//		HypixelPlayer hypixelPlayer = new HypixelPlayer(HypixelAPI.request(uuid));
		HypixelPlayer hypixelPlayer = new HypixelPlayer();
		hypixelPlayer.uuid = uuid;
		hypixelPlayer.updatePitPanda(PitPandaAPI.request(uuid.toString(), 0));
		hypixelPlayer.updatePitPanda(PitPandaAPI.request(uuid.toString(), 1));
		hypixelPlayer.updatePitPanda(PitPandaAPI.request(uuid.toString(), 2));
		hypixelPlayer.updatePitPanda(PitPandaAPI.request(uuid.toString(), 3));
		hypixelPlayer.updatePitPanda(PitPandaAPI.request(uuid.toString(), 4));
		hypixelPlayers.add(hypixelPlayer);
		return hypixelPlayer;
	}

	public static boolean hasLoadout(UUID uuid) {
		for(Loadout loadout : loadouts) {
			if(loadout.uuid.equals(uuid)) return true;
		}
		return false;
	}

	public static Loadout getLoadout(UUID uuid) {
		for(Loadout loadout : loadouts) {
			if(loadout.uuid.equals(uuid)) return loadout;
		}
		Loadout loadout = new Loadout(uuid);
		loadouts.add(loadout);
		return loadout;
	}

	public static int getPremiumCost(Player player) {
		int cost = 0;
		PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
		for(PremiumItem premiumItem : pitPlayer.premiumGUI.premiumItems) {
			cost += premiumItem.cost;
		}
		return cost;
	}
}
