package de.polarwolf.ragnarok.actions;

import static de.polarwolf.libsequence.actions.LibSequenceActionErrors.*;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.Plugin;

import de.polarwolf.libsequence.actions.LibSequenceActionResult;
import de.polarwolf.libsequence.config.LibSequenceConfigStep;
import de.polarwolf.libsequence.runnings.LibSequenceRunningSequence;
import de.polarwolf.ragnarok.config.RagnarokConfig;
import de.polarwolf.ragnarok.sequences.RagnarokSequence;
import de.polarwolf.ragnarok.tools.RagnarokTools;

public class RagnarokActionBlockNewLogins extends RagnarokAction {
	
	protected List<LibSequenceRunningSequence> blockingSequences = new ArrayList<>();

	public RagnarokActionBlockNewLogins(Plugin plugin, RagnarokConfig ragnarokConfig, RagnarokTools ragnarokTools, RagnarokSequence ragnarokSequence) {
		super(plugin, ragnarokConfig, ragnarokTools, ragnarokSequence);
	}

	@Override
	public LibSequenceActionResult doAuthorizedExecute(LibSequenceRunningSequence sequence, LibSequenceConfigStep configStep) {
		if (blockingSequences.isEmpty()) {
			ragnarokTools.blockNewLogins();
		}
		blockingSequences.add(sequence);
    	return new LibSequenceActionResult(null, LSAERR_OK, null);
	}
	
	protected void doEnd(LibSequenceRunningSequence sequence) {
		if (blockingSequences.contains(sequence)) {
			blockingSequences.remove(sequence);
			if (blockingSequences.isEmpty()) {
				ragnarokTools.unblockNewLogins();			
			}
		}
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
