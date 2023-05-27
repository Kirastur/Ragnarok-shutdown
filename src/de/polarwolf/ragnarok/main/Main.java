package de.polarwolf.ragnarok.main;

import java.util.UUID;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import de.polarwolf.libsequence.api.LibSequenceProvider;
import de.polarwolf.libsequence.orchestrator.LibSequenceSequencer;
import de.polarwolf.libsequence.orchestrator.LibSequenceStartOptions;
import de.polarwolf.libsequence.token.LibSequenceToken;
import de.polarwolf.ragnarok.api.RagnarokProvider;
import de.polarwolf.ragnarok.commands.RagnarokCommand;
import de.polarwolf.ragnarok.orchestrator.RagnarokAPI;

public final class Main extends JavaPlugin {

	public static final int PLUGINID_RAGNAROK = 10856;
	public static final String COMMAND_NAME = "ragnarök";

	public static final String MODE_NONE = "none";
	public static final String MODE_SHARED = "shared";
	public static final String MODE_PRIVATE = "private";

	public static final String RAGNAROK_ERROR = "Ragnarök is out of service. The game will never end...";

	protected String operationMode = "";
	protected UUID apiToken = null;
	protected LibSequenceToken libSequenceToken = null;
	protected RagnarokAPI ragnarokAPI = null;
	protected LibSequenceSequencer sequencerAPI = null;

	@Override
	public void onEnable() {

		// Prepare Config
		saveDefaultConfig();

		// Generate our API Token
		apiToken = UUID.randomUUID();

		// Enable bStats Metrics
		new Metrics(this, PLUGINID_RAGNAROK);

		// Read startup config
		operationMode = getConfig().getString("general.operationMode");
		if (!operationMode.equals(MODE_NONE) && !operationMode.equals(MODE_SHARED)
				&& !operationMode.equals(MODE_PRIVATE)) {
			getLogger().warning("Unknown Ragnarök operating mode");
			getLogger().warning(RAGNAROK_ERROR);
			return;
		}

		// Register Command and TabCompleter
		new RagnarokCommand(this, COMMAND_NAME);

		// Check if we should start the API
		if (operationMode.equals(MODE_NONE)) {
			getLogger().info("Ragnarök is in passive mode.");
			return;
		}

		// Get the LibSequence sequencer
		if (operationMode.equals(MODE_SHARED)) {
			if (LibSequenceProvider.getAPI() == null) {
				getLogger().warning("LibSequenceAPI not found");
				getLogger().warning(RAGNAROK_ERROR);
				return;
			}
			sequencerAPI = LibSequenceProvider.getAPI().getSequencer();
			if (sequencerAPI == null) {
				getLogger().warning("LibSequence sequenceAPI not found");
				getLogger().warning(RAGNAROK_ERROR);
				return;
			}
		}

		if (operationMode.equals(MODE_PRIVATE)) {
			libSequenceToken = new LibSequenceToken();
			LibSequenceStartOptions startOptions = new LibSequenceStartOptions(5, true, true);
			try {
				sequencerAPI = new LibSequenceSequencer(this, libSequenceToken, startOptions);
			} catch (Exception e) {
				e.printStackTrace();
				getLogger().warning("Failed to start private LibSequence sequencer");
				getLogger().warning(RAGNAROK_ERROR);
				return;
			}
		}

		// Build the Ragnarök API
		try {
			ragnarokAPI = new RagnarokAPI(this, apiToken, sequencerAPI);
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().warning("Failed to start Ragnarök orchestrator");
			getLogger().warning(RAGNAROK_ERROR);
			if (operationMode.equals(MODE_PRIVATE)) {
				sequencerAPI.disable(libSequenceToken);
			}
			return;
		}

		// Register API to Provider
		RagnarokProvider.setAPI(ragnarokAPI);

		// Now all is done and we can print the finish message
		getLogger().info("Ragnarök is prepared, but fortunately far away...");
	}

	@Override
	public void onDisable() {
		if ((ragnarokAPI != null) && !ragnarokAPI.disable(apiToken)) {
			getLogger().warning("Could not shutdown orchestrator");
		}
		if (operationMode.equals(MODE_PRIVATE) && (sequencerAPI != null) && !sequencerAPI.disable(libSequenceToken)) {
			getLogger().warning("Could not shutdown private LibSequence Instance");
		}
		ragnarokAPI = null;
		sequencerAPI = null;
		LibSequenceProvider.setAPI(null);
	}

}
