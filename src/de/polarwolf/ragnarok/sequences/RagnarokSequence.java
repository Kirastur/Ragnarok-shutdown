package de.polarwolf.ragnarok.sequences;

import java.util.Set;

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
import de.polarwolf.ragnarok.config.RagnarokConfig;

public class RagnarokSequence {
	
	public static final String SEQUENCENAME_SHUTDOWN = "shutdown";
	public static final String SEQUENCENAME_CANCEL = "cancel";
	
	protected final Plugin plugin;
	protected final RagnarokConfig ragnarokConfig;
	protected final LibSequenceSequencer sqSequencer;

	protected LibSequenceCallback sqCallback;
	protected boolean isSectionInitialized = false;
	
	
	public RagnarokSequence(Plugin plugin, RagnarokConfig ragnarokConfig) {
		this.plugin=plugin;
		this.ragnarokConfig=ragnarokConfig;
		sqSequencer = LibSequenceProvider.getAPI().getSequencer();
		sqCallback = new LibSequenceCallbackGeneric(plugin);
	}
	
	public boolean registerAction(String actionName, LibSequenceAction action) {
		 LibSequenceActionResult sqActionResult = sqSequencer.registerAction(actionName, action);
		 if (sqActionResult.hasError()) {
			 plugin.getLogger().warning(sqActionResult.toString());
		 }
		 return !sqActionResult.hasError();		
	}
	
	public boolean loadSequence() {
		LibSequenceConfigResult sqConfigResult = null;
		if (isSectionInitialized) {
			sqConfigResult = sqSequencer.reloadSection(sqCallback);
		} else {
			sqConfigResult = sqSequencer.addSection(sqCallback);
			isSectionInitialized = !sqConfigResult.hasError();
		}
		if (sqConfigResult.hasError()) {
			 plugin.getLogger().warning(sqConfigResult.toString());
		 }
		 return !sqConfigResult.hasError();		
	}
	
	public boolean isSequenceRunning() {
		Set<LibSequenceRunningSequence> sqRunningSequences = sqSequencer.queryRunningSequences(sqCallback);
		return !sqRunningSequences.isEmpty();
	}
	
	public boolean startShutdownSequence(CommandSender initiator) {
		if (isSequenceRunning()) {
			 plugin.getLogger().warning("Cannot start sequence - another sequence is already running");
			return false;
		}
		LibSequenceRunOptions runOptions = new LibSequenceRunOptions();
		runOptions.setSingleton(true);
		runOptions.setInitiator(initiator);
		runOptions.addAuthorizationKey(ragnarokConfig.getAuthorizationKey());
		LibSequenceRunResult sqRunResult = sqSequencer.executeOwnSequence(sqCallback, SEQUENCENAME_SHUTDOWN, runOptions);
		if (sqRunResult.hasError()) {
			 plugin.getLogger().warning(sqRunResult.toString());
		 }
		return !sqRunResult.hasError();
	}
	
	public boolean cancelShutdownSequence() {
		if (!isSequenceRunning()) {
			 plugin.getLogger().warning("Cannot cancel sequence - the sequence is not running");
			return false;
		}
		LibSequenceRunResult sqRunResult = sqSequencer.cancelSequenceByName(sqCallback, SEQUENCENAME_SHUTDOWN);
		if (sqRunResult.hasError()) {
			 plugin.getLogger().warning(sqRunResult.toString());
		 }
		return !sqRunResult.hasError();
	}

	public boolean startCancelSequence(CommandSender initiator) {
		LibSequenceRunOptions runOptions = new LibSequenceRunOptions();
		runOptions.setInitiator(initiator);
		LibSequenceRunResult sqRunResult = sqSequencer.executeOwnSequence(sqCallback, SEQUENCENAME_CANCEL, runOptions);
		if (sqRunResult.hasError()) {
			 plugin.getLogger().warning(sqRunResult.toString());
		 }
		return !sqRunResult.hasError();
	}
	
}
