package de.polarwolf.ragnarok.main;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import de.polarwolf.libsequence.exception.LibSequenceException;
import de.polarwolf.ragnarok.actions.RagnarokActionBlockNewLogins;
import de.polarwolf.ragnarok.actions.RagnarokActionCancel;
import de.polarwolf.ragnarok.actions.RagnarokActionKickall;
import de.polarwolf.ragnarok.actions.RagnarokActionShutdown;
import de.polarwolf.ragnarok.api.RagnarokAPI;
import de.polarwolf.ragnarok.bstats.MetricsLite;
import de.polarwolf.ragnarok.commands.RagnarokCommand;
import de.polarwolf.ragnarok.commands.RagnarokTabCompleter;
import de.polarwolf.ragnarok.events.RagnarokEventPlayer;
import de.polarwolf.ragnarok.sequences.RagnarokSequence;
import de.polarwolf.ragnarok.tools.RagnarokTools;

public final class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		
		// Prepare Config
		saveDefaultConfig();

		// Initialize Tools
		RagnarokTools ragnarokTools = new RagnarokTools(this);
		
		try {
		
		// Initialize Sequence
			RagnarokSequence ragnarokSequence = new RagnarokSequence(this, ragnarokTools);
			ragnarokSequence.registerAction("block-new-logins", new RagnarokActionBlockNewLogins(this, ragnarokTools, ragnarokSequence, ragnarokSequence.getAuthorizationKeyShutdown()));
			ragnarokSequence.registerAction("kickall", new RagnarokActionKickall(this, ragnarokTools, ragnarokSequence, ragnarokSequence.getAuthorizationKeyShutdown()));
			ragnarokSequence.registerAction("shutdown", new RagnarokActionShutdown(this, ragnarokTools, ragnarokSequence, ragnarokSequence.getAuthorizationKeyShutdown()));
			ragnarokSequence.registerAction("cancelshutdown", new RagnarokActionCancel(this, ragnarokTools, ragnarokSequence, ragnarokSequence.getAuthorizationKeyCancel()));
		
			// Register Event Handler
			getServer().getPluginManager().registerEvents(new RagnarokEventPlayer(this, ragnarokTools), this);
		
			// Now put them all together in the API object
			RagnarokAPI ragnarokAPI = new RagnarokAPI(ragnarokTools, ragnarokSequence);
		
			// Initialize Ragnarok Provider
			if (ragnarokTools.isEnableAPI()) {
				RagnarokProvider.setAPI(ragnarokAPI);
			}

			// Register Command and TabCompleter
			RagnarokCommand ragnarokCommand = new RagnarokCommand(this, ragnarokAPI);
			getCommand("ragnarök").setExecutor(ragnarokCommand);
			getCommand("ragnarök").setTabCompleter(new RagnarokTabCompleter(ragnarokCommand));
		
			// Enable bStats Metrics
			// Please download the bstats-code direct form their homepage
			// or disable the following instruction
			new MetricsLite(this, MetricsLite.PLUGINID_RAGNAROK);

			// All is prepared, now we can load the sequence section
			ragnarokSequence.loadSequences();

			// Now all is done and we can print the finish message
			getLogger().info("Ragnarök is prepared, but fortunately far away...");

		} catch (LibSequenceException e) { 
			getServer().getConsoleSender().sendMessage(ChatColor.RED + "ERROR " + e.getMessageCascade());
			getLogger().info("Check your Ragnaröke plugin.yml or delete it, so a fresh one is created on the next server start");
			getLogger().warning("Ragnarök is out of service. The game will never end...");
		}	
	}

}
