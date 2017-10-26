package com.himself12794.notenoughtime;

import com.himself12794.notenoughtime.command.TimeCommands;
import com.himself12794.notenoughtime.handlers.WorldTimeFlowHandler;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

@Mod(modid = NotEnoughTime.MODID, version = NotEnoughTime.VERSION)
public class NotEnoughTime {
    public static final String MODID = "notenoughtime";
    public static final String VERSION = "1.0.0-pre1";
    
    @Mod.Instance
    private static NotEnoughTime INSTANCE;
    
    private final WorldTimeFlowHandler timeFlowHandler = new WorldTimeFlowHandler();
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(timeFlowHandler);
    } 

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new TimeCommands());
	}

	
}