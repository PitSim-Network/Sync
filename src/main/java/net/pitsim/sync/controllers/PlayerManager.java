package net.pitsim.sync.controllers;

import de.tr7zw.nbtapi.NBTItem;
import dev.kyro.arcticapi.misc.AOutput;
import net.pitsim.sync.PitSim;
import net.pitsim.sync.controllers.objects.PitPlayer;
import net.pitsim.sync.enums.EquipmentType;
import net.pitsim.sync.enums.NBTTag;
import net.pitsim.sync.events.AttackEvent;
import net.pitsim.sync.events.EquipmentChangeEvent;
import net.pitsim.sync.hypixel.LoadoutManager;
import net.pitsim.sync.misc.Misc;
import net.pitsim.sync.misc.PitEquipment;
import net.pitsim.sync.misc.Sounds;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.*;

public class PlayerManager implements Listener {
	public static final Map<Player, PitEquipment> previousEquipmentMap = new HashMap<>();
	private static final List<UUID> realPlayers = new ArrayList<>();

	public static void addRealPlayer(UUID uuid) {
		realPlayers.add(uuid);
	}

	public static boolean isRealPlayer(LivingEntity testPlayer) {
		if(!(testPlayer instanceof Player)) return false;
		return realPlayers.contains(testPlayer.getUniqueId());
	}

	static {
		new BukkitRunnable() {
			@Override
			public void run() {
				for(Player onlinePlayer : Bukkit.getOnlinePlayers()) ((CraftPlayer) onlinePlayer).getHandle().getDataWatcher().watch(9, (byte) 0);
			}
		}.runTaskTimer(PitSim.INSTANCE, 0L, 20);
		new BukkitRunnable() {
			@Override
			public void run() {
				for(Player onlinePlayer : Bukkit.getOnlinePlayers()) PitPlayer.getPitPlayer(onlinePlayer).save();
			}
		}.runTaskTimer(PitSim.INSTANCE, 0L, 20);

		new BukkitRunnable() {
			@Override
			public void run() {
				EnchantManager.readPlayerEnchants();
				for(Player player : Bukkit.getOnlinePlayers()) {
					PitEquipment currentEquipment = new PitEquipment(player);
					if(!previousEquipmentMap.containsKey(player)) {
						previousEquipmentMap.put(player, currentEquipment);
						continue;
					}

					PitEquipment previousEquipment = previousEquipmentMap.get(player);

					for(EquipmentType equipmentType : EquipmentType.values()) {
						ItemStack previousItem = previousEquipment.getItemStack(equipmentType);
						ItemStack currentItem = currentEquipment.getItemStack(equipmentType);
						if(previousItem.equals(currentItem)) continue;

						EquipmentChangeEvent event = new EquipmentChangeEvent(player, equipmentType, previousEquipment, currentEquipment, false);
						Bukkit.getPluginManager().callEvent(event);
					}

					previousEquipmentMap.put(player, currentEquipment);
				}
			}
		}.runTaskTimer(PitSim.INSTANCE, 0L, 1L);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
		new BukkitRunnable() {
			@Override
			public void run() {
				PitPlayer.pitPlayers.remove(pitPlayer);
			}
		}.runTaskLater(PitSim.INSTANCE, 20L);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEnderchestClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		Block block = event.getClickedBlock();

		PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);

