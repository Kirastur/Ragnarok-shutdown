package de.polarwolf.ragnarok.api;

import org.bukkit.command.CommandSender;
import de.polarwolf.libsequence.config.LibSequenceConfigException;
import de.polarwolf.libsequence.runnings.LibSequenceRunException;
import de.polarwolf.ragnarok.sequences.RagnarokSequence;
import de.polarwolf.ragnarok.tools.RagnarokTools;

public class RagnarokAPI {
	
	protected final RagnarokTools tools;
	protected final RagnarokSequence sequence;
	
	public RagnarokAPI (RagnarokTools tools, RagnarokSequence sequence) {
		this.tools = tools;
		this.sequence = sequence;
	}
	
	public boolean isReady() {
		return sequence.isSectionReady();
	}
	
	public boolean isShutdownRunning() {
		return sequence.isShutdownSequenceRunning();
	}
	
	public boolean startShutdown(CommandSender initiator) throws LibSequenceRunException {
		if(!isReady()) {
			return false;
		}
		sequence.startShutdownSequence(initiator);
		return true;
	}
	
	public void cancelShutdown(CommandSender initiator) throws LibSequenceRunException {
		sequence.startCancelSequence(initiator);
	}
	
	public void abortShutdown(CommandSender initiator) throws LibSequenceRunException {
		sequence.startAbortSequence(initiator);
	}

	public boolean toogleShutdown(CommandSender initiator) throws LibSequenceRunException {
		if (isShutdownRunning()) {
			cancelShutdown(initiator);
			return true;
		} else {
			return startShutdown(initiator);
		}
	}
	
	public void reload() throws LibSequenceConfigException {
		// plugin reloadConfig() is done in LibSequence CallbackGeneric
		sequence.loadSequences();
	}
	
}
