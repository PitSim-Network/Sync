package net.pitsim.sync.hypixel;

import net.pitsim.sync.hypixel.Mystic;
import me.nullicorn.nedit.NBTReader;
import me.nullicorn.nedit.type.NBTCompound;
import me.nullicorn.nedit.type.NBTList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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

		}
		getStats();
	}

	public void getStats() {

		try {

			JSONObject achievements = playerObj.getJSONObject("achievements");
			JSONObject pitData = playerObj.getJSONObject("stats").getJSONObject("Pit").getJSONObject("profile");

			prestige = achievements.has("pit_prestiges") ? achievements.getInt("pit_prestiges") : 0;

			UUID = getUUID(playerObj.getString("uuid"));
			name = playerObj.getString("displayname");

			isOnline = playerObj.getLong("lastLogout") < playerObj.getLong("lastLogin");

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

				Mystic mystic = new Mystic(this, compound);
				if(!mystic.isMystic()) return;
				enderchestMystics.put(i[0], mystic);
//				Bukkit.broadcastMessage(mystic.owner.name + " " + mystic.enchantMap);

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
}
