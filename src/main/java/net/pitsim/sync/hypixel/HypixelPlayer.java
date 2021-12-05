package net.pitsim.sync.hypixel;

import me.nullicorn.nedit.NBTReader;
import me.nullicorn.nedit.type.NBTCompound;
import me.nullicorn.nedit.type.NBTList;
import net.pitsim.sync.commands.FreshCommand;
import net.pitsim.sync.controllers.EnchantManager;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.controllers.objects.PitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.zip.GZIPInputStream;

public class HypixelPlayer {

	public static List<HypixelPlayer> hypixelPlayers = new ArrayList<>();

	private JSONObject playerObj;

	public UUID UUID;
	public String name;
	public boolean isOnline;
	public int prestige;
	public Map<Integer, Mystic> enderchestMystics = new HashMap<>();
	public Map<Integer, Mystic> inventoryMystics = new HashMap<>();

	public List<Integer> recentKills = new ArrayList<>();

	public static HypixelPlayer getHypixelPlayer(Player player) {
		for(HypixelPlayer hypixelPlayer : hypixelPlayers) {
			if(hypixelPlayer.name.equals(player.getName())) return hypixelPlayer;
		}
		return null;
	}

	public HypixelPlayer(UUID UUID) {

		this.UUID = UUID;
	}

	public HypixelPlayer(JSONObject playerObj) {

		update(playerObj);
	}

	public void update(JSONObject playerObj) {

		try {
			this.playerObj = playerObj.getJSONObject("player");
		} catch(Exception ignored) {
			ignored.printStackTrace();
		}
		getStats();
		setEchestItems();
		setInventoryMystics();
	}

	public void getStats() {

		try {

			JSONObject achievements = playerObj.getJSONObject("achievements");
			JSONObject pitData = playerObj.getJSONObject("stats").getJSONObject("Pit").getJSONObject("profile");

			prestige = achievements.has("pit_prestiges") ? achievements.getInt("pit_prestiges") : 0;

			UUID = getUUID(playerObj.getString("uuid"));
			name = playerObj.getString("displayname");

//			isOnline = playerObj.getLong("lastLogout") < playerObj.getLong("lastLogin");

			JSONArray encoded = pitData.getJSONObject("inv_enderchest").getJSONArray("data");
//			JSONArray encoded = pitData.getJSONObject("inv_contents").getJSONArray("data");
			String[] stringArr = encoded.toString().replaceAll("\\[", "").replaceAll("]", "").split(",");
			byte[] byteArr = new byte[stringArr.length];
			for(int i = 0; i < stringArr.length; i++) {
				String string = stringArr[i];
				byteArr[i] = Byte.parseByte(string);
			}

			InputStream inputStream = new ByteArrayInputStream(byteArr);
			NBTCompound nbtCompound = NBTReader.read(inputStream);
//			System.out.println(nbtCompound);

			NBTList nbtList = nbtCompound.getList("i");
			assert nbtList != null;
			final int[] i = {-1};
			nbtList.forEachCompound(compound -> {
				i[0]++;
				if(compound.isEmpty()) return;

				if(isMystic(compound)) {
					Bukkit.broadcastMessage("isMystic");
					Mystic mystic = new Mystic(this, compound);
					if(!mystic.isMystic()) return;
					enderchestMystics.put(i[0], mystic);
				}
			});



			JSONArray encodedInv = pitData.getJSONObject("inv_contents").getJSONArray("data");
//			JSONArray encodedInv = pitData.getJSONObject("inv_contents").getJSONArray("data");
			String[] stringArrInInv = encodedInv.toString().replaceAll("\\[", "").replaceAll("]", "").split(",");
			byte[] byteArrInv= new byte[stringArrInInv.length];
			for(int j = 0; j < stringArrInInv.length; j++) {
				String string = stringArrInInv[j];
				byteArrInv[j] = Byte.parseByte(string);
			}

			InputStream  inputStreamInv = new ByteArrayInputStream(byteArrInv);
			NBTCompound nbtCompoundInv = NBTReader.read(inputStreamInv);
//			System.out.println(nbtCompound);

			NBTList nbtListInv = nbtCompoundInv.getList("i");
			assert nbtListInv != null;
			final int[] j = {-1};
			nbtListInv.forEachCompound(compound -> {
				j[0]++;
				if(compound.isEmpty()) return;

				Mystic mystic = new Mystic(this, compound);
				if(!mystic.isMystic()) return;
				inventoryMystics.put(j[0], mystic);

			});
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public JSONObject getPlayerObj() {
		return playerObj;
	}

	public int getRecentKills() {

		if(recentKills.isEmpty()) return 0;
		return recentKills.get(recentKills.size() - 1) - recentKills.get(0);
	}

	private UUID getUUID(String unformattedUUID) {

		return java.util.UUID.fromString(
				unformattedUUID
						.replaceFirst(
								"(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
						)
		);
	}

	public void jsonCode(String inputString) throws IOException {
		String jsonFormatOutput = decompressGzip(decodeBase64String(inputString));
		System.out.println(jsonFormatOutput);
	}

	public byte[] decodeBase64String(String string){
		return Base64.getDecoder().decode(string);
	}

	public String decompressGzip(byte[] compressed) throws IOException {
		final int BUFFER_SIZE = 32;
		ByteArrayInputStream byteArrayStream = new ByteArrayInputStream(compressed);
		GZIPInputStream gzipStream = new GZIPInputStream(byteArrayStream, BUFFER_SIZE);
		StringBuilder string = new StringBuilder();
		byte[] data = new byte[BUFFER_SIZE];
		int bytesRead;
		while ((bytesRead = gzipStream.read(data)) != -1) {
			string.append(new String(data, 0, bytesRead));
		}
		gzipStream.close();
		byteArrayStream.close();
		return string.toString();
	}

	public void setEchestItems() {
		Player player = null;

		for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			if(onlinePlayer.getName().equals(this.name)) player = onlinePlayer;
		}

		if(player == null) return;
		PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);

		for(Map.Entry<Integer, Mystic> item : this.enderchestMystics.entrySet()) {

			Map<Enchant, Integer> enchantMap = item.getValue().enchantMap;
			Map<PitEnchant, Integer> updatedEnchantMap = new HashMap<>();

			for(Map.Entry<Enchant, Integer> enchant : enchantMap.entrySet()) {
				for(PitEnchant pitEnchant : EnchantManager.pitEnchants) {
					if(pitEnchant.refNames.contains(enchant.getKey().getDisplayName())) {
						updatedEnchantMap.put(pitEnchant, enchant.getValue());
					}
				}
			}

			if(updatedEnchantMap.size() > 0 && updatedEnchantMap.size() < 4) {
				String mysticString = item.getValue().type.displayName.equals("Pants") ? item.getValue().color.refName : item.getValue().type.displayName;
				ItemStack mystic = FreshCommand.getFreshItem(mysticString);

				try {
					for(Map.Entry<PitEnchant, Integer> newEnchant : updatedEnchantMap.entrySet()) {
						mystic = EnchantManager.addEnchant(mystic, EnchantManager.getEnchant(newEnchant.getKey().refNames.get(0)), newEnchant.getValue(), false);
					}

				} catch(Exception e) {
					e.printStackTrace();
				}
				pitPlayer.enderchestMystics.put(item.getKey(), mystic);
			}
		}
	}

