package de.polarwolf.ragnarok.tools;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.polarwolf.libsequence.runnings.LibSequenceRunningSequence;

public class RagnarokTools {
	
	public static final String CONFIG_ENABLE_API = "general.enableAPI";
	public static final String CONFIG_DEBUG_OUTPUT = "general.enableDebugOutput";
	
	protected final Plugin plugin;
	protected List<LibSequenceRunningSequence> blockingSequences = new ArrayList<>();

	public RagnarokTools(Plugin plugin) {
		this.plugin = plugin;
	}
	
	// Query data from config
	public boolean isEnableAPI() {
		return plugin.getConfig().getBoolean(CONFIG_ENABLE_API);
	}
	
	public boolean isDebugOutput() {
		return plugin.getConfig().getBoolean(CONFIG_DEBUG_OUTPUT);
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
		for (Player player: players) {
			player.kickPlayer(plugin.getServer().getShutdownMessage());
		}
	}

	public void shutdownServer() {
		plugin.getLogger().info("Performing Server Shutdown");
		plugin.getServer().shutdown();
	}

}
