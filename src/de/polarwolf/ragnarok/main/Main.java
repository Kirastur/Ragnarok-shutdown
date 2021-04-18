package de.polarwolf.ragnarok.main;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

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
		
		// Initialize Sequence
		RagnarokSequence ragnarokSequence = new RagnarokSequence(this, ragnarokTools);
		ragnarokSequence.registerAction("block-new-logins", new RagnarokActionBlockNewLogins(this, ragnarokTools, ragnarokSequence));
		ragnarokSequence.registerAction("kickall", new RagnarokActionKickall(this, ragnarokTools, ragnarokSequence));
		ragnarokSequence.registerAction("shutdown", new RagnarokActionShutdown(this, ragnarokTools, ragnarokSequence));
		ragnarokSequence.registerAction("cancelshutdown", new RagnarokActionCancel(this, ragnarokTools, ragnarokSequence));
		
		// All is prepared, now we can load the sequence section
		ragnarokSequence.loadSequences();
		
		// Register Event Handler
		getServer().getPluginManager().registerEvents(new RagnarokEventPlayer(this, ragnarokTools), this);
		
		// Now put them all together in the API object
		RagnarokAPI ragnarokAPI = new RagnarokAPI(this, ragnarokTools, ragnarokSequence);
		
		// Initialize Ragnarok Provider
		if (ragnarokTools.isEnableAPI()) {
			RagnarokProvider.setAPI(ragnarokAPI);
		}

		// Register Command and TabCompleter
		RagnarokCommand ragnarokCommand = new RagnarokCommand(ragnarokAPI);
		getCommand("ragnarök").setExecutor(ragnarokCommand);
		getCommand("ragnarök").setTabCompleter(new RagnarokTabCompleter(ragnarokCommand));
		
		// Enable bStats Metrics
		// Please download the bstats-code direct form their homepage
		// or disable the following instruction
		new MetricsLite(this, MetricsLite.PLUGINID_RAGNAROK);

		// Now all is done and we can print the finish message
		if (ragnarokAPI.isReady()) {
			getLogger().info("Ragnarök is prepared, but fortunately far away...");
		} else {
			getServer().getConsoleSender().sendMessage(ChatColor.RED + "Ragnarök is out of service. The game will never end...");			
		}
	}

}
