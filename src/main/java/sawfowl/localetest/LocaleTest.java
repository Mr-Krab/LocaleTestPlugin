package sawfowl.localetest;

import java.util.Arrays;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.util.locale.Locales;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import com.google.inject.Inject;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import sawfowl.localeapi.api.ConfigTypes;
import sawfowl.localeapi.api.LocaleService;
import sawfowl.localeapi.event.LocaleServiseEvent;
import sawfowl.localeapi.serializetools.SerializedItemStack;
import sawfowl.localeapi.serializetools.TypeTokens;
import sawfowl.localeapi.utils.AbstractLocaleUtil;

@Plugin("localetest")
public class LocaleTest {
	private Logger logger;
	private boolean saveHocon = false;
	private boolean saveYaml = false;
	private boolean saveJson = false;
	private boolean saveProperties = false;

	private LocaleTest instance;
	private LocaleService api;
	private PluginContainer pluginContainer;

	public LocaleService getAPI() {
		return api;
	}

	@Inject
	public LocaleTest(PluginContainer pluginContainer) {
		this.pluginContainer = pluginContainer;
		instance = this;
		logger = LogManager.getLogger("PluginForTestLocales");
	}

	@Listener
	public void onLocaleServisePostEvent(LocaleServiseEvent.Construct event) {
		api = event.getLocaleService();
		if(!api.localesExist(instance)) {
			api.saveAssetLocales(instance);
			logger.info("Total asset locales created/saved -> " + api.getPluginLocales(instance).size());
			// When creating localizations, be sure to create a default localization - Locales.DEFAULT.
			// If the above check is performed, the localization creation will not be performed because it is unnecessary.
			api.createPluginLocale(instance, ConfigTypes.HOCON, Locales.DEFAULT);
			api.createPluginLocale(instance, ConfigTypes.YAML, Locales.EN_CA);
			api.createPluginLocale("localetest", ConfigTypes.JSON, Locales.EN_GB);
			api.createPluginLocale("localetest", ConfigTypes.PROPERTIES, Locales.RU_RU);
		}
	}

	@Listener
	public void onEnable(StartedEngineEvent<Server> event) throws ConfigurateException {
		testWrite();
		// The Sponge configuration works in asynchronous mode. If reading is performed immediately after writing, a delay must be made.
		Sponge.asyncScheduler().submit(Task.builder().delay(Ticks.of(5)).plugin(pluginContainer).execute(() -> {
			testRead();
		}).build());
	}

