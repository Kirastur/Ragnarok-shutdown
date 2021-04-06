package de.polarwolf.ragnarok.api;

import org.bukkit.command.CommandSender;
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
	
	public boolean startShutdown(CommandSender initiator) {
		if (tools.isCommandAuthorized()) {
			plugin.getLogger().info("Ragnarök was called ... be prepared");
			return sequence.startShutdownSequence(initiator);
		}
		return false;
	}
	
	public boolean cancelShutdown(CommandSender initiator) {
		if (tools.isCommandAuthorized()) {
			plugin.getLogger().info("Ragnarök was send back to his bed");
			Boolean cancelResult = sequence.cancelShutdownSequence();
			if (cancelResult) {
				return sequence.startCancelSequence(initiator);
			}
		}
		return false;
	}
	
	public boolean abortShutdown(CommandSender initiator) {
		plugin.getLogger().info("Ragnarök was forced back to his bed");
		Boolean cancelResult = sequence.cancelShutdownSequence();
		if (cancelResult) {
			return sequence.startCancelSequence(initiator);
		}
		return false;
	}

	public boolean toogleShutdown(CommandSender initiator) {
		if (isShutdownRunning()) {
			return cancelShutdown(initiator);
		} else {
			return startShutdown(initiator);
		}
	}
	
	public boolean reload() {
		// plugin reloadConfig() is done in LibSequence CallbackGeneric
		return sequence.loadSequence();
	}
	
	public void debugEnable() {
		config.setDebug(true);
	}
	
	public void debugDisable() {
		config.setDebug(false);
	}

}
