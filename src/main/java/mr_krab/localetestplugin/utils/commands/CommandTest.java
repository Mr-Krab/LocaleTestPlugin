package mr_krab.localetestplugin.utils.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

import mr_krab.localetestplugin.Main;

public class CommandTest implements CommandExecutor {

	private static final Main plugin = Main.getInstance();
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		src.sendMessage(plugin.getLocaleAPI().getOrDefaultLocale(plugin, src.getLocale()).getString("test.success1"));
		src.sendMessage(plugin.getLocales().get(src.getLocale()).getString("test.success2"));
		src.sendMessage(plugin.getLocale(src.getLocale()).getString("test.success3"));
		src.sendMessage(plugin.getOrDefaultLocale(src.getLocale()).getString("test.success4"));
		return CommandResult.success();
	}

}