package net.pitsim.sync.hypixel;

import me.nullicorn.nedit.NBTReader;
import me.nullicorn.nedit.type.NBTCompound;
import me.nullicorn.nedit.type.NBTList;
import net.pitsim.sync.controllers.KyroItems;
import net.pitsim.sync.enums.SpecialItem;
import net.pitsim.sync.exceptions.PitException;
import org.bukkit.inventory.ItemStack;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.util.*;

public class HypixelPlayer {
	public JSONObject playerObj;
	public boolean first = true;

	public UUID uuid;
	public String name;
	public int prestige;

	public int enderchestRows = 6;
	public boolean hasThick = true;
	public int heresyLevel = 3;

	public Map<Mystic, ItemLocation> mysticMap = new LinkedHashMap<>();

	public HypixelPlayer() {
	}

	public void updatePitPanda(JSONObject playerObj) {
		if(playerObj == null) return;

		if(first) {
			mysticMap.put(new Mystic(SpecialItem.GHELM), new ItemLocation(InventoryType.INVENTORY, 0));
			mysticMap.put(new Mystic(SpecialItem.ARCH_CHEST), new ItemLocation(InventoryType.INVENTORY, 1));
			mysticMap.put(new Mystic(SpecialItem.ARMA_BOOTS), new ItemLocation(InventoryType.INVENTORY, 2));
		}

		JSONArray items = playerObj.getJSONArray("items");
		for(Object item : items) {
			try {
				JSONObject jsonItem = (JSONObject) item;
				Mystic mystic = new Mystic(jsonItem);
				mysticMap.put(mystic, new ItemLocation(InventoryType.STASH, 0));
			} catch(Exception exception) {
				if(exception instanceof PitException) continue;
				exception.printStackTrace();
			}
		}

		first = false;
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
		if(uuid.equals(UUID.fromString("01acbb49-6357-4502-81ca-e79f4b31a44e")) && section.equals("inv_contents")) {
			int count = 27;
			for(ItemStack item : KyroItems.items) dataMap.put(new Mystic(item), new ItemLocation(InventoryType.INVENTORY, count++));
		}
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
						dataMap.put(new Mystic(SpecialItem.GHELM), new ItemLocation(inventoryType, j[0]));
					} else if(name.toLowerCase().contains("archangel")) {
						for(Map.Entry<Mystic, ItemLocation> entry : dataMap.entrySet())
							if(entry.getKey().specialItem != null && entry.getKey().specialItem == SpecialItem.ARCH_CHEST) return;
						dataMap.put(new Mystic(SpecialItem.ARCH_CHEST), new ItemLocation(inventoryType, j[0]));
					} else if(name.toLowerCase().contains("armageddon")) {
						for(Map.Entry<Mystic, ItemLocation> entry : dataMap.entrySet())
							if(entry.getKey().specialItem != null && entry.getKey().specialItem == SpecialItem.ARMA_BOOTS) return;
						dataMap.put(new Mystic(SpecialItem.ARMA_BOOTS), new ItemLocation(inventoryType, j[0]));
					}
				} catch(Exception ignored) { }

				Mystic mystic = new Mystic(compound);
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
