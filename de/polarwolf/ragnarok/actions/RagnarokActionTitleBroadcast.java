package de.polarwolf.ragnarok.actions;

import static de.polarwolf.libsequence.actions.LibSequenceActionErrors.*;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.polarwolf.libsequence.actions.LibSequenceAction;
import de.polarwolf.libsequence.actions.LibSequenceActionResult;
import de.polarwolf.libsequence.config.LibSequenceConfigStep;
import de.polarwolf.libsequence.runnings.LibSequenceRunningSequence;

public class RagnarokActionTitleBroadcast implements LibSequenceAction {
	
	public static final String NAME_TITLE = "title";
	public static final String NAME_SUBTITLE = "subtitle";
	public static final String NAME_SECONDS = "seconds";
	
	protected final Plugin plugin;
	
	public RagnarokActionTitleBroadcast (Plugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public LibSequenceActionResult checkSyntax(LibSequenceConfigStep configStep) {
		String title = configStep.getValue(NAME_TITLE);
		String subtitle = configStep.getValue(NAME_SUBTITLE);
		String seconds = configStep.getValue(NAME_SECONDS);
		if ((title==null) && (subtitle==null)) {			
	    	return new LibSequenceActionResult(configStep.getActionName(), LSAERR_MISSING_ATTRIBUTE, "'title' or 'subitle' must be defined");
		}
		if (seconds == null) {
	    	return new LibSequenceActionResult(configStep.getActionName(), LSAERR_MISSING_ATTRIBUTE, "'seconds' must be defined");
		}
		try {
			Integer.parseUnsignedInt(seconds);
		} catch (Exception e) {
			return new LibSequenceActionResult(configStep.getActionName(), LSAERR_EXCEPTION, "'seconds' not numeric");
		}
    	return new LibSequenceActionResult(null, LSAERR_OK, null);
	}
	
	@Override
	public LibSequenceActionResult doExecute(LibSequenceRunningSequence sequence, LibSequenceConfigStep configStep) {
		String title = configStep.getValue(NAME_TITLE);
		String subtitle = configStep.getValue(NAME_SUBTITLE);
		String seconds = configStep.getValue(NAME_SECONDS);
		int duration;
		try {
			duration = Integer.parseUnsignedInt(seconds);
		} catch (Exception e) {
			return new LibSequenceActionResult(configStep.getActionName(), LSAERR_EXCEPTION, "'seconds' not numeric");
		}
		for (Player player : plugin.getServer().getOnlinePlayers()) {
			player.sendTitle(title, subtitle, 10, duration *20, 20);
		}
    	return new LibSequenceActionResult(null, LSAERR_OK, null);
	}

	@Override
	public void onInit(LibSequenceRunningSequence sequence) {
		// Not needed here
	}

	@Override
    public void onCancel(LibSequenceRunningSequence sequence) {
		// Not needed here
    }
    
	@Override
    public void onFinish(LibSequenceRunningSequence sequence) {
		// Not needed here
    }

}
