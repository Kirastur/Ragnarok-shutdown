package de.polarwolf.ragnarok.actions;

import org.bukkit.plugin.Plugin;

import de.polarwolf.libsequence.config.LibSequenceConfigStep;
import de.polarwolf.libsequence.runnings.LibSequenceRunningSequence;
import de.polarwolf.ragnarok.sequences.RagnarokSequence;
import de.polarwolf.ragnarok.tools.RagnarokTools;

public class RagnarokActionShutdown extends RagnarokAction {

	public RagnarokActionShutdown(Plugin plugin, RagnarokTools ragnarokTools, RagnarokSequence ragnarokSequence, String authorizationKey) {
		super(plugin, ragnarokTools, ragnarokSequence, authorizationKey);
	}

	@Override
	public void execute(LibSequenceRunningSequence sequence, LibSequenceConfigStep configStep) {
		ragnarokTools.shutdownServer();
	}

}