	public void setInventoryMystics() {
		Player player = null;

		for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			if(onlinePlayer.getName().equals(this.name)) player = onlinePlayer;
		}

		if(player == null) return;
		PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);

		for(Map.Entry<Integer, Mystic> item : this.inventoryMystics.entrySet()) {

			Map<Enchant, Integer> enchantMap = item.getValue().enchantMap;
			Map<PitEnchant, Integer> updatedEnchantMap = new HashMap<>();

			for(Map.Entry<Enchant, Integer> enchant : enchantMap.entrySet()) {
				for(PitEnchant pitEnchant : EnchantManager.pitEnchants) {
					if(pitEnchant.refNames.contains(enchant.getKey().getDisplayName())) {
						updatedEnchantMap.put(pitEnchant, enchant.getValue());
					}
				}
			}

			if(updatedEnchantMap.size() > 0 && updatedEnchantMap.size() < 4) {
				String mysticString = item.getValue().type.displayName.equals("Pants") ? item.getValue().color.refName : item.getValue().type.displayName;
				ItemStack mystic = FreshCommand.getFreshItem(mysticString);

				try {
					for(Map.Entry<PitEnchant, Integer> newEnchant : updatedEnchantMap.entrySet()) {
						mystic = EnchantManager.addEnchant(mystic, EnchantManager.getEnchant(newEnchant.getKey().refNames.get(0)), newEnchant.getValue(), false);
					}

				} catch(Exception e) {
					e.printStackTrace();
				}
				pitPlayer.inventoryMystics.put(item.getKey(), mystic);
			}
		}
	}

	public static boolean isMystic(NBTCompound data) {
		if(!data.containsKey("tag")) return false;
		if(!data.getCompound("tag").containsKey("ExtraAttributes")) return false;
		NBTCompound attributes = data.getCompound("tag").getCompound("ExtraAttributes");
		return attributes.containsKey("Nonce");
	}
}
