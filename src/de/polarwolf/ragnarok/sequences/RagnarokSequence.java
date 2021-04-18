package de.polarwolf.ragnarok.sequences;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import de.polarwolf.libsequence.main.LibSequenceProvider;
import de.polarwolf.libsequence.api.LibSequenceSequencer;
import de.polarwolf.libsequence.callback.LibSequenceCallback;
import de.polarwolf.libsequence.callback.LibSequenceCallbackGeneric;
import de.polarwolf.libsequence.actions.LibSequenceAction;
import de.polarwolf.libsequence.actions.LibSequenceActionResult;
import de.polarwolf.libsequence.config.LibSequenceConfigResult;
import de.polarwolf.libsequence.runnings.LibSequenceRunOptions;
import de.polarwolf.libsequence.runnings.LibSequenceRunResult;
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

	protected LibSequenceCallback sqCallback;
	protected boolean isSectionInitialized = false;
	
	
	public RagnarokSequence(Plugin plugin, RagnarokTools ragnarokTools) {
		this.plugin=plugin;
		this.ragnarokTools=ragnarokTools;
		authorizationKeyShutdown = UUID.randomUUID().toString();
		authorizationKeyCancel = UUID.randomUUID().toString();
		sqSequencer = LibSequenceProvider.getAPI().getSequencer();
		sqCallback = new LibSequenceCallbackGeneric(plugin, ragnarokTools.isDebugOutput());
	}
	
	public boolean registerAction(String actionName, LibSequenceAction action) {
		 LibSequenceActionResult sqActionResult = sqSequencer.registerAction(actionName, action);
		 if (sqActionResult.hasError()) {
			 plugin.getLogger().warning(sqActionResult.toString());
		 }
		 return !sqActionResult.hasError();		
	}
	
	public boolean loadSequences() {
		LibSequenceConfigResult sqConfigResult =  sqSequencer.loadSection(sqCallback);
		 if (sqConfigResult.hasError()) {
			 plugin.getLogger().warning(sqConfigResult.toString());
			 return false;
		 }
		 if (!isSectionReady()) {
			 plugin.getLogger().warning("At least one required sequence is missing");
			 plugin.getLogger().warning("   shutdown - cancel - abort");
			 plugin.getLogger().warning("Please fix your config or delete it to get a fresh default config");
			 return false;
		 }
		return true;		
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
		Set<LibSequenceRunningSequence> sqRunningSequences = sqSequencer.queryRunningSequences(sqCallback);
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

	public boolean doStopShutdownSequence() {
		LibSequenceRunningSequence runningSequence = findRunningShutdownSequence();
		if (runningSequence == null) {
			return false;
		}
		runningSequence.cancel();
		return true;		
	}
	
	public boolean startShutdownSequence(CommandSender initiator) {
		if (isShutdownSequenceRunning()) {
			 plugin.getLogger().warning("Cannot start sequence - another sequence is already running");
			return false;
		}
		LibSequenceRunOptions runOptions = new LibSequenceRunOptions();
		runOptions.setSingleton(true);
		runOptions.setInitiator(initiator);
		runOptions.addAuthorizationKey(getAuthorizationKeyShutdown());
		LibSequenceRunResult sqRunResult = sqSequencer.executeOwnSequence(sqCallback, SEQUENCENAME_SHUTDOWN, runOptions);
		if (sqRunResult.hasError()) {
			 plugin.getLogger().warning(sqRunResult.toString());
		 }
		return !sqRunResult.hasError();
	}
	
	public boolean cancelShutdownSequence(CommandSender initiator) {
		if (!isShutdownSequenceRunning()) {
			 plugin.getLogger().warning("Cannot cancel sequence - the sequence is not running");
			return false;
		}
		LibSequenceRunOptions runOptions = new LibSequenceRunOptions();
		runOptions.setInitiator(initiator);
		runOptions.addAuthorizationKey(getAuthorizationKeyCancel());
		LibSequenceRunResult sqRunResult = sqSequencer.executeOwnSequence(sqCallback, SEQUENCENAME_CANCEL, runOptions);
		if (sqRunResult.hasError()) {
			 plugin.getLogger().warning(sqRunResult.toString());
		 }
		return !sqRunResult.hasError();
	}

	public boolean abortShutdownSequence(CommandSender initiator) {
		if (!isShutdownSequenceRunning()) {
			 plugin.getLogger().warning("Cannot abort sequence - the sequence is not running");
			return false;
		}
		LibSequenceRunOptions runOptions = new LibSequenceRunOptions();
		runOptions.setInitiator(initiator);
		runOptions.addAuthorizationKey(getAuthorizationKeyCancel());
		LibSequenceRunResult sqRunResult = sqSequencer.executeOwnSequence(sqCallback, SEQUENCENAME_ABORT, runOptions);
		if (sqRunResult.hasError()) {
			 plugin.getLogger().warning(sqRunResult.toString());
		 }
		return !sqRunResult.hasError();
	}
	
}
