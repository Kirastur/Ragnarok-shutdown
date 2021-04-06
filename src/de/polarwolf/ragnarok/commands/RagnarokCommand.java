package de.polarwolf.ragnarok.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import de.polarwolf.ragnarok.api.RagnarokAPI;

public class RagnarokCommand implements CommandExecutor {
	
	protected static final String MSG_OK = "OK";
	protected static final String MSG_ERR = "ERROR";
	
	protected final Plugin plugin;
	protected final RagnarokAPI ragnarokAPI;
	
	public RagnarokCommand (Plugin plugin, RagnarokAPI ragnarokAPI) {
		this.plugin = plugin;
		this.ragnarokAPI = ragnarokAPI;
	}
	
	protected List<String> listAllCommands() {
		ArrayList<String> cmds = new ArrayList<>();
		cmds.add("start");
		cmds.add("cancel");
		cmds.add("toggle");
		cmds.add("status");
		cmds.add("abort");
		cmds.add("reload");
		cmds.add("debugenable");
		cmds.add("debugdisable");
		return cmds;
	}
	
	protected boolean hasCommandPermission(CommandSender sender, String cmd) {
		return sender.hasPermission("ragnarök.command."+cmd);			
	}

	protected List<String> filterCommands(CommandSender sender, List<String> rawCommands) {
		ArrayList<String> filteredCommands = new ArrayList<>();		
		for (String commandName: rawCommands) {
			if (hasCommandPermission(sender, commandName)) {
				filteredCommands.add(commandName);
			}
		}
		return filteredCommands;
	}
	
	protected void cmdHelp(CommandSender sender) {
		String s = String.join(" ", filterCommands(sender, listAllCommands()));  
		if (s.isEmpty()) {
			sender.sendMessage("You don't have any permissions here.");
		} else {
			sender.sendMessage("Valid subcommands are: "+s);
		}
	}

	protected void cmdStart(CommandSender sender) {
		if (ragnarokAPI.startShutdown(sender)) {
			sender.sendMessage("Shutdown started");			
		} else {
			sender.sendMessage(MSG_ERR);
		}
	}
	
	protected void cmdCancel(CommandSender sender) {
		if (ragnarokAPI.cancelShutdown(sender)) {
			sender.sendMessage("Cancel accepted");			
		} else {
			sender.sendMessage("MSG_ERR");
		}
	}
	
	protected void cmdToogle(CommandSender sender) {
		if (ragnarokAPI.toogleShutdown(sender)) {
			sender.sendMessage(MSG_OK);			
		} else {
			sender.sendMessage(MSG_ERR);
		}
	}
	
	protected void cmdStatus(CommandSender sender) {
		if (ragnarokAPI.isShutdownRunning()) {
			sender.sendMessage("Ragnarök is comming");
		} else {
			sender.sendMessage("Ragnarök is sleeping");
		}
	}
	
	protected void cmdAbort(CommandSender sender) {
		if (ragnarokAPI.abortShutdown(sender)) {
			sender.sendMessage("Abort accepted");			
		} else {
			sender.sendMessage(MSG_ERR);
		}
	}
	
	protected void cmdReload(CommandSender sender) {
		if (ragnarokAPI.reload()) {
			sender.sendMessage(MSG_OK);			
		} else {
			sender.sendMessage(MSG_ERR);
		}
	}
	
	protected void cmdDebugEnable(CommandSender sender) {
		ragnarokAPI.debugEnable();
		sender.sendMessage(MSG_OK);			
	}
	
	protected void cmdDebugDisable(CommandSender sender) {
		ragnarokAPI.debugDisable();
		sender.sendMessage(MSG_OK);			
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length==0) {
			return false;
		}
		String subCommand=args[0];
		if (subCommand.equalsIgnoreCase("help")) {
			cmdHelp(sender);
			return true;
		}
		if (!listAllCommands().contains(subCommand)) {
			sender.sendMessage("Unknown subcommand");
			return true;
		}
		if (!hasCommandPermission(sender, subCommand)) {
			sender.sendMessage("You don't have the permission to use this");
			return true;
		}
		if (subCommand.equalsIgnoreCase("start")) {
			cmdStart(sender);
			return true;
		}
		if (subCommand.equalsIgnoreCase("cancel")) {
			cmdCancel(sender);
			return true;
		}
		if (subCommand.equalsIgnoreCase("toggle")) {
			cmdToogle(sender);
			return true;
		}
		if (subCommand.equalsIgnoreCase("status")) {
			cmdStatus(sender);
			return true;
		}
		if (subCommand.equalsIgnoreCase("abort")) {
			cmdAbort(sender);
			return true;
		}
		if (subCommand.equalsIgnoreCase("reload")) {
			cmdReload(sender);
			return true;
		}
		if (subCommand.equalsIgnoreCase("debugenable")) {
			cmdDebugEnable(sender);
			return true;
		}
		if (subCommand.equalsIgnoreCase("debugdisable")) {
			cmdDebugDisable(sender);
			return true;
		}
		return false;
	}

}
