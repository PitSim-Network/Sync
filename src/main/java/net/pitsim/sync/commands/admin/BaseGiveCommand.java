package net.pitsim.sync.commands.admin;

import dev.kyro.arcticapi.commands.ABaseCommand;
import dev.kyro.arcticapi.misc.AOutput;
import org.bukkit.command.CommandSender;

import java.util.List;

public class BaseGiveCommand extends ABaseCommand {
	public BaseGiveCommand(ABaseCommand baseCommand, String executor) {
		super(baseCommand, executor);
	}

	@Override
	public void executeBase(CommandSender sender, List<String> args) {
		if(!sender.isOp()) return;
		for(String line : createHelp().getMessage()) AOutput.sendIfPlayer(sender, line);
	}

	@Override
	public void executeFail(CommandSender sender, List<String> args) {
		executeBase(sender, args);
	}
}
