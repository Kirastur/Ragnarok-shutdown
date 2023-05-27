package de.polarwolf.ragnarok.shutdown;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import de.polarwolf.libsequence.actions.LibSequenceAction;
import de.polarwolf.libsequence.actions.LibSequenceActionException;
import de.polarwolf.libsequence.exception.LibSequenceException;
import de.polarwolf.libsequence.logger.LibSequenceLoggerDefault;
import de.polarwolf.libsequence.orchestrator.LibSequenceSequencer;
import de.polarwolf.libsequence.runnings.LibSequenceRunException;
import de.polarwolf.libsequence.runnings.LibSequenceRunOptions;
import de.polarwolf.libsequence.runnings.LibSequenceRunningSequence;
import de.polarwolf.ragnarok.orchestrator.RagnarokConfig;
import de.polarwolf.ragnarok.orchestrator.RagnarokOrchestrator;

public class RagnarokSequence {

	public static final String SEQUENCENAME_SHUTDOWN = "shutdown";
	public static final String SEQUENCENAME_CANCEL = "cancel";
	public static final String SEQUENCENAME_ABORT = "abort";

	public static final String SECTION_NAME = "sequences";

	protected final Plugin plugin;
	protected final LibSequenceSequencer sequenceAPI;
	protected final LibSequenceLoggerDefault logger;
	protected final RagnarokConfig ragnarokConfig;
	protected final RagnarokHelper ragnarokHelper;

	public RagnarokSequence(RagnarokOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
		this.sequenceAPI = orchestrator.getSequenceAPI();
		this.logger = orchestrator.getLogger();
		this.ragnarokConfig = orchestrator.getRagnarokConfig();
		this.ragnarokHelper = orchestrator.getRagnarokHelper();
		ragnarokHelper.setRagnarokSequence(this);
	}

	public void registerAction(String actionName, LibSequenceAction action) throws LibSequenceActionException {
		sequenceAPI.registerAction(actionName, action);
	}

	public void registerSequences() {
		sequenceAPI.attachConfigFileToReloaderLater(plugin, ragnarokHelper.getToken(), null, SECTION_NAME);
	}

	public boolean isSectionReady() {
		return (sequenceAPI.hasOwnSequence(ragnarokHelper.getToken(), SEQUENCENAME_SHUTDOWN)
				&& sequenceAPI.hasOwnSequence(ragnarokHelper.getToken(), SEQUENCENAME_CANCEL)
				&& sequenceAPI.hasOwnSequence(ragnarokHelper.getToken(), SEQUENCENAME_ABORT));
	}

	// We could held a local copy of the runningSequence, but this would be
	// redundant. So it's better to make a realtime query
	public LibSequenceRunningSequence findRunningShutdownSequence() {
		List<LibSequenceRunningSequence> runningSequences = sequenceAPI.findRunningSequences(ragnarokHelper.getToken());
		for (LibSequenceRunningSequence myRunningSequence : runningSequences) {
			if (myRunningSequence.getName().equals(SEQUENCENAME_SHUTDOWN)) {
				return myRunningSequence;
			}
		}
		return null;
	}

	public boolean isShutdownSequenceRunning() {
		return (findRunningShutdownSequence() != null);
	}

	public void startShutdownSequence(CommandSender initiator) throws LibSequenceRunException {
		LibSequenceRunOptions runOptions = new LibSequenceRunOptions();
		runOptions.setSingleton(true);
		runOptions.addAuthorizationToken(ragnarokHelper.getToken());
		runOptions.setInitiator(initiator);
		runOptions.setLogger(logger);
		sequenceAPI.executeOwnSequence(ragnarokHelper.getToken(), SEQUENCENAME_SHUTDOWN, runOptions);
	}

	public void stopShutdownSequence() {
		LibSequenceRunningSequence runningSequence = findRunningShutdownSequence();
		if (runningSequence != null) {
			runningSequence.cancel();
		}
	}

	public void startCancelSequence(CommandSender initiator) throws LibSequenceRunException {
		LibSequenceRunOptions runOptions = new LibSequenceRunOptions();
		runOptions.addAuthorizationToken(ragnarokHelper.getToken());
		runOptions.setInitiator(initiator);
		runOptions.setLogger(logger);
		sequenceAPI.executeOwnSequence(ragnarokHelper.getToken(), SEQUENCENAME_CANCEL, runOptions);
	}

	public void startAbortSequence(CommandSender initiator) throws LibSequenceRunException {
		LibSequenceRunOptions runOptions = new LibSequenceRunOptions();
		runOptions.addAuthorizationToken(ragnarokHelper.getToken());
		runOptions.setInitiator(initiator);
		runOptions.setLogger(logger);
		sequenceAPI.executeOwnSequence(ragnarokHelper.getToken(), SEQUENCENAME_ABORT, runOptions);
	}

	public int reload() throws LibSequenceException {
		return sequenceAPI.partialReloadFromConfigFile(ragnarokHelper.getToken());
	}

	public void disable() {
		ragnarokHelper.setRagnarokSequence(null);
	}

}
