package de.polarwolf.ragnarok.actions;

import de.polarwolf.libsequence.actions.LibSequenceActionGeneric;
import de.polarwolf.ragnarok.shutdown.RagnarokHelper;

public abstract class RagnarokAction extends LibSequenceActionGeneric {

	private final RagnarokHelper ragnarokHelper;

	protected RagnarokAction(RagnarokHelper ragnarokHelper) {
		super(ragnarokHelper.getToken());
		this.ragnarokHelper = ragnarokHelper;
	}

	protected RagnarokHelper getRagnarokHelper() {
		return ragnarokHelper;
	}

	protected boolean isDisabled() {
		return ragnarokHelper.isDisabled();
	}

}
