package net.pitsim.sync.enums;

public enum NBTTag {

	ITEM_UUID("sync-uuid"),
	PIT_NONCE("sync-nonce"),
	PIT_ENCHANTS("sync-enchants"),
	PIT_ENCHANT_ORDER("sync-enchant-order"),
	ITEM_JEWEL_ENCHANT("sync-jewel-enchant"),
	ITEM_TOKENS("sync-token-num"),
	ITEM_RTOKENS("sync-rare-token-num"),
	ITEM_ENCHANTS("sync-enchant-num"),
	PLAYER_KILLS("sync-player-kills"),
	BOT_KILLS("sync-bot-kills"),
	JEWEL_KILLS("sync-jewel-kills"),
	IS_JEWEL("sync-isjewel"),
	IS_GEMMED("sync-isgemmed"),
	UNDROPPABLE("sync-undroppable"),
	DROP_CONFIRM("sync-dropconfirm"),
	ORIGINAL_COLOR("sync-original-color"),
	CURRENT_LIVES("sync-current-lives"),
	MAX_LIVES("sync-max-lives"),
	IS_FEATHER("sync-isfeather"),
	IS_TOKEN("sync-istoken"),
	IS_VILE("sync-isvile"),
	IS_SHARD("sync-isshard"),
	IS_GEM("sync-isgem"),
	IS_GHELMET("sync-isghelm"),
	GHELMET_UUID("sync-ghelm-uuid"),
	GHELMET_GOLD("sync-ghelm-gold"),
	GHELMET_ABILITY("sync-ghelm-ability"),
	IS_YUMMY_BREAD("sync-is-yummy-bread"),
	IS_VERY_YUMMY_BREAD("sync-is-very-yummy-bread"),

	IS_VENOM("sync-isvenom");

	private final String ref;

	NBTTag(String ref) {

		this.ref = ref;
	}

	public String getRef() {
		return ref;
	}
}
