package com.himself12794.notenoughtime;

import java.io.File;

import org.apache.logging.log4j.Logger;

import com.himself12794.notenoughtime.handlers.common.TimeFlowHandler;
import com.himself12794.notenoughtime.proxy.CommonProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = NotEnoughTime.MODID, version = NotEnoughTime.VERSION, acceptableRemoteVersions = "*", guiFactory = "com.himself12794.notenoughtime.GuiFactory")
public class NotEnoughTime {
    public static final String MODID = "notenoughtime";
    public static final String NAME = "NotEnoughTime";
    public static final String VERSION = "1.0.0-pre2";
    
    @Mod.Instance
    private static NotEnoughTime INSTANCE;
    
	@Mod.Metadata(MODID)
	private static ModMetadata META;

	@SidedProxy(
		serverSide="com.himself12794.notenoughtime.proxy.CommonProxy",
		clientSide="com.himself12794.notenoughtime.proxy.ClientProxy")
	private static CommonProxy proxy;
	
	public final ModConfig config = new ModConfig(new File("config/" + NAME + ".cfg"));
    public final TimeFlowHandler timeFlowHandler = new TimeFlowHandler();
    private Logger logger;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	logger = event.getModLog();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	proxy.init(event);
    } 
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	proxy.postInit(event);
    } 

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		proxy.serverStarting(event);
	}

	public static NotEnoughTime instance() {
		return INSTANCE;
	}
	
	public static Logger logger() {
		return INSTANCE.logger;
	}
	
	public static CommonProxy proxy() {
		return proxy;
	}
	
	public static ModMetadata getMetadata() {
		return META;
	}
	
	public static ModConfig config() {
		return INSTANCE.config;
	}
	
}