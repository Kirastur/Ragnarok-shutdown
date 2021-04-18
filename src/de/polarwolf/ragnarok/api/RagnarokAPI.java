package de.polarwolf.ragnarok.api;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import de.polarwolf.ragnarok.sequences.RagnarokSequence;
import de.polarwolf.ragnarok.tools.RagnarokTools;

public class RagnarokAPI {
	
	protected final Plugin plugin;
	protected final RagnarokTools tools;
	protected final RagnarokSequence sequence;
	
	public RagnarokAPI (Plugin plugin, RagnarokTools tools, RagnarokSequence sequence) {
		this.plugin = plugin;
		this.tools = tools;
		this.sequence = sequence;
	}
	
	public boolean isReady() {
		return sequence.isSectionReady();
	}
	
	public boolean isShutdownRunning() {
		return sequence.isShutdownSequenceRunning();
	}
	
	public boolean startShutdown(CommandSender initiator) {
		if(!isReady()) {
			return false;
		}
		return sequence.startShutdownSequence(initiator);
	}
	
	public boolean cancelShutdown(CommandSender initiator) {
		return sequence.cancelShutdownSequence(initiator);
	}
	
	public boolean abortShutdown(CommandSender initiator) {
		return sequence.abortShutdownSequence(initiator);
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
		return sequence.loadSequences();
	}
	
}
