package de.polarwolf.ragnarok.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.polarwolf.libsequence.exception.LibSequenceException;
import de.polarwolf.libsequence.runnings.LibSequenceRunException;
import de.polarwolf.ragnarok.api.RagnarokProvider;
import de.polarwolf.ragnarok.main.Main;
import de.polarwolf.ragnarok.orchestrator.RagnarokAPI;

public class RagnarokCommand implements CommandExecutor {

	public static final String CMD_START = "start";
	public static final String CMD_CANCEL = "cancel";
	public static final String CMD_TOGGLE = "toggle";
	public static final String CMD_STATUS = "status";
	public static final String CMD_ABORT = "abort";
	public static final String CMD_RELOAD = "reload";
	public static final String CMD_HELP = "help";

	protected static final String MSG_COMMING = "Ragnarök is comming";
	protected static final String MSG_SLEEPING = "Ragnarök is sleeping";
	protected static final String MSG_HELP = "Valid actions are: ";
	protected static final String MSG_RELOAD_OK = "Ragnarök sequences successfully reloaded";
	protected static final String MSG_IS_RUNNING = "Another shutdown is already running";
	protected static final String MSG_IS_NOT_RUNNING = "There is no shutdown to cancel";
	protected static final String MSG_MISSING_PERMISSION = "You don't have the permission to use this";
	protected static final String MSG_NO_PERMISSIONS = "You don't have any permissions here";
	protected static final String MSG_UNKNOWN_ACTION = "Unknown action";
	protected static final String MSG_ERROR = "Ragnarök is broken. Please call a viking to repair.";
	protected static final String MSG_NO_API = "Nobody believes in Ragnarok so he left...";

	protected static final String PERMISSION_PREFIX = "ragnarök.command";

	protected final Main main;
	protected final String commandName;
	protected final RagnarokTabCompleter tabCompleter;

	public RagnarokCommand(Main main, String commandName) {
		this.main = main;
		this.commandName = commandName;
		main.getCommand(commandName).setExecutor(this);
		tabCompleter = new RagnarokTabCompleter(main, this);
	}

	public String getCommandName() {
		return commandName;
	}

	protected RagnarokAPI getRagnarokAPI() {
		return RagnarokProvider.getAPI();
	}

	protected List<String> listAllCommandActions() {
		ArrayList<String> cmds = new ArrayList<>();
		cmds.add(CMD_START);
		cmds.add(CMD_CANCEL);
		cmds.add(CMD_TOGGLE);
		cmds.add(CMD_STATUS);
		cmds.add(CMD_ABORT);
		cmds.add(CMD_RELOAD);
		return cmds;
	}

	protected boolean hasCommandPermission(CommandSender sender, String cmd) {
		return sender.hasPermission(PERMISSION_PREFIX + "." + cmd);
	}

	protected List<String> filterCommandActions(CommandSender sender, List<String> rawCommandActions) {
		ArrayList<String> filteredCommandActions = new ArrayList<>();
		for (String myCommandActionName : rawCommandActions) {
			if (hasCommandPermission(sender, myCommandActionName)) {
				filteredCommandActions.add(myCommandActionName);
			}
		}
		return filteredCommandActions;
	}

	protected void cmdHelp(CommandSender sender) {
		String s = String.join(" ", filterCommandActions(sender, listAllCommandActions()));
		if (s.isEmpty()) {
			sender.sendMessage(MSG_NO_PERMISSIONS);
		} else {
			sender.sendMessage(MSG_HELP + s);
		}
	}

	protected void cmdStart(CommandSender sender) throws LibSequenceRunException {
		if (getRagnarokAPI().isShutdownRunning()) {
			sender.sendMessage(MSG_IS_RUNNING);
			return;
		}
		if (!getRagnarokAPI().startShutdown(sender)) {
			sender.sendMessage(MSG_ERROR);
		}
	}

	protected void cmdCancel(CommandSender sender) throws LibSequenceRunException {
		if (!getRagnarokAPI().isShutdownRunning()) {
			sender.sendMessage(MSG_IS_NOT_RUNNING);
			return;
		}
		getRagnarokAPI().cancelShutdown(sender);
	}

	protected void cmdToogle(CommandSender sender) throws LibSequenceRunException {
		if (!getRagnarokAPI().toogleShutdown(sender)) {
			sender.sendMessage(MSG_ERROR);
		}
	}

	protected void cmdStatus(CommandSender sender) {
		if (getRagnarokAPI().isShutdownRunning()) {
			sender.sendMessage(MSG_COMMING);
		} else {
			sender.sendMessage(MSG_SLEEPING);
		}
	}

	protected void cmdAbort(CommandSender sender) throws LibSequenceRunException {
		if (!getRagnarokAPI().isShutdownRunning()) {
			sender.sendMessage(MSG_IS_NOT_RUNNING);
			return;
		}
		getRagnarokAPI().abortShutdown(sender);
	}

	protected void cmdReload(CommandSender sender) throws LibSequenceException {
		getRagnarokAPI().reload();
		sender.sendMessage(MSG_RELOAD_OK);
	}

	protected boolean dispatchCommand(CommandSender sender, String commandAction) {
		try {
			if (commandAction.equalsIgnoreCase(CMD_START)) {
				cmdStart(sender);
				return true;
			}
			if (commandAction.equalsIgnoreCase(CMD_CANCEL)) {
				cmdCancel(sender);
				return true;
			}
			if (commandAction.equalsIgnoreCase(CMD_TOGGLE)) {
				cmdToogle(sender);
				return true;
			}
			if (commandAction.equalsIgnoreCase(CMD_STATUS)) {
				cmdStatus(sender);
				return true;
			}
			if (commandAction.equalsIgnoreCase(CMD_ABORT)) {
				cmdAbort(sender);
				return true;
			}
			if (commandAction.equalsIgnoreCase(CMD_RELOAD)) {
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
		if (getRagnarokAPI() == null) {
			sender.sendMessage(MSG_NO_API);
			return true;
		}
		if (args.length == 0) {
			return false;
		}
		String commandAction = args[0];
		if (commandAction.equalsIgnoreCase(CMD_HELP)) {
			cmdHelp(sender);
			return true;
		}
		if (!listAllCommandActions().contains(commandAction)) {
			sender.sendMessage(MSG_UNKNOWN_ACTION);
			return true;
		}
		if (!hasCommandPermission(sender, commandAction)) {
			sender.sendMessage(MSG_MISSING_PERMISSION);
			return true;
		}
		return dispatchCommand(sender, commandAction);
	}

}
