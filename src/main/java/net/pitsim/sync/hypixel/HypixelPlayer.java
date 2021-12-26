package net.pitsim.sync.hypixel;

import me.nullicorn.nedit.NBTReader;
import me.nullicorn.nedit.type.NBTCompound;
import me.nullicorn.nedit.type.NBTList;
import net.pitsim.sync.enums.SpecialItem;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.util.*;

public class HypixelPlayer {
	public JSONObject playerObj;

	public UUID uuid;
	public String name;
	public int prestige;

	public int enderchestRows = 6;
	public boolean hasThick = true;

	public Map<Mystic, ItemLocation> mysticMap = new LinkedHashMap<>();
	public List<SpecialItem> specialItems = new ArrayList<>();

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
	}

	public void getStats() {
		try {
			JSONObject achievements = playerObj.getJSONObject("achievements");
			JSONObject pitData = playerObj.getJSONObject("stats").getJSONObject("Pit").getJSONObject("profile");

			prestige = achievements.has("pit_prestiges") ? achievements.getInt("pit_prestiges") : 0;
			uuid = getUUID(playerObj.getString("uuid"));
			name = playerObj.getString("displayname");

			mysticMap.putAll(getDataSection(pitData, "inv_contents", InventoryType.INVENTORY));
			mysticMap.putAll(getDataSection(pitData, "inv_enderchest", InventoryType.ENDERCHEST));
			mysticMap.putAll(getDataSection(pitData, "inv_armor", InventoryType.ARMOR));
			mysticMap.putAll(getDataSection(pitData, "item_stash", InventoryType.STASH));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public Map<Mystic, ItemLocation> getDataSection(JSONObject pitData, String section, InventoryType inventoryType) {
		Map<Mystic, ItemLocation> dataMap = new HashMap<>();
		if(!pitData.has(section)) return dataMap;
		try {
			JSONArray encodedInv = pitData.getJSONObject(section).getJSONArray("data");
			String[] stringArrInInv = encodedInv.toString().replaceAll("\\[", "").replaceAll("]", "").split(",");
			byte[] byteArrInv = new byte[stringArrInInv.length];
			for(int j = 0; j < stringArrInInv.length; j++) {
				String string = stringArrInInv[j];
				byteArrInv[j] = Byte.parseByte(string);
			}
			NBTList nbtListInv = NBTReader.read(new ByteArrayInputStream(byteArrInv)).getList("i");
			final int[] j = {-1};
			nbtListInv.forEachCompound(compound -> {
				j[0]++;
				if(compound.isEmpty()) return;

				try {
					String name = compound.getCompound("tag").getCompound("display").getString("Name", "");
					if(name.toLowerCase().contains("golden")) {
						for(Map.Entry<Mystic, ItemLocation> entry : dataMap.entrySet())
								if(entry.getKey().specialItem != null && entry.getKey().specialItem == SpecialItem.GHELM) return;
						dataMap.put(new Mystic(this, SpecialItem.GHELM), new ItemLocation(inventoryType, j[0]));
					} else if(name.toLowerCase().contains("archangel")) {
						for(Map.Entry<Mystic, ItemLocation> entry : dataMap.entrySet())
							if(entry.getKey().specialItem != null && entry.getKey().specialItem == SpecialItem.ARCH_CHEST) return;
						dataMap.put(new Mystic(this, SpecialItem.ARCH_CHEST), new ItemLocation(inventoryType, j[0]));
					} else if(name.toLowerCase().contains("armageddon")) {
						for(Map.Entry<Mystic, ItemLocation> entry : dataMap.entrySet())
							if(entry.getKey().specialItem != null && entry.getKey().specialItem == SpecialItem.ARMA_BOOTS) return;
						dataMap.put(new Mystic(this, SpecialItem.ARMA_BOOTS), new ItemLocation(inventoryType, j[0]));
					}
				} catch(Exception ignored) { }

				Mystic mystic = new Mystic(this, compound);
				if(!mystic.isMystic()) return;
				dataMap.put(mystic, new ItemLocation(inventoryType, j[0]));
			});
			return dataMap;
		} catch(Exception e) {
			return dataMap;
		}
	}

	private UUID getUUID(String unformattedUUID) {
		return UUID.fromString(unformattedUUID.replaceFirst(
				"(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"));
	}

	public static boolean isMystic(NBTCompound data) {
		return !data.containsKey("tag") || !data.getCompound("tag").containsKey("ExtraAttributes") ||
				!data.getCompound("tag").getCompound("ExtraAttributes").containsKey("Nonce");
	}

	public static class ItemLocation {
		public InventoryType inventoryType;
		public int slot;

		public ItemLocation(InventoryType inventoryType, int slot) {
			this.inventoryType = inventoryType;
			this.slot = slot;
		}
	}

	public enum InventoryType {
		INVENTORY,
		ENDERCHEST,
		ARMOR,
		STASH
	}
}
