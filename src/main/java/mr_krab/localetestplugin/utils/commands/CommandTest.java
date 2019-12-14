package mr_krab.localetestplugin.utils.commands;

import java.util.Locale;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.util.TypeTokens;

import mr_krab.localetestplugin.Main;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class CommandTest implements CommandExecutor {

	private static final Main plugin = Main.getInstance();
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		src.sendMessage(plugin.getLocaleAPI().getOrDefaultLocale(plugin, src.getLocale()).getString("test.success1"));
		src.sendMessage(plugin.getLocales().get(src.getLocale()).getString("test.success2"));
		src.sendMessage(plugin.getLocale(src.getLocale()).getString("test.success3"));
		src.sendMessage(plugin.getOrDefaultLocale(src.getLocale()).getString("test.success4"));
		src.sendMessage(
				TextSerializers.FORMATTING_CODE.deserialize(
						plugin.getLocaleAPI().getHoconLocale(plugin, src.getLocale()).getLocaleNode().getNode("HoconTest1").getString()
						)
				);
		plugin.getLocaleAPI().createHoconLocale(plugin, Locale.US);
		Text text = Text.of(TextColors.AQUA, "Test 1 json locale success!");
		Text text2 = Text.of(TextColors.RED, "Test 1 yaml locale success!");
		System.out.println(plugin.getLocaleAPI().jsonLocaleLoaded(plugin, Locale.US));
		if(!plugin.getLocaleAPI().jsonLocaleLoaded(plugin, Locale.US) && !plugin.getLocaleAPI().jsonLocaleExist(plugin, Locale.US)) {
			plugin.getLocaleAPI().createJsonLocale(plugin, Locale.US);
			try {
				plugin.getLocaleAPI().getJsonLocale(plugin, Locale.US).getLocaleNode().getNode("JsonTest1").setValue(TypeTokens.TEXT_TOKEN, text);
			} catch (ObjectMappingException e) {
				e.printStackTrace();
			}
			plugin.getLocaleAPI().getJsonLocale(plugin, Locale.US).saveLocaleNode();
		}
		if(!plugin.getLocaleAPI().yamlLocaleLoaded(plugin, Locale.US)) {
			plugin.getLocaleAPI().createYamlLocale(plugin, Locale.US);
			try {
				plugin.getLocaleAPI().getYamlLocale(plugin, Locale.US).getLocaleNode().getNode("YamlTest1").setValue(TypeTokens.TEXT_TOKEN, text2);
			} catch (ObjectMappingException e) {
				e.printStackTrace();
			}
			plugin.getLocaleAPI().getYamlLocale(plugin, Locale.US).saveLocaleNode();
		}
		try {
			src.sendMessage(plugin.getLocaleAPI().getJsonLocale(plugin, Locale.US).getLocaleNode().getNode("JsonTest1").getValue(TypeTokens.TEXT_TOKEN));
		} catch (ObjectMappingException e) {
			e.printStackTrace();
		}
		try {
			src.sendMessage(plugin.getLocaleAPI().getYamlLocale(plugin, Locale.US).getLocaleNode().getNode("YamlTest1").getValue(TypeTokens.TEXT_TOKEN));
		} catch (ObjectMappingException e) {
			e.printStackTrace();
		}
		return CommandResult.success();
	}

}