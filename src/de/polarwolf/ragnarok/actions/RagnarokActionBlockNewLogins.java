package de.polarwolf.ragnarok.actions;

import de.polarwolf.libsequence.config.LibSequenceConfigStep;
import de.polarwolf.libsequence.runnings.LibSequenceRunningSequence;
import de.polarwolf.ragnarok.shutdown.RagnarokHelper;

public class RagnarokActionBlockNewLogins extends RagnarokAction {

	public RagnarokActionBlockNewLogins(RagnarokHelper ragnarokHelper) {
		super(ragnarokHelper);
	}

	@Override
	public void execute(LibSequenceRunningSequence sequence, LibSequenceConfigStep configStep) {
		if (!isDisabled())
			getRagnarokHelper().addBlockingSequence(sequence);
	}

	@Override
	public void onCancel(LibSequenceRunningSequence sequence) {
		if (!isDisabled())
			getRagnarokHelper().removeBlockingSequence(sequence);
	}

	@Override
	public void onFinish(LibSequenceRunningSequence sequence) {
		if (!isDisabled())
			getRagnarokHelper().removeBlockingSequence(sequence);
	}

}
