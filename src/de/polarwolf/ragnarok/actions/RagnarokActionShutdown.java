package de.polarwolf.ragnarok.actions;

import de.polarwolf.libsequence.config.LibSequenceConfigStep;
import de.polarwolf.libsequence.runnings.LibSequenceRunningSequence;
import de.polarwolf.ragnarok.shutdown.RagnarokHelper;

public class RagnarokActionShutdown extends RagnarokAction {

	public RagnarokActionShutdown(RagnarokHelper ragnarokHelper) {
		super(ragnarokHelper);
	}

	@Override
	public void execute(LibSequenceRunningSequence sequence, LibSequenceConfigStep configStep) {
		if (!isDisabled())
			getRagnarokHelper().shutdownServer();
	}

}
