package com.himself12794.notenoughtime.handlers;

import com.himself12794.notenoughtime.world.WorldTimeData;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import net.minecraft.world.World;

public class WorldTimeFlowHandler {
	
	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event) {

		World world = event.world;
		/*
		if (world.provider.dimensionId == 0 && !world.isRemote && event.phase == Phase.START) {
			System.out.println("World info: " + world.getWorldInfo().getWorldTime());
			System.out.println("World time: " + world.getWorldTime());
			System.out.println("Total World info time: " + world.getWorldInfo().getWorldTotalTime());
			System.out.println("Total World time: " + world.getTotalWorldTime());
		}*/
		if (event.phase == Phase.START) {
		
			long worldTime = world.getWorldInfo().getWorldTime();
			long adjustedTime = worldTime % 24000;
			boolean isDay = adjustedTime < 12000;
			
			WorldTimeData data = WorldTimeData.getForWorld(world);
			
			if (data.isEnabled) {
			
				world.getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
				
				data.ticksSinceLastAdvance++;
				if (data.ticksSinceLastAdvance >= data.dayLengthMultiplier && isDay) {
					data.ticksSinceLastAdvance = 0;
					world.getWorldInfo().setWorldTime(worldTime + 1);
				} else if (data.ticksSinceLastAdvance >= data.nightLengthMultiplier && !isDay) {
					data.ticksSinceLastAdvance = 0;
					world.getWorldInfo().setWorldTime(worldTime + 1);
				}
				
				data.markDirty();
			} 
		}
	}
}