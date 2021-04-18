package de.polarwolf.ragnarok.actions;

import static de.polarwolf.libsequence.actions.LibSequenceActionErrors.*;

import org.bukkit.plugin.Plugin;

import de.polarwolf.libsequence.actions.LibSequenceActionResult;
import de.polarwolf.libsequence.config.LibSequenceConfigStep;
import de.polarwolf.libsequence.runnings.LibSequenceRunOptions;
import de.polarwolf.libsequence.runnings.LibSequenceRunningSequence;
import de.polarwolf.ragnarok.sequences.RagnarokSequence;
import de.polarwolf.ragnarok.tools.RagnarokTools;

public class RagnarokActionCancel extends RagnarokAction{

	public RagnarokActionCancel(Plugin plugin, RagnarokTools ragnarokTools, RagnarokSequence ragnarokSequence) {
		super(plugin, ragnarokTools, ragnarokSequence);
	}

	@Override
	public boolean isAuthorized(LibSequenceRunOptions runOptions, LibSequenceConfigStep configStep) {
		return runOptions.verifyAuthorizationKey(ragnarokSequence.getAuthorizationKeyCancel());
	}
	
	@Override
	public LibSequenceActionResult doExecute(LibSequenceRunningSequence sequence, LibSequenceConfigStep configStep) {
		boolean result = ragnarokSequence.doStopShutdownSequence();
		if (result) {
			return new LibSequenceActionResult(configStep.getSequenceName(), configStep.getActionName(), LSAERR_OK, null);
		} else {
			return new LibSequenceActionResult(configStep.getSequenceName(), configStep.getActionName(), LSAERR_USER_DEFINED_ERROR, "Cannot cancel shutdown - the shutdown sequence is not running");
			
		}
	}

}
