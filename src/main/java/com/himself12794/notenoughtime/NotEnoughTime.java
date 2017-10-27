package com.himself12794.notenoughtime;

import org.apache.logging.log4j.Logger;

import com.himself12794.notenoughtime.command.TimeCommands;
import com.himself12794.notenoughtime.handlers.TimeFlowHandler;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = NotEnoughTime.MODID, version = NotEnoughTime.VERSION)
public class NotEnoughTime {
    public static final String MODID = "notenoughtime";
    public static final String VERSION = "1.0.0-pre1";
    
    @Mod.Instance
    private static NotEnoughTime INSTANCE;
    
	@Mod.Metadata(MODID)
	private static ModMetadata META;
    
    private final TimeFlowHandler timeFlowHandler = new TimeFlowHandler();
    private Logger logger;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	logger = event.getModLog();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	logger.info("Registering event handlers");
        FMLCommonHandler.instance().bus().register(timeFlowHandler);
    } 

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		logger.info("Registering server commands");
		event.registerServerCommand(new TimeCommands());
	}

	public static NotEnoughTime instance() {
		return INSTANCE;
	}
	
	public static Logger getLogger() {
		return INSTANCE.logger;
	}
	
	public static ModMetadata getMetadata() {
		return META;
	}
	
}