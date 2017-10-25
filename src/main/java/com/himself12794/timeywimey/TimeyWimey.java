package com.himself12794.timeywimey;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

@Mod(modid = TimeyWimey.MODID, version = TimeyWimey.VERSION)
public class TimeyWimey
{
    public static final String MODID = "timeywimey";
    public static final String VERSION = "1.0";
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(new Ticking());
    } 



	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new TimeCommands());
	}

	public static class Ticking {
		
		@SubscribeEvent
		public void onWorldTick(TickEvent.WorldTickEvent event) {
			World world = event.world;
			
			boolean isDay = world.getWorldTime() < 12000;
			
			MapStorage storage = world.perWorldStorage;
			
			WorldTicker data = WorldTicker.getForWorld(world);
			
			if (data.isEnabled) {
			
				world.getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
				
				data.ticksSinceLastAdvance++;
				if (data.ticksSinceLastAdvance >= data.dayLengthMultiplier && isDay) {
					data.ticksSinceLastAdvance = 0;
					world.setWorldTime(world.getWorldTime() + 1);
				} else if (data.ticksSinceLastAdvance >= data.nightLengthMultiplier && !isDay) {
					data.ticksSinceLastAdvance = 0;
					world.setWorldTime(world.getWorldTime() + 1);
				}
				if (world.getWorldTime() > 24000) {
					world.setWorldTime(0);
				}
				
				if (world.provider.dimensionId == 0 && !world.isRemote) {
					System.out.println("Last advance: " + data.ticksSinceLastAdvance);
					System.out.println("Time: " + world.getWorldTime());
				}
				
			} else {
				world.getGameRules().setOrCreateGameRule("doDaylightCycle", "true");
			}
			data.markDirty();
		}
	}
	
	public static class WorldTicker extends WorldSavedData {

		public static final String PROPERTIES_ID = MODID + "_TickHandler";

		int dayLengthMultiplier = 6;
		int nightLengthMultiplier = 6;
		int ticksSinceLastAdvance = 0;
		boolean isEnabled = true;
		
		public WorldTicker(String id) {
			super(id);
		}
		
		public WorldTicker() {
			this(PROPERTIES_ID);
		}

		public static WorldTicker getForWorld(World world) {

			WorldTicker data = (WorldTicker)world.perWorldStorage.loadData(WorldTicker.class, WorldTicker.PROPERTIES_ID);
			if (data == null) {
				data = new WorldTicker();
			}
			world.perWorldStorage.setData(WorldTicker.PROPERTIES_ID, data);
			
			return data;
		}
		
		@Override
		public void readFromNBT(NBTTagCompound worldData) {
			if (worldData.hasKey(PROPERTIES_ID + ":lastAdvance")) {
				ticksSinceLastAdvance = worldData.getInteger(PROPERTIES_ID + ":lastAdvance");
			} 
			if (worldData.hasKey(PROPERTIES_ID + ":dayLengthMultiplier")) {
				dayLengthMultiplier = worldData.getInteger(PROPERTIES_ID + ":dayLengthMultiplier");
			} 
			if (worldData.hasKey(PROPERTIES_ID + ":nightLengthMultiplier")) {
				nightLengthMultiplier = worldData.getInteger(PROPERTIES_ID + ":nightLengthMultiplier");
			} 
			if (worldData.hasKey(PROPERTIES_ID + ":isEnabled")) {
				isEnabled = worldData.getInteger(PROPERTIES_ID + ":isEnabled") != 0;
			}
		}

		@Override
		public void writeToNBT(NBTTagCompound worldData) {
			worldData.setInteger(PROPERTIES_ID + ":lastAdvance", ticksSinceLastAdvance);
			worldData.setInteger(PROPERTIES_ID + ":dayLengthMultiplier", dayLengthMultiplier);
			worldData.setInteger(PROPERTIES_ID + ":nightLengthMultiplier", nightLengthMultiplier);
			worldData.setInteger(PROPERTIES_ID + ":isEnabled", isEnabled ? 1 : 0);
		}
		
	}
}