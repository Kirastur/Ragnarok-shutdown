package de.polarwolf.ragnarok.actions;

import de.polarwolf.libsequence.config.LibSequenceConfigStep;
import de.polarwolf.libsequence.runnings.LibSequenceRunningSequence;
import de.polarwolf.ragnarok.shutdown.RagnarokHelper;

public class RagnarokActionCancel extends RagnarokAction {

	public RagnarokActionCancel(RagnarokHelper ragnarokHelper) {
		super(ragnarokHelper);
	}

	@Override
	public void execute(LibSequenceRunningSequence sequence, LibSequenceConfigStep configStep) {
		if (!isDisabled())
			getRagnarokHelper().stopShutdownSequence();
	}

}
