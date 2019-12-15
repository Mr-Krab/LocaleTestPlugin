package mr_krab.localetestplugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import com.google.inject.Inject;

import mr_krab.localeapi.LocaleAPIMain;
import mr_krab.localeapi.utils.LocaleAPI;
import mr_krab.localeapi.utils.LocaleUtil;
import mr_krab.localetestplugin.utils.commands.CommandTest;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

@Plugin(id = "localetestplugin",
		name = "LocaleTestPlugin",
		version = "1.0.0-S7.1",
		dependencies = {
				@Dependency(id = "localeapi", optional = true)
		},
		authors = "Mr_Krab")
public class Main {

	@Inject
	@DefaultConfig(sharedRoot = false)
	private Path defaultConfig;
	@Inject
	@ConfigDir(sharedRoot = false)
	private Path configDir;
	@Inject
	@ConfigDir(sharedRoot = false)
	private File configFile;
	@Inject
	@DefaultConfig(sharedRoot = false)
	private ConfigurationLoader<CommentedConfigurationNode> configLoader;
	private CommentedConfigurationNode rootNode;
	@Inject
	private Logger logger;
	private LocaleAPI localeAPI = null;

	private static Main instance;

	public static Main getInstance() {
		return instance;
	}
	public Logger getLogger() {
		return logger;
	}
	public LocaleAPI getLocaleAPI() {
		return localeAPI;
	}
	public File getConfigFile() {
		return configFile;
	}
	public CommentedConfigurationNode getRootNode() {
		return rootNode;
	}

	@Listener
	public void onPreInitialization(GamePreInitializationEvent event) {
		instance = this;
		load();
	}

	@Listener
	public void onPostInitialization(GamePostInitializationEvent event) {
		localeAPI = LocaleAPIMain.getInstance().getAPI();
		localeAPI.saveLocales(instance);
		localeAPI.saveHoconLocales(instance);
		localeAPI.saveJsonLocales(instance);
		localeAPI.saveYamlLocales(instance);
		commandRegister();
	}
	
	@Listener
	public void onReload(GameReloadEvent event) {
		load();
	}

	public void load() {
		configLoader = HoconConfigurationLoader.builder().setPath(configDir.resolve("config.conf")).build();
		try {
			rootNode = configLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void commandRegister() {
		CommandSpec commandTest = CommandSpec.builder()
		        .executor(new CommandTest())
		        .build();
		
		Sponge.getCommandManager().register(this, commandTest, "test");
	}

	//**** An example to simplify use. || Пример для упрощения использования. ****\\
	public Map<Locale, LocaleUtil> getLocales() {
		return localeAPI.getLocalesMap("localetestplugin");
	}

	public LocaleUtil getLocale(Locale locale) {
		return getLocales().get(locale);
	}

	public LocaleUtil getDefaultLocale() {
		return getLocales().get(localeAPI.getDefaultLocale());
	}

	public LocaleUtil getOrDefaultLocale(Locale locale) {
		if(getLocales().containsKey(locale)) {
			return getLocale(locale);
		}
		return getDefaultLocale();
	}
	
}
