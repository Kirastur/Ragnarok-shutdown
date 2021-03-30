package de.polarwolf.ragnarok.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.Plugin;

import de.polarwolf.ragnarok.config.RagnarokConfig;

public class RagnarokEventPlayer implements Listener {
	
	protected final Plugin plugin;
	protected final RagnarokConfig ragnarokConfig;
	
	public RagnarokEventPlayer(Plugin plugin, RagnarokConfig ragnarokConfig) {
		this.plugin = plugin;
		this.ragnarokConfig = ragnarokConfig;
	}

	@EventHandler
	public void onPlayerJoin(PlayerLoginEvent evt) {
		if (ragnarokConfig.isBlockNewLogins()) {
			plugin.getLogger().info("Blocking player login");	
			evt.disallow(PlayerLoginEvent.Result.KICK_OTHER, plugin.getServer().getShutdownMessage());
		}
	}

}
