package de.polarwolf.ragnarok.api;

import de.polarwolf.ragnarok.orchestrator.RagnarokAPI;

public class RagnarokProvider {

	private static RagnarokAPI ragnarokAPI;

	private RagnarokProvider() {
	}

	public static boolean setAPI(RagnarokAPI newAPI) {
		if ((ragnarokAPI != null) && !ragnarokAPI.isDisabled()) {
			return false;
		}
		ragnarokAPI = newAPI;
		return true;
	}

	public static RagnarokAPI getAPI() {
		return ragnarokAPI;
	}

}
