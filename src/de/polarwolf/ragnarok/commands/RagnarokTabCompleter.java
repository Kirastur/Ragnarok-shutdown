package de.polarwolf.ragnarok.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import de.polarwolf.ragnarok.main.Main;

public class RagnarokTabCompleter implements TabCompleter {

	protected final RagnarokCommand command;

	public RagnarokTabCompleter(Main main, RagnarokCommand command) {
		this.command = command;
		main.getCommand(command.getCommandName()).setTabCompleter(this);
	}

	protected List<String> listCommands(CommandSender sender) {
		return command.filterCommandActions(sender, command.listAllCommandActions());
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			return listCommands(sender);
		}
		return new ArrayList<>();
	}

}
