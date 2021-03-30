package de.polarwolf.ragnarok.actions;

import static de.polarwolf.libsequence.actions.LibSequenceActionErrors.*;

import org.bukkit.plugin.Plugin;

import de.polarwolf.libsequence.actions.LibSequenceActionResult;
import de.polarwolf.libsequence.config.LibSequenceConfigStep;
import de.polarwolf.libsequence.runnings.LibSequenceRunningSequence;
import de.polarwolf.ragnarok.config.RagnarokConfig;
import de.polarwolf.ragnarok.sequences.RagnarokSequence;
import de.polarwolf.ragnarok.tools.RagnarokTools;

public class RagnarokActionShutdown extends RagnarokAction {

	public RagnarokActionShutdown(Plugin plugin, RagnarokConfig ragnarokConfig, RagnarokTools ragnarokTools, RagnarokSequence ragnarokSequence) {
		super(plugin, ragnarokConfig, ragnarokTools, ragnarokSequence);
	}

	@Override
	public LibSequenceActionResult doAuthorizedExecute(LibSequenceRunningSequence sequence, LibSequenceConfigStep configStep) {
		ragnarokTools.shutdownServer();
    	return new LibSequenceActionResult(null, LSAERR_OK, null);
	}

}
