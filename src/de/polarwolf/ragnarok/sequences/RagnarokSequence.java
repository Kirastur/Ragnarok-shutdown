package de.polarwolf.ragnarok.sequences;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import de.polarwolf.libsequence.main.LibSequenceProvider;
import de.polarwolf.libsequence.api.LibSequenceSequencer;
import de.polarwolf.libsequence.callback.LibSequenceCallbackGeneric;
import de.polarwolf.libsequence.actions.LibSequenceAction;
import de.polarwolf.libsequence.actions.LibSequenceActionException;
import de.polarwolf.libsequence.config.LibSequenceConfigException;
import de.polarwolf.libsequence.runnings.LibSequenceRunException;
import de.polarwolf.libsequence.runnings.LibSequenceRunOptions;
import de.polarwolf.libsequence.runnings.LibSequenceRunningSequence;
import de.polarwolf.ragnarok.tools.RagnarokTools;

public class RagnarokSequence {
	
	public static final String SEQUENCENAME_SHUTDOWN = "shutdown";
	public static final String SEQUENCENAME_CANCEL = "cancel";
	public static final String SEQUENCENAME_ABORT = "abort";
	
	protected final String authorizationKeyShutdown; 
	protected final String authorizationKeyCancel; 
	
	protected final Plugin plugin;
	protected final RagnarokTools ragnarokTools;
	protected final LibSequenceSequencer sqSequencer;

	protected LibSequenceCallbackGeneric sqCallback;
	protected boolean isSectionInitialized = false;
	
	
	public RagnarokSequence(Plugin plugin, RagnarokTools ragnarokTools) {
		this.plugin=plugin;
		this.ragnarokTools=ragnarokTools;
		authorizationKeyShutdown = UUID.randomUUID().toString();
		authorizationKeyCancel = UUID.randomUUID().toString();
		sqSequencer = LibSequenceProvider.getAPI().getSequencer();
		sqCallback = new LibSequenceCallbackGeneric(plugin);
		sqCallback.setEnableConsoleNotifications(ragnarokTools.isDebugOutput());
		sqCallback.setEnableInitiatorNotifications(ragnarokTools.isDebugOutput());
	}
	
	public void registerAction(String actionName, LibSequenceAction action) throws LibSequenceActionException {
		 sqSequencer.registerAction(actionName, action);
	}
	
	public void loadSequences() throws LibSequenceConfigException {
		sqSequencer.loadSection(sqCallback);
	}
	
	public String getAuthorizationKeyShutdown() {
		return authorizationKeyShutdown;
	}

	public String getAuthorizationKeyCancel() {
		return authorizationKeyCancel;
	}
	
	public boolean isSectionReady() {
		return (sqSequencer.hasOwnSequence(sqCallback, SEQUENCENAME_SHUTDOWN) &&
 				 sqSequencer.hasOwnSequence(sqCallback, SEQUENCENAME_CANCEL) &&
		         sqSequencer.hasOwnSequence(sqCallback, SEQUENCENAME_ABORT));
	}
	
	
	// We could held a local copy of the runningSequence, but this would be redundant
	// So it's better to make a realtime query
	public LibSequenceRunningSequence findRunningShutdownSequence() {
		Set<LibSequenceRunningSequence> sqRunningSequences = sqSequencer.findRunningSequences(sqCallback);
		Iterator<LibSequenceRunningSequence> iter = sqRunningSequences.iterator();
		while (iter.hasNext()) {
			LibSequenceRunningSequence runningSequence = iter.next();
			if (runningSequence.getName().equals(SEQUENCENAME_SHUTDOWN)) {
				return runningSequence;
			}
		}
		return null;
	}
	
	public boolean isShutdownSequenceRunning() {
		return (findRunningShutdownSequence() != null);
		
	}

	public void stopShutdownSequence() {
		LibSequenceRunningSequence runningSequence = findRunningShutdownSequence();
		if (runningSequence != null) {
			runningSequence.cancel();
		}
	}
	
	public void startShutdownSequence(CommandSender initiator) throws LibSequenceRunException {
		LibSequenceRunOptions runOptions = new LibSequenceRunOptions();
		runOptions.setSingleton(true);
		runOptions.setInitiator(initiator);
		runOptions.addAuthorizationKey(getAuthorizationKeyShutdown());
		sqSequencer.executeOwnSequence(sqCallback, SEQUENCENAME_SHUTDOWN, runOptions);
	}
	
	public void startCancelSequence(CommandSender initiator) throws LibSequenceRunException {
		LibSequenceRunOptions runOptions = new LibSequenceRunOptions();
		runOptions.setInitiator(initiator);
		runOptions.addAuthorizationKey(getAuthorizationKeyCancel());
		sqSequencer.executeOwnSequence(sqCallback, SEQUENCENAME_CANCEL, runOptions);
	}

	public void startAbortSequence(CommandSender initiator) throws LibSequenceRunException {
		LibSequenceRunOptions runOptions = new LibSequenceRunOptions();
		runOptions.setInitiator(initiator);
		runOptions.addAuthorizationKey(getAuthorizationKeyCancel());
		sqSequencer.executeOwnSequence(sqCallback, SEQUENCENAME_ABORT, runOptions);
	}
	
}
