package sawfowl.localetest;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import com.google.gson.JsonObject;

import sawfowl.localeapi.api.serializetools.itemstack.CompoundTag;

@ConfigSerializable
public class CustomNBT implements CompoundTag {

	@Setting("TestKey")
	private String test = "Test Value 1";
	@Setting("CustomNBT2")
	private CustomNBT2 customNBT = new CustomNBT2();

	String getString() {
		return test;
	}

	public CustomNBT2 getCustomNBT2() {
		return customNBT;
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject object = new JsonObject();
		object.add("CustomNBT2", customNBT.toJsonObject());
		object.addProperty("TestKey", test);
		return object;
	}
	
}