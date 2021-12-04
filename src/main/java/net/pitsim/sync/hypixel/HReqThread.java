package net.pitsim.sync.hypixel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HReqThread extends Thread {

	public static List<UUID> toRequest = new ArrayList<>();
	public static List<UUID> requested = new ArrayList<>();

	public static int totalRequested = 0;

	@Override
	public void run() {

		if(toRequest.isEmpty()) {
			sleepThread();
			return;
		}

		new Thread(() -> {
			HypixelPlayer hypixelPlayer = new HypixelPlayer(HypixelAPI.request(toRequest.remove(0)));
			requested.add(hypixelPlayer.UUID);
			if(hypixelPlayer.name != null) totalRequested++;
		}).start();

		sleepThread();
	}

	public void sleepThread() {
		int time = 500 / APIKeys.apiKeys.size() + 10;

		try {
			Thread.sleep(time);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		new HReqThread().start();
	}
}
