package net.pitsim.sync.misc;

import net.minecraft.server.v1_8_R3.*;
import net.pitsim.sync.PitSim;
import net.pitsim.sync.controllers.HopperManager;
import net.pitsim.sync.controllers.PlayerManager;
import net.pitsim.sync.enchants.needtoinspect.PinDown;
import net.pitsim.sync.enums.PitEntityType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Misc {
	public static boolean isEntity(Entity entity, PitEntityType... entityTypes) {
		PitEntityType pitEntityType = getEntity(entity);
		return Arrays.asList(entityTypes).contains(pitEntityType);
	}

	public static PitEntityType getEntity(Entity entity) {
		if(!(entity instanceof LivingEntity)) return PitEntityType.NON_LIVING;
		LivingEntity livingEntity = (LivingEntity) entity;
		if(PlayerManager.isRealPlayer(livingEntity)) return PitEntityType.REAL_PLAYER;
//		if(NonManager.getNon(livingEntity) != null) return PitEntityType.NON;
		if(HopperManager.isHopper(livingEntity)) return PitEntityType.HOPPER;
//		if(DarkzoneManager.isPitMob(livingEntity)) return PitEntityType.PIT_MOB;
//		if(BossManager.isPitBoss(livingEntity)) return PitEntityType.PIT_BOSS;
		return PitEntityType.UNKNOWN;
	}

	public static String getFormattedName(Player player) {
		return getFormattedName(player.getName());
	}

	public static String getFormattedName(String name) {
		return name.endsWith("s") ? name + "'" : name + "'s";
	}

	public static UUID getUUID(UUID uuid) {
//		if(true) return UUID.fromString("0b60ba0a-60d8-4b20-ae48-487de134f069");
//		if(true) return UUID.fromString("89c731f7-a81d-440c-aceb-51507896f88a");
		return uuid;
	}

	public static String ordinalWords(int num) {

		switch(num) {
			case 1:
				return "";
			case 2:
				return " second";
			case 3:
				return " third";
			case 4:
				return " fourth";
			case 5:
				return " fifth";
		}
		return "";
	}

	public static void applyPotionEffect(Player player, PotionEffectType type, int duration, int amplifier, boolean ambient, boolean particles) {
		if(amplifier < 0) return;
		if(duration == 0) return;

		if(PinDown.attemptPin(player, type)) return;

		for(PotionEffect potionEffect : player.getActivePotionEffects()) {
			if(!potionEffect.getType().equals(type) || potionEffect.getAmplifier() > amplifier) continue;
			if(potionEffect.getAmplifier() == amplifier && potionEffect.getDuration() >= duration) continue;
			player.removePotionEffect(type);
			break;
		}
		player.addPotionEffect(new PotionEffect(type, duration, amplifier, ambient, particles));
	}

	public static void multiKill(Player player) {

		new BukkitRunnable() {
			int count = 0;
			@Override
			public void run() {

				switch(count) {
					case 0:
						Sounds.MULTI_1.play(player);
						break;
					case 1:
						Sounds.MULTI_2.play(player);
						break;
					case 2:
						Sounds.MULTI_3.play(player);
						break;
					case 3:
						Sounds.MULTI_4.play(player);
						break;
					case 4:
						Sounds.MULTI_5.play(player);
						break;
				}

				if(++count > 5) cancel();
			}
		}.runTaskTimer(PitSim.INSTANCE, 0L, 2L);
	}

	/**
	 * Rounds damage and then converts to hearts.
	 * Should only be used for displaying, not calculation.
	 */
	public static String getHearts(double damage) {

		return roundString(damage / 2) + "\u2764";
	}

	/**
	 * Rounds a number to 2 decimal places and trims extra zeros.
	 * Should only be used for displaying, not calculation.
	 */
	public static String roundString(double number) {

		return new DecimalFormat("#,##0.###").format(number);
	}

	/**
	 * Converts to multiplier
	 */
	public static double getReductionMultiplier(double reduction) {

		return Math.max(1 - (reduction / 100D), 0);
	}

	public static int linearEnchant(int level, double step, double base) {

		return (int) (level * step + base);
	}

	public static void sendActionBar(Player player, String message) {
		PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" +
				ChatColor.translateAlternateColorCodes('&', message) + "\"}"), (byte) 2);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}

	public static void sendTitle(Player player, String message, int length) {
		IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" +
				ChatColor.translateAlternateColorCodes('&', message) + "\"}");

		PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
		PacketPlayOutTitle titleLength = new PacketPlayOutTitle(5, length, 5);


		((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
	}

	public static void sendSubTitle(Player player, String message, int length) {
		IChatBaseComponent chatSubTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" +
				ChatColor.translateAlternateColorCodes('&', message) + "\"}");

		PacketPlayOutTitle subtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSubTitle);
		PacketPlayOutTitle subTitleLength = new PacketPlayOutTitle(5, length, 5);

		((CraftPlayer) player).getHandle().playerConnection.sendPacket(subtitle);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(subTitleLength);
	}

	public static boolean isCritical(Player player) {
		return player.getFallDistance() > 0.0F &&
				!player.isOnGround() &&
				!player.isInsideVehicle() &&
				!player.hasPotionEffect(PotionEffectType.BLINDNESS) &&
				player.getLocation().getBlock().getType() != Material.LADDER &&
				player.getLocation().getBlock().getType() != Material.VINE;
	}

	public static boolean isAirOrNull(ItemStack itemStack) {

		return itemStack == null || itemStack.getType() == Material.AIR;
	}

	public static String getFormattedKills(int kills) {

		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		decimalFormat.setGroupingUsed(true);
		decimalFormat.setGroupingSize(3);

		return decimalFormat.format(kills);
	}

	public static void strikeLightningForPlayers(Location location, double radius) {
		List<Player> nearbyPlayers = new ArrayList<>();
		for(Entity nearbyEntity : location.getWorld().getNearbyEntities(location, radius, radius, radius)) {
			if(!(nearbyEntity instanceof Player)) continue;
			nearbyPlayers.add((Player) nearbyEntity);
		}


		Player[] lightningPlayers = new Player[nearbyPlayers.size()];
		lightningPlayers = nearbyPlayers.toArray(lightningPlayers);
		strikeLightningForPlayers(location, lightningPlayers);
	}

	public static void strikeLightningForPlayers(Location location, Player... players) {
		World world = ((CraftWorld) location.getWorld()).getHandle();

		for(Player player : players) {
			EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
			entityPlayer.playerConnection.sendPacket(new PacketPlayOutSpawnEntityWeather(
					new EntityLightning(world, location.getX(), location.getY(), location.getZ(), false, false)));

			player.playSound(location, Sound.AMBIENCE_THUNDER, 10, 1);
			player.playSound(location, Sound.EXPLODE, 10, (float) (Math.random() * 0.2 + 0.6));
		}
	}

	public static File getFileFromResource(String fileName) throws URISyntaxException {

		ClassLoader classLoader = Misc.class.getClassLoader();
		URL resource = classLoader.getResource(fileName);
		if (resource == null) {
			return null;
		}
		return new File(resource.toURI());
	}

	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	public static void clearInventory(Player player) {
		player.getInventory().clear();
		player.getInventory().setHelmet(null);
		player.getInventory().setChestplate(null);
		player.getInventory().setLeggings(null);
		player.getInventory().setBoots(null);
		player.setItemOnCursor(new ItemStack(Material.AIR));
	}

	public static void giveDiamond(Player player) {
		if(isAirOrNull(player.getEquipment().getHelmet())) player.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
		if(isAirOrNull(player.getEquipment().getChestplate())) player.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
		if(isAirOrNull(player.getEquipment().getLeggings())) player.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
		if(isAirOrNull(player.getEquipment().getBoots())) player.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
	}

	public static long getRunnableOffset(int minutes) {
		return (long) (Math.random() * 20 * 60 * minutes);
	}
}
