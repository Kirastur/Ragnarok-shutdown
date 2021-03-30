package de.polarwolf.ragnarok.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.polarwolf.ragnarok.config.RagnarokConfig;

public class RagnarokTools {
	
	protected final Plugin plugin;
	protected final RagnarokConfig config;

	public RagnarokTools(Plugin plugin, RagnarokConfig config) {
		this.plugin = plugin;
		this.config = config;
	}
	
    protected void printWarning(String warningText) {
		plugin.getLogger().warning(warningText);
    }
    
    protected void printInfo(String infoText) {
    	plugin.getLogger().info(infoText);
    }
    
    protected void printDebug (String debugText) {
    	if (config.isDebug()) {
    		printInfo("Debug: "+debugText);
    	}
    }
    
    protected void printNotFound (String errorText, String errorParameter) {
		printWarning(errorText+" \""+errorParameter+"\" not found.");    	
    }
    
	// We use a dedicated array here because we want to do a "safe consequential iteration" later on 
	protected List<Player> enumPlayers() {
		List<Player> players = new ArrayList<>();
		for (Player player : plugin.getServer().getOnlinePlayers()) {
			players.add(player);
		}
		return players;
	}
	
	protected Map<World, ProtectedRegion> enumRegions(String regionName) {
		Map<World, ProtectedRegion> regionMap = new HashMap<>();
		for (World world : plugin.getServer().getWorlds()) {
			ProtectedRegion region = WorldGuard
	 					.getInstance()
	 					.getPlatform()
	 					.getRegionContainer()
	 					.get(BukkitAdapter.adapt(world))
	 					.getRegion(regionName);
			if (region != null) {
				regionMap.put(world, region);
			}
		}
		return regionMap;
	}
	
	protected boolean isPlayerAuthorized(Player player, Map<World, ProtectedRegion> regionMap, String neededPermissionName) {
		
		// Step one: Has player the permission?
    	if (!player.hasPermission(neededPermissionName)) {
   			printDebug("Player doesn't have the permission");
    		return false;
    	}
    	
    	// Step two: Does a region exists in the world the player is in?
    	ProtectedRegion region = regionMap.get(player.getWorld());
    	if (region==null) {
   			printDebug("No region in player's world");
    		return false;
    	}
   
    	// Step three: Does the player stay inside the region?
    	Location location = player.getLocation();
    	BlockVector3 bloc = BlockVector3.at(location.getX(), location.getY(), location.getZ());
    	if (!region.contains (bloc)) {
   			printDebug("Player is not in region");
    		return false;
    	}

		printDebug("All criteria are fullfilled");
    	return true;
    }
	
	public boolean isCommandAuthorized() {
		String regionName = config.getRegionName();
		String neededPermissionName = config.getPermissionName();

		// Get matching WorldGuard regions
		Map<World, ProtectedRegion> regionMap = enumRegions(regionName);
		if (regionMap.isEmpty()) {
			printNotFound("WorldGuard Region", regionName);
			return false;
		}
		
		// Loop for all players
		printDebug("Checking all players");
		for (Player player : enumPlayers()) {

			printDebug("Checking player "+player.getName());
			if (isPlayerAuthorized(player, regionMap, neededPermissionName)) {
				printDebug("Command is authorized by at least one player");
				return true;
			}
		}
		printDebug("Sorry, no player found to authorize the command");
		return false;
    }
	
	public void shutdownServer() {
		printDebug("shutdownServer called");
		plugin.getServer().shutdown();
	}

	public void kickall() {
		printDebug("kickall called");
		for (Player player : enumPlayers()) {
			player.kickPlayer(plugin.getServer().getShutdownMessage());
		}
	}

	public void blockNewLogins() {
		printDebug("blockNewLogins called");
		config.setBlockNewLogins(true);
	}

	public void unblockNewLogins() {
		printDebug("unblockNewLogins called");
		config.setBlockNewLogins(false);
	}

	public void setDebug (boolean newDebug) {
		config.setDebug(newDebug);
	}

}
