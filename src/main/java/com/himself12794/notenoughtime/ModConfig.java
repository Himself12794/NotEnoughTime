package com.himself12794.notenoughtime;

import java.io.File;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

public class ModConfig {

	final Configuration mainConfig;
	final ConfigCategory generalModConfig;

	private boolean updateTimeEveryTick = false;
	
	public ModConfig(File location) {
		mainConfig = new Configuration(location, true);
	
		generalModConfig = mainConfig.getCategory(NotEnoughTime.NAME + " Config");
		generalModConfig.setLanguageKey("notenoughtime.config.general");
		generalModConfig.setComment("General Configuration");
		
		syncConfig();
	}
	
	@SubscribeEvent
	public void configChanged(ConfigChangedEvent.OnConfigChangedEvent event) {

		if (event.modID.equals(NotEnoughTime.MODID)) {
			syncConfig();
		}

	}
	
	public void syncConfig() {
		updateTimeEveryTick = mainConfig.getBoolean("UpdateTimeEveryTick", generalModConfig.getName(), true, "Turning this off might improve FPS, at the cost of jumping sun/moon location");

		if (mainConfig.hasChanged()) mainConfig.save();
	}
	
	@Override
	public String toString() {
		return generalModConfig.getName();
	}
	
	public boolean updateTimePerTick() {
		return updateTimeEveryTick;
	}
	
}