	private void testWrite() {
		try {
			getLocaleUtil(Locales.DEFAULT).getLocaleNode("ItemStack").set(TypeTokens.SERIALIZED_STACK_TOKEN, new SerializedItemStack(ItemStack.of(ItemTypes.STONE)));
		} catch (SerializationException e) {
			logger.error(e.getLocalizedMessage());
		}
		
		// Test write and save default locale - en-US. I deliberately indicated the wrong localization in the code.
		boolean checkHocon = updateIsSave(this.saveHocon, getLocaleUtil(Locales.CA_ES).checkString("&a&lDefault locale. &4&lTest String HOCON config", "Test comment", "TestPath"));
		checkHocon = updateIsSave(this.saveHocon, getLocaleUtil(Locales.CA_ES).checkString("&a&lDefault locale. Checking the string existing only in it.", "Test comment", "TestPath1"));
		checkHocon = updateIsSave(this.saveHocon,  getLocaleUtil(Locales.CA_ES).checkComponent(true, serialize("&a&lDefault locale. &4&lTest JSON string HOCON config"), "Test comment", "TestComponentPath"));
		checkHocon = updateIsSave(this.saveHocon, getLocaleUtil(Locales.CA_ES).checkListStrings(Arrays.asList("&a&lDefault locale. &4&lTest Strings HOCON config", "String 2"), "Test comment", "TestListPath"));
		checkHocon = updateIsSave(this.saveHocon,  getLocaleUtil(Locales.CA_ES).checkListComponents(true, Arrays.asList(serialize("&a&lDefault locale. &4&lTest JSON strings HOCON config"), serialize("Component String 2")), "Test comment", "TestListComponentsPath"));
		if(checkHocon)  getLocaleUtil(Locales.CA_ES).saveLocaleNode();
		
		// Test write and save locale - en-CA.
		boolean checkYaml = updateIsSave(saveYaml, getLocaleUtil(Locales.EN_CA).checkString("&a&len-CA locale. &4&lTest String YAML config", "Test comment", "TestPath"));
		checkYaml = updateIsSave(this.saveYaml, getLocaleUtil(Locales.EN_CA).checkComponent(true, serialize("&a&len-CA locale. &4&lTest JSON string YAML"), "Test comment", "TestComponentPath"));
		checkYaml = updateIsSave(this.saveYaml, getLocaleUtil(Locales.EN_CA).checkListStrings(Arrays.asList("&a&len-CA locale. &4&lTest Strings YAML config", "String 2"), "Test comment", "TestListPath"));
		checkYaml = updateIsSave(this.saveYaml,  getLocaleUtil(Locales.EN_CA).checkListComponents(true, Arrays.asList(serialize("&a&len-CA locale. &4&lTest JSON strings YAML config"), serialize("Component String 2")), "Test comment", "TestListComponentsPath"));
		if(checkYaml) getLocaleUtil(Locales.EN_CA).saveLocaleNode();
		
		// Test write and save locale - en-GB.
		boolean checkJson = updateIsSave(saveJson, getLocaleUtil(Locales.EN_GB).checkString("&a&len-GB locale. &4&lTest String JSON config", null, "TestPath"));
		checkJson = updateIsSave(this.saveJson, getLocaleUtil(Locales.EN_GB).checkComponent(true, serialize("&a&len-GB locale. &4&lTest JSON string JSON"), null, "TestComponentPath"));
		checkJson = updateIsSave(this.saveJson, getLocaleUtil(Locales.EN_GB).checkListStrings(Arrays.asList("&a&len-GB locale. &4&lTest Strings JSON config", "String 2"), null, "TestListPath"));
		checkJson = updateIsSave(this.saveJson,  getLocaleUtil(Locales.EN_GB).checkListComponents(true, Arrays.asList(serialize("&a&len-GB locale. &4&lTest JSON strings JSON config"), serialize("Component String 2")), null, "TestListComponentsPath"));
		if(checkJson) getLocaleUtil(Locales.EN_GB).saveLocaleNode();
		
		// Test write and save locale - ru-RU.
		boolean checkLegacy = updateIsSave(saveProperties, getLocaleUtil(Locales.RU_RU).checkString("&a&lЛокализация ru-RU. &4&lТест строки конфига PROPERTIES", null, "TestPath", "TestPath2"));
		checkLegacy = updateIsSave(this.saveProperties, getLocaleUtil(Locales.RU_RU).checkComponent(true, serialize("&a&lЛокализация ru-RU. &4&lТест JSON строки конфига PROPERTIES"), null, "TestComponentPath"));
		checkLegacy = updateIsSave(this.saveProperties, getLocaleUtil(Locales.RU_RU).checkListStrings(Arrays.asList("&a&lЛокализация ru-RU. &4&lТест строк конфига PROPERTIES", "Строка 2"), null, "TestListPath"));
		checkLegacy = updateIsSave(this.saveProperties,  getLocaleUtil(Locales.RU_RU).checkListComponents(true, Arrays.asList(serialize("&a&lЛокализация ru-RU. &4&lТест JSON строк конфига PROPERTIES"), serialize("Строка компонент 2")), null, "TestListComponentsPath"));
		if(checkLegacy) getLocaleUtil(Locales.RU_RU).saveLocaleNode();
		
	}

