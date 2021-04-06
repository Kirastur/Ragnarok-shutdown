package de.polarwolf.ragnarok.actions;

import static de.polarwolf.libsequence.actions.LibSequenceActionErrors.*;

import org.bukkit.plugin.Plugin;

import de.polarwolf.libsequence.actions.LibSequenceAction;
import de.polarwolf.libsequence.actions.LibSequenceActionResult;
import de.polarwolf.libsequence.config.LibSequenceConfigStep;
import de.polarwolf.libsequence.runnings.LibSequenceRunOptions;
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
	
	@Override
	public LibSequenceActionResult checkSyntax(LibSequenceConfigStep configStep) {
		// The most actions here have no parameters
    	return new LibSequenceActionResult(configStep.getSequenceName(), configStep.getActionName(), LSAERR_OK, null);
	}
	
	@Override
	public boolean isAuthorized(LibSequenceRunOptions runOptions, LibSequenceConfigStep configStep) {
		return runOptions.verifyAuthorizationKey(ragnarokConfig.getAuthorizationKey());
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