		if(block.getType() == Material.ENDER_CHEST) {
			event.setCancelled(true);
			if(pitPlayer.loadout == null) {
				AOutput.error(player, "For some reason you do not have anything loaded");
				return;
			} else if(pitPlayer.loadout.loadoutGUI == null) {
				AOutput.error(player, "You can only make changes to your own layout");
				return;
			}

			if(player.getWorld() != MapManager.getLobby()) {
				AOutput.send(player, "&7Your layout will only save if you do this in the lobby");
			}

			pitPlayer.loadout.loadoutGUI.open();
		} else if(block.getType() == Material.ENDER_PORTAL_FRAME) {
			event.setCancelled(true);
			if(pitPlayer.loadout == null) {
				AOutput.error(player, "For some reason you do not have anything loaded");
				return;
			} else if(pitPlayer.loadout.loadoutGUI == null) {
				AOutput.error(player, "You can only make changes to your own layout");
				return;
			}

			if(player.getWorld() != MapManager.getLobby()) {
				AOutput.error(player, "You can only do this in the lobby");
				return;
			}

			pitPlayer.loadout.loadoutGUI.getHomePanel().openPanel(pitPlayer.loadout.loadoutGUI.voidMenuPanel);
		} else if(block.getType() == Material.BEACON) {
			event.setCancelled(true);

			if(player.getWorld() != MapManager.getLobby()) {
				AOutput.error(player, "You can only do this in the lobby");
				return;
			}

			pitPlayer.premiumGUI.open();
		}
	}

	@EventHandler
	public void onEquipmentChange(EquipmentChangeEvent event) {
		PitPlayer pitPlayer = event.getPitPlayer();
		pitPlayer.updateMaxHealth();
		pitPlayer.updateWalkingSpeed();
	}

	@EventHandler
	public void onHungerLoss(FoodLevelChangeEvent event) {
		event.setCancelled(true);
	}

	public static List<UUID> pantsSwapCooldown = new ArrayList<>();
	@EventHandler
	public static void onClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if(Misc.isAirOrNull(player.getItemInHand())) return;

		int firstArrow = -1; boolean multipleStacks = false; boolean hasSpace = false;
		if(player.getItemInHand().getType() == Material.BOW) {

//			NBTItem nbtItem = new NBTItem(player.getItemInHand());
//			if(nbtItem.hasKey(NBTTag.ITEM_UUID.getRef()) && !player.getItemInHand().getItemMeta().hasEnchant(Enchantment.WATER_WORKER)) {
//				ItemStack modified = player.getItemInHand();
//				modified.addUnsafeEnchantment(Enchantment.WATER_WORKER, 1);
//				ItemMeta itemMeta = modified.getItemMeta();
//				itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
//				modified.setItemMeta(itemMeta);
//				player.setItemInHand(modified);
//			}

			for(int i = 0; i < 36; i++) {
				ItemStack itemStack = player.getInventory().getItem(i);
				if(Misc.isAirOrNull(itemStack)) {
					hasSpace = true;
					continue;
				}
				if(itemStack.getType() != Material.ARROW) continue;
				if(firstArrow == -1) firstArrow = i; else {
					multipleStacks = true;
					break;
				}
			}
			if(!multipleStacks) {
				if(firstArrow == -1) {
					if(hasSpace) {
						player.getInventory().addItem(new ItemStack(Material.ARROW, 32));
					} else {
						AOutput.error(player, "Please make room in your inventory for arrows");
					}
				} else {
					player.getInventory().setItem(firstArrow, new ItemStack(Material.ARROW, 32));
				}
			}
		}

		if(player.getItemInHand().getType().toString().contains("LEGGINGS")){
			if(Misc.isAirOrNull(player.getInventory().getLeggings())) return;

			if(pantsSwapCooldown.contains(player.getUniqueId())) {

				Sounds.NO.play(player);
				return;
			}

			ItemStack held = player.getItemInHand();
			player.setItemInHand(player.getInventory().getLeggings());
			player.getInventory().setLeggings(held);

			pantsSwapCooldown.add(player.getUniqueId());
			new BukkitRunnable() {
				@Override
				public void run() {
					pantsSwapCooldown.remove(player.getUniqueId());
				}
			}.runTaskLater(PitSim.INSTANCE, 40L);
			Sounds.ARMOR_SWAP.play(player);
		}
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		new BukkitRunnable() {
			@Override
			public void run() {
				event.getPlayer().teleport(MapManager.getLobbySpawn());
			}
		}.runTaskLater(PitSim.INSTANCE, 10L);

	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		if(event.getPlayer().getLocation().getY() < 20)  {
			DamageManager.death(event.getPlayer());
		}
	}

	@EventHandler
	public void onItemDamage(PlayerItemDamageEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onAttack(AttackEvent.Apply attackEvent) {
		PitPlayer pitPlayer = PitPlayer.getPitPlayer(attackEvent.attacker);
		if(pitPlayer.loadout != null && pitPlayer.loadout.hypixelPlayer.heresyLevel >= 2) attackEvent.increasePercent += 5;

		ItemStack chestplate = attackEvent.defender.getEquipment().getChestplate();
		if(Misc.isAirOrNull(chestplate)) return;
		NBTItem nbtItem = new NBTItem(chestplate);
		if(nbtItem.hasKey(NBTTag.IS_ARCH.getRef())) attackEvent.decreasePercent += 10;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		LoadoutManager.getHypixelPlayer(player.getUniqueId());

		PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
		if(pitPlayer.loadout != null && !pitPlayer.loadout.stash.isEmpty()) {
			int stashSize = pitPlayer.loadout.stash.size();
			AOutput.send(player, "c&lSTASH!&7 You have " + stashSize + " item" + (stashSize == 1 ? "" : "s" + " in your stash"));
		}

		player.setGameMode(GameMode.SURVIVAL);
	}

	@EventHandler
	public void onJoin(PlayerSpawnLocationEvent event) {
		Player player = event.getPlayer();
		Location spawnLoc = MapManager.getLobbySpawn();
		player.teleport(spawnLoc);
	}

	@EventHandler
	public void onCraft(InventoryClickEvent event) {
		if(event.getSlot() == 80 || event.getSlot() == 81 || event.getSlot() == 82 || event.getSlot() == 83) event.setCancelled(true);
	}

	@EventHandler
	public void onJoin(AsyncPlayerPreLoginEvent event) {
		UUID playerUUID = event.getUniqueId();
		if(!realPlayers.contains(playerUUID)) addRealPlayer(playerUUID);
		Player player = Bukkit.getServer().getPlayerExact(event.getName());
		if(player == null) return;
		if(player.isOnline()) {
			event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.RED + "You are already online! \nIf you believe this is an error, try re-logging in a few seconds.");
			return;
		}
	}

	@EventHandler
	public static void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if(!player.isOp()) return;
		new BukkitRunnable() {
			@Override
			public void run() {
				Bukkit.dispatchCommand(player, "buzz exempt");
			}
		}.runTaskLater(PitSim.INSTANCE, 1L);
	}

	public static List<Player> toggledPlayers = new ArrayList<>();

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if(!event.getPlayer().isOp()) return;
		if(toggledPlayers.contains(event.getPlayer())) return;
		event.setCancelled(true);
		AOutput.error(event.getPlayer(), "&CBlock breaking disabled, run /pitsim bypass to toggle");
	}

	@EventHandler
	public void onBreak(BlockPlaceEvent event) {
		if(!event.getPlayer().isOp()) return;
		if(toggledPlayers.contains(event.getPlayer())) return;
		event.setCancelled(true);
		AOutput.error(event.getPlayer(), "&CBlock placing disabled, run /pitsim bypass to toggle");
	}
}
