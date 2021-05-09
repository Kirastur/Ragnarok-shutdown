package de.polarwolf.ragnarok.actions;

import static de.polarwolf.libsequence.actions.LibSequenceActionErrors.*;

import org.bukkit.plugin.Plugin;

import de.polarwolf.libsequence.actions.LibSequenceActionException;
import de.polarwolf.libsequence.config.LibSequenceConfigStep;
import de.polarwolf.libsequence.exception.LibSequenceException;
import de.polarwolf.libsequence.runnings.LibSequenceRunOptions;
import de.polarwolf.libsequence.runnings.LibSequenceRunningSequence;
import de.polarwolf.ragnarok.sequences.RagnarokSequence;
import de.polarwolf.ragnarok.tools.RagnarokTools;

public class RagnarokActionCancel extends RagnarokAction{

	public RagnarokActionCancel(Plugin plugin, RagnarokTools ragnarokTools, RagnarokSequence ragnarokSequence, String authorizationKey) {
		super(plugin, ragnarokTools, ragnarokSequence, authorizationKey);
	}

	@Override
	public void validateAuthorization(LibSequenceRunOptions runOptions, LibSequenceConfigStep configStep) throws LibSequenceException {
		if (!runOptions.verifyAuthorizationKey(ragnarokSequence.getAuthorizationKeyCancel())) {
			throw new LibSequenceActionException(configStep.findActionName(), LSAERR_NOT_AUTHORIZED, null);
		}
	}
	
	@Override
	public void execute(LibSequenceRunningSequence sequence, LibSequenceConfigStep configStep) {
		ragnarokSequence.stopShutdownSequence();
	}

}
