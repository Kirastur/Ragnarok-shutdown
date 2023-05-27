package de.polarwolf.ragnarok.orchestrator;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import de.polarwolf.libsequence.exception.LibSequenceException;
import de.polarwolf.libsequence.orchestrator.LibSequenceSequencer;
import de.polarwolf.libsequence.runnings.LibSequenceRunException;

public class RagnarokAPI {

	protected final UUID apiToken;
	protected final RagnarokOrchestrator orchestrator;

	public RagnarokAPI(Plugin plugin, UUID apiToken, LibSequenceSequencer sequenceAPI) throws LibSequenceException {
		this.apiToken = apiToken;
		this.orchestrator = new RagnarokOrchestrator(plugin, sequenceAPI);
	}

	public boolean isReady() {
		return !orchestrator.isDisabled() && orchestrator.getRagnarokSequence().isSectionReady();
	}

	public boolean isShutdownRunning() {
		return orchestrator.getRagnarokSequence().isShutdownSequenceRunning();
	}

	public boolean startShutdown(CommandSender initiator) throws LibSequenceRunException {
		if (!isReady()) {
			return false;
		}
		orchestrator.getRagnarokSequence().startShutdownSequence(initiator);
		return true;
	}

	public void cancelShutdown(CommandSender initiator) throws LibSequenceRunException {
		if (!orchestrator.isDisabled()) {
			orchestrator.getRagnarokSequence().startCancelSequence(initiator);
		}
	}

	public void abortShutdown(CommandSender initiator) throws LibSequenceRunException {
		if (!orchestrator.isDisabled()) {
			orchestrator.getRagnarokSequence().startAbortSequence(initiator);
		}
	}

	public boolean toogleShutdown(CommandSender initiator) throws LibSequenceRunException {
		if (isShutdownRunning()) {
			cancelShutdown(initiator);
			return true;
		} else {
			return startShutdown(initiator);
		}
	}

	public void reload() throws LibSequenceException {
		orchestrator.reload();
	}

	public boolean isDisabled() {
		return orchestrator.isDisabled();
	}

	public boolean disable(UUID currentApiToken) {
		if (!apiToken.equals(currentApiToken)) {
			return false;
		}
		orchestrator.disable();
		return true;
	}
}
