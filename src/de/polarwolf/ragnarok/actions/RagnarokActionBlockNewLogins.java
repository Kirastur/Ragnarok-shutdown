package de.polarwolf.ragnarok.actions;

import org.bukkit.plugin.Plugin;

import de.polarwolf.libsequence.config.LibSequenceConfigStep;
import de.polarwolf.libsequence.runnings.LibSequenceRunningSequence;
import de.polarwolf.ragnarok.sequences.RagnarokSequence;
import de.polarwolf.ragnarok.tools.RagnarokTools;

public class RagnarokActionBlockNewLogins extends RagnarokAction {
	
	public RagnarokActionBlockNewLogins(Plugin plugin, RagnarokTools ragnarokTools, RagnarokSequence ragnarokSequence, String authorizationKey) {
		super(plugin, ragnarokTools, ragnarokSequence, authorizationKey);
	}

	@Override
	public void execute(LibSequenceRunningSequence sequence, LibSequenceConfigStep configStep) {
		ragnarokTools.addBlockingSequence(sequence);
	}
	
	protected void doEnd(LibSequenceRunningSequence sequence) {
		ragnarokTools.removeBlockingSequence(sequence);
	}
	
	@Override
    public void onCancel(LibSequenceRunningSequence sequence) {
		doEnd(sequence);
    }
    
	@Override
    public void onFinish(LibSequenceRunningSequence sequence) {
		doEnd(sequence);
    }
	

}
