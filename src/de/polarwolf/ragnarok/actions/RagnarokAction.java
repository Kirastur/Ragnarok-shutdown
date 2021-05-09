package de.polarwolf.ragnarok.actions;

import org.bukkit.plugin.Plugin;

import de.polarwolf.libsequence.actions.LibSequenceActionGeneric;
import de.polarwolf.ragnarok.sequences.RagnarokSequence;
import de.polarwolf.ragnarok.tools.RagnarokTools;

public abstract class RagnarokAction extends LibSequenceActionGeneric {

	protected final Plugin plugin;
	protected final RagnarokTools ragnarokTools;
	protected final RagnarokSequence ragnarokSequence;
	
	protected RagnarokAction(Plugin plugin, RagnarokTools ragnarokTools, RagnarokSequence ragnarokSequence, String authorizationKey) {
		super(authorizationKey);
		this.plugin = plugin;
		this.ragnarokTools = ragnarokTools;
		this.ragnarokSequence = ragnarokSequence;
	}
	
}