	private void testRead() {
		
		logger.info("Total locales created/saved -> " + api.getPluginLocales(instance).size());
		
		try {
			logger.warn("Start test getting ItemStack from config!");
			logger.info(getLocaleUtil(Locales.DEFAULT).getLocaleNode("ItemStack").get(TypeTokens.SERIALIZED_STACK_TOKEN).getItemStack().type().asComponent());
		} catch (SerializationException e) {
			logger.error(e.getLocalizedMessage());
		}
		//Test writed strings
		logger.warn("Start test strings! TestPath");
		logger.info(getLocaleUtil(Locales.CA_ES).getComponent(false, "TestPath"));
		logger.info(getLocaleUtil(Locales.EN_CA).getComponent(false, "TestPath"));
		logger.info(getLocaleUtil(Locales.EN_GB).getComponent(false, "TestPath"));
		logger.info(getLocaleUtil(Locales.RU_RU).getComponent(false, "TestPath", "TestPath2"));
		logger.info(getLocaleUtil(Locales.RU_RU).getComponent(false, "TestPath1"));
		logger.info(getLocaleUtil(Locales.RU_RU).getComponent(false, "TestNulledPath"));
		
		//test writed components
		logger.warn("Start test components! TestComponentPath");
		logger.info(getLocaleUtil(Locales.CA_ES).getComponent(true, "TestComponentPath"));
		logger.info(getLocaleUtil(Locales.EN_CA).getComponent(true, "TestComponentPath"));
		logger.info(getLocaleUtil(Locales.EN_GB).getComponent(true, "TestComponentPath"));
		logger.info(getLocaleUtil(Locales.RU_RU).getComponent(true, "TestComponentPath"));
		
		//Test writed list strings
		logger.warn("Start test list strings! TestListPath");
		logger.info(getLocaleUtil(Locales.CA_ES).getListComponents(false, "TestListPath").get(0));
		logger.info(getLocaleUtil(Locales.CA_ES).getListComponents(false, "TestListPath").get(1));
		logger.info(getLocaleUtil(Locales.EN_CA).getListComponents(false, "TestListPath").get(0));
		logger.info(getLocaleUtil(Locales.EN_CA).getListComponents(false, "TestListPath").get(1));
		logger.info(getLocaleUtil(Locales.EN_GB).getListComponents(false, "TestListPath").get(0));
		logger.info(getLocaleUtil(Locales.EN_GB).getListComponents(false, "TestListPath").get(1));
		logger.info(getLocaleUtil(Locales.RU_RU).getListComponents(false, "TestListPath").get(0));
		logger.info(getLocaleUtil(Locales.RU_RU).getListComponents(false,"TestListPath").get(1));
		
		//test writed list components
		logger.warn("Start test list components! TestListComponentsPath");
		logger.info(getLocaleUtil(Locales.CA_ES).getListComponents(true, "TestListComponentsPath").get(0));
		logger.info(getLocaleUtil(Locales.CA_ES).getListComponents(true, "TestListComponentsPath").get(1));
		logger.info(getLocaleUtil(Locales.EN_CA).getListComponents(true, "TestListComponentsPath").get(0));
		logger.info(getLocaleUtil(Locales.EN_CA).getListComponents(true, "TestListComponentsPath").get(1));
		logger.info(getLocaleUtil(Locales.EN_GB).getListComponents(true, "TestListComponentsPath").get(0));
		logger.info(getLocaleUtil(Locales.EN_GB).getListComponents(true, "TestListComponentsPath").get(1));
		logger.info(getLocaleUtil(Locales.RU_RU).getListComponents(true, "TestListComponentsPath").get(0));
		logger.info(getLocaleUtil(Locales.RU_RU).getListComponents(true, "TestListComponentsPath").get(1));
	}

	private Component serialize(String string) {
		return LegacyComponentSerializer.legacyAmpersand().deserialize(string);
	}

	private AbstractLocaleUtil getLocaleUtil(Locale locale) {
		return api.getOrDefaultLocale(instance, locale);
	}

	private boolean updateIsSave(boolean currentResult, boolean check) {
		if(check) currentResult = check;
		return currentResult;
	}

}
