package com.himself12794.notenoughtime.world;

import com.himself12794.notenoughtime.NotEnoughTime;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class WorldTimeData extends WorldSavedData {

	public static final String PROPERTIES_ID = NotEnoughTime.MODID + "_TickHandler";

	public int dayLengthMultiplier = 1;
	public int nightLengthMultiplier = 1;
	public int ticksSinceLastAdvance = 0;
	public boolean isEnabled = true;
	
	public WorldTimeData(String id) {
		super(id);
	}
	
	public WorldTimeData() {
		this(PROPERTIES_ID);
	}

	public static WorldTimeData getForWorld(World world) {

		WorldTimeData data = (WorldTimeData)world.perWorldStorage.loadData(WorldTimeData.class, WorldTimeData.PROPERTIES_ID);
		if (data == null) {
			data = new WorldTimeData();
		}
		world.perWorldStorage.setData(WorldTimeData.PROPERTIES_ID, data);
		
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