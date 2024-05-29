package sawfowl.localetest;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import com.google.gson.JsonObject;

import sawfowl.localeapi.api.serializetools.itemstack.PluginComponent;

@ConfigSerializable
public class CustomNBT2 implements PluginComponent {

	@Setting("TestKey")
	private String test = "Test Value 2";
	@Setting("CustomNBT3")
	private CustomNBT3 customNBT = new CustomNBT3();

	String getString() {
		return test;
	}

	public CustomNBT3 getCustomNBT3() {
		return customNBT;
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject object = new JsonObject();
		object.add("CustomNBT3", customNBT.toJsonObject());
		object.addProperty("TestKey", test);
		return object;
	}

	@Override
	public String toString() {
		return "CustomNBT [test=" + test + ", customNBT=" + customNBT + "]";
	}
	
}