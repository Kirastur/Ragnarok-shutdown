package de.polarwolf.ragnarok.actions;

import static de.polarwolf.libsequence.actions.LibSequenceActionErrors.*;

import org.bukkit.plugin.Plugin;

import de.polarwolf.libsequence.actions.LibSequenceAction;
import de.polarwolf.libsequence.actions.LibSequenceActionResult;
import de.polarwolf.libsequence.config.LibSequenceConfigStep;
import de.polarwolf.libsequence.runnings.LibSequenceRunningSequence;
import de.polarwolf.ragnarok.config.RagnarokConfig;
import de.polarwolf.ragnarok.sequences.RagnarokSequence;
import de.polarwolf.ragnarok.tools.RagnarokTools;

public abstract class RagnarokAction implements LibSequenceAction {

	protected final Plugin plugin;
	protected final RagnarokConfig ragnarokConfig;
	protected final RagnarokTools ragnarokTools;
	protected final RagnarokSequence ragnarokSequence;
	
	protected RagnarokAction(Plugin plugin, RagnarokConfig ragnarokConfig, RagnarokTools ragnarokTools, RagnarokSequence ragnarokSequence) {
		this.plugin = plugin;
		this.ragnarokConfig = ragnarokConfig;
		this.ragnarokTools = ragnarokTools;
		this.ragnarokSequence = ragnarokSequence;
	}
	
	protected abstract LibSequenceActionResult doAuthorizedExecute(LibSequenceRunningSequence sequence, LibSequenceConfigStep configStep);

	@Override
	public LibSequenceActionResult checkSyntax(LibSequenceConfigStep configStep) {
		// The most actions here have no parameters
    	return new LibSequenceActionResult(null, LSAERR_OK, null);
	}
	
	@Override
	public LibSequenceActionResult doExecute(LibSequenceRunningSequence sequence, LibSequenceConfigStep configStep) {
		if (ragnarokSequence.isMySequence(sequence) || !ragnarokConfig.isActionsArePrivate()) {
			return doAuthorizedExecute(sequence, configStep);
		} else {
			return new LibSequenceActionResult(configStep.getActionName(), LSAERR_USER_DEFINED_ERROR, "Permission denied");
		}
	}

	@Override
	public void onInit(LibSequenceRunningSequence sequence) {		
	}

	@Override
    public void onCancel(LibSequenceRunningSequence sequence) {
    }
    
	@Override
    public void onFinish(LibSequenceRunningSequence sequence) {
    }

}
