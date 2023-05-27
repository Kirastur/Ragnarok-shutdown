package de.polarwolf.ragnarok.orchestrator;

import org.bukkit.plugin.Plugin;

public class RagnarokConfig {

	public static final String CONFIG_DEBUG = "general.enableDebug";

	protected final Plugin plugin;

	public RagnarokConfig(RagnarokOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
	}

	public boolean isDebug() {
		return plugin.getConfig().getBoolean(CONFIG_DEBUG);
	}

}
