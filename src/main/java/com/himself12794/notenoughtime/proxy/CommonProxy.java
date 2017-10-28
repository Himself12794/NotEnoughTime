package com.himself12794.notenoughtime.proxy;

import com.himself12794.notenoughtime.NotEnoughTime;
import com.himself12794.notenoughtime.command.TimeCommands;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class CommonProxy {

	public void init(FMLInitializationEvent event) {
    	NotEnoughTime.logger().info("Registering event handlers");

    	FMLCommonHandler.instance().bus().register(NotEnoughTime.config());
        FMLCommonHandler.instance().bus().register(NotEnoughTime.instance().timeFlowHandler);
	}
	
	public void postInit(FMLPostInitializationEvent event) {
		NotEnoughTime.config().syncConfig();
	}
	
	public void serverStarting(FMLServerStartingEvent event) {
		NotEnoughTime.logger().info("Registering server commands");
		event.registerServerCommand(new TimeCommands());
	}
	
}
