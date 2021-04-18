package de.polarwolf.ragnarok.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.Plugin;

import de.polarwolf.ragnarok.tools.RagnarokTools;

public class RagnarokEventPlayer implements Listener {
	
	protected final Plugin plugin;
	protected final RagnarokTools ragnarokTools;
	
	public RagnarokEventPlayer(Plugin plugin, RagnarokTools ragnarokTools) {
		this.plugin = plugin;
		this.ragnarokTools = ragnarokTools;
	}

	@EventHandler
	public void onPlayerJoin(PlayerLoginEvent evt) {
		if (ragnarokTools.isBlockNewLogins()) {
			plugin.getLogger().info("Blocking player login: " + evt.getPlayer().getName());	
			evt.disallow(PlayerLoginEvent.Result.KICK_OTHER, plugin.getServer().getShutdownMessage());
		}
	}

}
