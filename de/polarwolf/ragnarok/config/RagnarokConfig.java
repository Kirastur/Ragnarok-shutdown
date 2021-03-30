package de.polarwolf.ragnarok.config;

import org.bukkit.plugin.Plugin;

public class RagnarokConfig {
	
	protected String regionName;
	protected String permissionName;
	protected boolean actionsArePrivate;
	protected boolean enableAPI;
	protected boolean blockNewLogins = false;
	protected boolean debug = false;

	protected final Plugin plugin;
 
	public RagnarokConfig(Plugin plugin) {
		this.plugin = plugin;
		loadConfig();
	}
	
	public void loadConfig() {
		plugin.saveDefaultConfig();
		enableAPI = plugin.getConfig().getBoolean("general.enable-api");
		actionsArePrivate = plugin.getConfig().getBoolean("general.actions-are-private");
		regionName = plugin.getConfig().getString("protection.region");
		permissionName = plugin.getConfig().getString("protection.permission");
	}
	
	public String getRegionName() {
		return regionName;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public boolean isEnableAPI() {
		return enableAPI;
	}

	public boolean isActionsArePrivate() {
		return actionsArePrivate;
	}

	public boolean isBlockNewLogins() {
		return blockNewLogins;
	}

	public void setBlockNewLogins(boolean blockNewLogins) {
		this.blockNewLogins = blockNewLogins;
	}
		
	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
}
