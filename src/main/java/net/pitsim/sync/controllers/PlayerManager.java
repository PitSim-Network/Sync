package net.pitsim.sync.controllers;

import be.maximvdw.featherboard.api.FeatherBoardAPI;
import de.tr7zw.nbtapi.NBTItem;
import dev.kyro.arcticapi.misc.AOutput;
import net.pitsim.sync.PitSim;
import net.pitsim.sync.controllers.objects.PitPlayer;
import net.pitsim.sync.enums.NBTTag;
import net.pitsim.sync.events.AttackEvent;
import net.pitsim.sync.misc.Misc;
import net.pitsim.sync.misc.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//import net.kyori.adventure.audience.Audience;

public class PlayerManager implements Listener {
//	public static Map<Player, BossBarManager> bossBars = new HashMap<>();
	static {
		new BukkitRunnable() {
			@Override
			public void run() {
				for(Player onlinePlayer : Bukkit.getOnlinePlayers()) ((CraftPlayer) onlinePlayer).getHandle().getDataWatcher().watch(9, (byte) 0);
			}
		}.runTaskTimer(PitSim.INSTANCE, 0L, 20L);
	}

	public static List<UUID> pantsSwapCooldown = new ArrayList<>();
	@EventHandler
	public static void onClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if(Misc.isAirOrNull(player.getItemInHand())) return;

		int firstArrow = -1; boolean multipleStacks = false; boolean hasSpace = false;
		if(player.getItemInHand().getType() == Material.BOW) {

			NBTItem nbtItem = new NBTItem(player.getItemInHand());
			if(nbtItem.hasKey(NBTTag.ITEM_UUID.getRef()) && !player.getItemInHand().getItemMeta().hasEnchant(Enchantment.WATER_WORKER)) {
				ItemStack modified = player.getItemInHand();
				modified.addUnsafeEnchantment(Enchantment.WATER_WORKER, 1);
				ItemMeta itemMeta = modified.getItemMeta();
				itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				modified.setItemMeta(itemMeta);
				player.setItemInHand(modified);
			}

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
				event.getPlayer().teleport(Bukkit.getWorld("lobby").getSpawnLocation());
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

//		Arch chest
		attackEvent.multiplier.add(0.85);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);

		FeatherBoardAPI.resetDefaultScoreboard(event.getPlayer());
		FeatherBoardAPI.showScoreboard(event.getPlayer(), "default");
	}



	@EventHandler
	public void onJoin(PlayerSpawnLocationEvent event) {
		Player player = event.getPlayer();
		PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
		Location spawnLoc = Bukkit.getWorld("lobby").getSpawnLocation();
		player.teleport(spawnLoc);

		new BukkitRunnable() {
			@Override
			public void run() {
				Bukkit.getServer().dispatchCommand(player, "spawn");
			}
		}.runTaskLater(PitSim.INSTANCE,  10L);
	}

	@EventHandler
	public void onCraft(InventoryClickEvent event) {
		if(event.getSlot() == 80 || event.getSlot() == 81 || event.getSlot() == 82 || event.getSlot() == 83) event.setCancelled(true);
	}

	@EventHandler
	public void onJoin(AsyncPlayerPreLoginEvent event) {
		Player player = Bukkit.getServer().getPlayerExact(event.getName());
		if(player == null) return;
		if(player.isOnline()) {
			event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.RED + "You are already online! \nIf you believe this is an error, try re-logging in a few seconds.");
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
