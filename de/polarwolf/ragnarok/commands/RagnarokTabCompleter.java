package de.polarwolf.ragnarok.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class RagnarokTabCompleter implements TabCompleter{
	
	protected final RagnarokCommand ragnarokCommand;
	
	public RagnarokTabCompleter(RagnarokCommand ragnarokCommand) {
		this.ragnarokCommand = ragnarokCommand;
	}

	protected List<String> listCommands(CommandSender sender) {
		  return ragnarokCommand.filterCommands(sender, ragnarokCommand.listAllCommands());
		}
		
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length==1) {
			return listCommands(sender);
		}
		return new ArrayList<>();
	}

}
