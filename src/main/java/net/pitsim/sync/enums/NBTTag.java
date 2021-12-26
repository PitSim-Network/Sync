package net.pitsim.sync.enums;

public enum NBTTag {

	ITEM_UUID("sync-uuid"),
	PIT_NONCE("sync-nonce"),
	PIT_ENCHANTS("sync-enchants"),
	PIT_ENCHANT_ORDER("sync-enchant-order"),
	ITEM_TOKENS("sync-token-num"),
	ITEM_RTOKENS("sync-rare-token-num"),
	ITEM_ENCHANTS("sync-enchant-num"),
	IS_GEMMED("sync-isgemmed"),
	UNDROPPABLE("sync-undroppable"),
	DROP_CONFIRM("sync-dropconfirm"),
	CURRENT_LIVES("sync-current-lives"),
	MAX_LIVES("sync-max-lives"),
	PREMIUM_TYPE("sync-premium"),

	IS_SPECIAL("sync-isspecial"),
	IS_GHELM("sync-isghelm"),
	IS_ARCH("sync-isarch"),
	IS_ARMAS("sync-isamras"),

	IS_VENOM("sync-isvenom");

	private final String ref;

	NBTTag(String ref) {

		this.ref = ref;
	}

	public String getRef() {
		return ref;
	}
}
