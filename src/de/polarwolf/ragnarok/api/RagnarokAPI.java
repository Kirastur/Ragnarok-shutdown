package de.polarwolf.ragnarok.api;

import org.bukkit.plugin.Plugin;

import de.polarwolf.ragnarok.config.RagnarokConfig;
import de.polarwolf.ragnarok.sequences.RagnarokSequence;
import de.polarwolf.ragnarok.tools.RagnarokTools;

public class RagnarokAPI {
	
	protected final Plugin plugin;
	protected final RagnarokConfig config;
	protected final RagnarokTools tools;
	protected final RagnarokSequence sequence;
	
	public RagnarokAPI (Plugin plugin, RagnarokConfig config, RagnarokTools tools, RagnarokSequence sequence) {
		this.plugin = plugin;
		this.config = config;
		this.tools = tools;
		this.sequence = sequence;
	}
	
	public boolean isShutdownRunning() {
		return sequence.isSequenceRunning();
	}
	
	public boolean startShutdown() {
		if (tools.isCommandAuthorized()) {
			plugin.getLogger().info("Ragnarök was called ... be prepared");
			return sequence.startShutdownSequence();
		}
		return false;
	}
	
	public boolean cancelShutdown() {
		if (tools.isCommandAuthorized()) {
			plugin.getLogger().info("Ragnarök was send back to his bed");
			Boolean cancelResult = sequence.cancelShutdownSequence();
			if (cancelResult) {
				return sequence.startCancelSequence();
			}
		}
		return false;
	}
	
	public boolean abortShutdown() {
		plugin.getLogger().info("Ragnarök was forced back to his bed");
		Boolean cancelResult = sequence.cancelShutdownSequence();
		if (cancelResult) {
			return sequence.startCancelSequence();
		}
		return false;
	}

	public boolean toogleShutdown() {
		if (isShutdownRunning()) {
			return cancelShutdown();
		} else {
			return startShutdown();
		}
	}
	
	public boolean reload() {
		plugin.reloadConfig();
		return sequence.loadSequence();
	}
	
	public void debugEnable() {
		config.setDebug(true);
	}
	
	public void debugDisable() {
		config.setDebug(false);
	}

}
