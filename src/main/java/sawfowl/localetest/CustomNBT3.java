package sawfowl.localetest;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.localeapi.serializetools.CompoundTag;

@ConfigSerializable
public class CustomNBT3 extends CompoundTag {

	@Setting("TestKey")
	private String test = "Test Value 3";

	String getString() {
		return test;
	}
	
}