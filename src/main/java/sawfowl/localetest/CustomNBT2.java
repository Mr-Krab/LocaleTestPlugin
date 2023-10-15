package sawfowl.localetest;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.localeapi.serializetools.CompoundTag;

@ConfigSerializable
public class CustomNBT2 extends CompoundTag {

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
	
}