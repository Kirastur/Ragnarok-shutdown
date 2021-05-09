package de.polarwolf.ragnarok.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.polarwolf.libsequence.config.LibSequenceConfigException;
import de.polarwolf.libsequence.exception.LibSequenceException;
import de.polarwolf.libsequence.runnings.LibSequenceRunException;
import de.polarwolf.ragnarok.api.RagnarokAPI;
import de.polarwolf.ragnarok.main.Main;

public class RagnarokCommand implements CommandExecutor {
	
	protected static final String MSG_RELOAD_OK = "Ragnarök sequences successfully reloaded";
	protected static final String MSG_ERROR = "Ragnarök is broken. Please call a viking to repair.";
	protected static final String MSG_COMMING = "Ragnarök is comming";
	protected static final String MSG_SLEEPING = "Ragnarök is sleeping";
	protected static final String MSG_NO_PERMISSIONS = "You don't have any permissions here";
	protected static final String MSG_HELP = "Valid subcommands are: ";
	protected static final String MSG_UNKNOWN_SUBCOMMAND = "Unknown subcommand";
	protected static final String MSG_MISSING_PERMISSION = "You don't have the permission to use this";
	protected static final String MSG_IS_RUNNING = "Another shutdown is already running";
	protected static final String MSG_IS_NOT_RUNNING = "There is no shutdown to cancel";

	protected static final String PERMISSION_PREFIX= "ragnarök.command";
	
	protected final Main main;
	protected final RagnarokAPI ragnarokAPI;
	
	public RagnarokCommand (Main main, RagnarokAPI ragnarokAPI) {
		this.main = main;
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
		return cmds;
	}
	
	protected boolean hasCommandPermission(CommandSender sender, String cmd) {
		return sender.hasPermission(PERMISSION_PREFIX+"."+cmd);			
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
			sender.sendMessage(MSG_NO_PERMISSIONS);
		} else {
			sender.sendMessage(MSG_HELP + s);
		}
	}

	protected void cmdStart(CommandSender sender) throws LibSequenceRunException {
		if (ragnarokAPI.isShutdownRunning()) {
			sender.sendMessage(MSG_IS_RUNNING);
			return;
		}
		if (!ragnarokAPI.startShutdown(sender)) {
			sender.sendMessage(MSG_ERROR);
		}
	}
	
	protected void cmdCancel(CommandSender sender) throws LibSequenceRunException {
		if (!ragnarokAPI.isShutdownRunning()) {
			sender.sendMessage(MSG_IS_NOT_RUNNING);
			return;
		}
		ragnarokAPI.cancelShutdown(sender);
	}
	
	protected void cmdToogle(CommandSender sender) throws LibSequenceRunException {
		if (!ragnarokAPI.toogleShutdown(sender)) {
			sender.sendMessage(MSG_ERROR);
		}
	}
	
	protected void cmdStatus(CommandSender sender) {
		if (ragnarokAPI.isShutdownRunning()) {
			sender.sendMessage(MSG_COMMING);
		} else {
			sender.sendMessage(MSG_SLEEPING);
		}
	}
	
	protected void cmdAbort(CommandSender sender) throws LibSequenceRunException {
		if (!ragnarokAPI.isShutdownRunning()) {
			sender.sendMessage(MSG_IS_NOT_RUNNING);
			return;
		}
		ragnarokAPI.abortShutdown(sender);
	}
	
	protected void cmdReload(CommandSender sender) throws LibSequenceConfigException {
		ragnarokAPI.reload();
		sender.sendMessage(MSG_RELOAD_OK);			
	}
	
	protected boolean dispatchCommand(CommandSender sender, String subCommand) {
		try {
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
			return false;
		} catch (LibSequenceException e) {
			sender.sendMessage(e.getTitle());
			main.getLogger().warning(e.getMessageCascade());
			if (e.hasJavaException()) {
				e.printStackTrace();
			}
		}
		
		return true;		
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
			sender.sendMessage(MSG_UNKNOWN_SUBCOMMAND);
			return true;
		}
		if (!hasCommandPermission(sender, subCommand)) {
			sender.sendMessage(MSG_MISSING_PERMISSION);
			return true;
		}
		
		return dispatchCommand (sender, subCommand);
	}

}
