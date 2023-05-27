package de.polarwolf.ragnarok.shutdown;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.Plugin;

import de.polarwolf.ragnarok.orchestrator.RagnarokOrchestrator;

public class RagnarokListener implements Listener {

	protected final Plugin plugin;
	protected final RagnarokHelper ragnarokHelper;

	public RagnarokListener(RagnarokOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
		this.ragnarokHelper = orchestrator.getRagnarokHelper();
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onPlayerJoin(PlayerLoginEvent event) {
		if (!ragnarokHelper.isDisabled() && ragnarokHelper.isBlockNewLogins()) {
			plugin.getLogger().info("Blocking player login: " + event.getPlayer().getName());
			event.disallow(PlayerLoginEvent.Result.KICK_OTHER, plugin.getServer().getShutdownMessage());
		}
	}

	public void disable() {
		HandlerList.unregisterAll(this);
	}

}
