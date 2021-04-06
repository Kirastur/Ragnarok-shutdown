package de.polarwolf.ragnarok.main;

import org.bukkit.plugin.java.JavaPlugin;

import de.polarwolf.ragnarok.actions.RagnarokActionBlockNewLogins;
import de.polarwolf.ragnarok.actions.RagnarokActionKickall;
import de.polarwolf.ragnarok.actions.RagnarokActionShutdown;
import de.polarwolf.ragnarok.api.RagnarokAPI;
import de.polarwolf.ragnarok.bstats.MetricsLite;
import de.polarwolf.ragnarok.commands.RagnarokCommand;
import de.polarwolf.ragnarok.commands.RagnarokTabCompleter;
import de.polarwolf.ragnarok.config.RagnarokConfig;
import de.polarwolf.ragnarok.events.RagnarokEventPlayer;
import de.polarwolf.ragnarok.sequences.RagnarokSequence;
import de.polarwolf.ragnarok.tools.RagnarokTools;

public final class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		
		// Load Config
		RagnarokConfig ragnarokConfig = new RagnarokConfig(this);
		
		// Initialize Tools
		RagnarokTools ragnarokTools = new RagnarokTools(this, ragnarokConfig);
		
		// Initialize Sequence
		RagnarokSequence ragnarokSequence = new RagnarokSequence(this, ragnarokConfig);
		ragnarokSequence.registerAction("block-new-logins", new RagnarokActionBlockNewLogins(this, ragnarokConfig, ragnarokTools, ragnarokSequence));
		ragnarokSequence.registerAction("kickall", new RagnarokActionKickall(this, ragnarokConfig, ragnarokTools, ragnarokSequence));
		ragnarokSequence.registerAction("shutdown", new RagnarokActionShutdown(this, ragnarokConfig, ragnarokTools, ragnarokSequence));
		boolean isSequenceReady = ragnarokSequence.loadSequence();
		
		// Register Event Handler
		getServer().getPluginManager().registerEvents(new RagnarokEventPlayer(this, ragnarokConfig), this);
		
		// Now put them all together in the API object
		RagnarokAPI ragnarokAPI = new RagnarokAPI(this, ragnarokConfig, ragnarokTools, ragnarokSequence);
		
		// Initialize Ragnarok Provider
		if (ragnarokConfig.isEnableAPI()) {
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

		// Now all is done and we can print the finish message
		if (isSequenceReady) {
			getLogger().info("Ragnarök is prepared, but fortunately far away...");
		} else {
			getLogger().info("Ragnarök is broken, please call a viking to repair...");			
		}
	}

}
