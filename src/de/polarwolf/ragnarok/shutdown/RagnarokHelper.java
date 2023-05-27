package de.polarwolf.ragnarok.shutdown;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.polarwolf.libsequence.runnings.LibSequenceRunningSequence;
import de.polarwolf.libsequence.token.LibSequenceToken;
import de.polarwolf.ragnarok.orchestrator.RagnarokOrchestrator;

public class RagnarokHelper {

	private final LibSequenceToken token;

	protected final Plugin plugin;
	protected RagnarokSequence ragnarokSequence = null;
	protected List<LibSequenceRunningSequence> blockingSequences = new ArrayList<>();

	public RagnarokHelper(RagnarokOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
		this.token = new LibSequenceToken();
	}

	// We use one token for all purpose
	public LibSequenceToken getToken() {
		return token;
	}

	// Protected from calling outside startup
	void setRagnarokSequence(RagnarokSequence newRagnarokSequence) {
		ragnarokSequence = newRagnarokSequence;
	}

	// Dealing with "Blocking new Logins"
	public boolean isBlockNewLogins() {
		return (!blockingSequences.isEmpty());
	}

	public void addBlockingSequence(LibSequenceRunningSequence sequence) {
		if (blockingSequences.isEmpty()) {
			plugin.getLogger().info("Blocking new logins");
		}
		blockingSequences.add(sequence);
	}

	public void removeBlockingSequence(LibSequenceRunningSequence sequence) {
		if (blockingSequences.isEmpty()) {
			return;
		}
		blockingSequences.remove(sequence);
		if (blockingSequences.isEmpty()) {
			plugin.getLogger().info("Remove blocking of new logins");
		}
	}

	// Do the "real" action
	public void kickall() {
		plugin.getLogger().info("Kicking all players from the server");
		List<Player> players = new ArrayList<>(plugin.getServer().getOnlinePlayers());
		for (Player player : players) {
			player.kickPlayer(plugin.getServer().getShutdownMessage());
		}
	}

	public void shutdownServer() {
		plugin.getLogger().info("Performing Server Shutdown");
		plugin.getServer().shutdown();
	}

	public void stopShutdownSequence() {
		if (!isDisabled()) {
			ragnarokSequence.stopShutdownSequence();
		}
	}

	// Manage Plugin disable
	public boolean isDisabled() {
		return (ragnarokSequence == null);
	}

}
