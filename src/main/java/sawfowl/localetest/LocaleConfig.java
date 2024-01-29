package sawfowl.localetest;

import java.util.Arrays;
import java.util.List;

import org.spongepowered.api.data.Keys;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import sawfowl.localeapi.api.LocaleReference;
import sawfowl.localeapi.api.TextUtils;
import sawfowl.localeapi.api.serializetools.itemstack.SerializedItemStackPlainNBT;

@ConfigSerializable
public class LocaleConfig implements LocaleReference {

	public LocaleConfig(){}

	@Setting("TestPath")
	@Comment("Test comment")
	private String testPath = "&a&lDefault locale. &4&lTest String HOCON config";
	@Setting("TestPath1")
	private String testPath1 = "&a&lDefault locale. Checking the string existing only in it.";
	@Setting("TestComponentPath")
	private Component testComponentPath = serialize("&a&lDefault locale. &4&lTest JSON string HOCON config");
	@Setting("TestListPath")
	private List<String> testListPath = Arrays.asList("&a&lDefault locale. &4&lTest Strings HOCON config", "String 2");
	@Setting("TestListComponentsPath")
	private List<Component> testListComponentsPath = Arrays.asList(serialize("&a&lDefault locale. &4&lTest JSON strings HOCON config"), serialize("Component String 2"));
	@Setting("ItemStack")
	private ItemStack itemStack = createStack();

	public String getTestPath() {
		return testPath;
	}
	public String getTestPath1() {
		return testPath1;
	}
	public Component getTestComponentPath() {
		return testComponentPath;
	}
	public List<String> getTestListPath() {
		return testListPath;
	}
	public List<Component> getTestListComponentsPath() {
		return testListComponentsPath;
	}
	public ItemStack getItemStack() {
		return itemStack;
	}

	private ItemStack createStack() {
		ItemStack itemStack = ItemStack.of(ItemTypes.DIAMOND_SWORD.get());
		itemStack.offer(Keys.CUSTOM_NAME, TextUtils.deserializeLegacy("&bCustom sword name"));
		itemStack.offer(Keys.ITEM_DURABILITY, 500);
		itemStack.offer(Keys.LORE, Arrays.asList(serialize("&2Item lore. Line 1."), serialize("&6Item lore. Line 2.")));
		itemStack.offer(Keys.APPLIED_ENCHANTMENTS, Arrays.asList(Enchantment.of(EnchantmentTypes.LOOTING, 1)));
		SerializedItemStackPlainNBT item = new SerializedItemStackPlainNBT(itemStack);
		item.getOrCreateTag().putObject(LocaleTest.getPluginContainer(), "TestInt", 123213213);
		CustomNBT nbt = new CustomNBT();
		item.getOrCreateTag().putCompoundTag(LocaleTest.getPluginContainer(), "TestTag", nbt);
		return item.getItemStack();
	}

	private Component serialize(String string) {
		return LegacyComponentSerializer.legacyAmpersand().deserialize(string);
	}

}
