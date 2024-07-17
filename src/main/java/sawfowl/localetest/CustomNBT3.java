package sawfowl.localetest;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import com.google.gson.JsonObject;

import sawfowl.localeapi.api.serializetools.itemstack.PluginComponent;

@ConfigSerializable
public class CustomNBT3 implements PluginComponent {

	@Setting("TestKey")
	private String test = "Test Value 3";

	String getString() {
		return test;
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject object = new JsonObject();
		object.addProperty("TestKey", test);
		return object;
	}

	@Override
	public String toString() {
		return "CustomNBT [test=" + test + "]";
	}
	
}