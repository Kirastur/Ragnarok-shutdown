package de.polarwolf.ragnarok.actions;

import static de.polarwolf.libsequence.actions.LibSequenceActionErrors.*;

import org.bukkit.plugin.Plugin;

import de.polarwolf.libsequence.actions.LibSequenceActionResult;
import de.polarwolf.libsequence.config.LibSequenceConfigStep;
import de.polarwolf.libsequence.runnings.LibSequenceRunningSequence;
import de.polarwolf.ragnarok.sequences.RagnarokSequence;
import de.polarwolf.ragnarok.tools.RagnarokTools;

public class RagnarokActionBlockNewLogins extends RagnarokAction {
	
	public RagnarokActionBlockNewLogins(Plugin plugin, RagnarokTools ragnarokTools, RagnarokSequence ragnarokSequence) {
		super(plugin, ragnarokTools, ragnarokSequence);
	}

	@Override
	public LibSequenceActionResult doExecute(LibSequenceRunningSequence sequence, LibSequenceConfigStep configStep) {
		ragnarokTools.addBlockingSequence(sequence);
    	return new LibSequenceActionResult(configStep.getSequenceName(), configStep.getActionName(), LSAERR_OK, null, null);
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
