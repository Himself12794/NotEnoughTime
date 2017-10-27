package com.himself12794.notenoughtime.world;

import com.himself12794.notenoughtime.NotEnoughTime;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

/**
 * Holds the time flow information for the world, such as partial tick advance for
 * partial time flow, and multipliers for time flow. This information is world 
 * specific, so time might flow differently in different dimensions.
 * 
 * @author Himself12794
 *
 */
public class TimeFlowData extends WorldSavedData {

	public static final String PROPERTIES_ID = NotEnoughTime.MODID + "_TimeHandler";
	public static final String PARTIAL_TICK_COUNTER = "partialTickCounter";
	public static final String DAY_MULTIPLIER = "dayMultiplier";
	public static final String NIGHT_MULTIPLIER = "nightMultiplier";
	public static final String IS_ENABLED = "isEnabled";
	
	// Defaults
	public float dayLengthMultiplier = 1.0F;
	public float nightLengthMultiplier = 1.0F;
	public float partialTickCounter = 0F;
	public boolean isEnabled = true;
	
	public TimeFlowData(String id) {
		super(id);
	}
	
	public TimeFlowData() {
		this(PROPERTIES_ID);
	}

	public static TimeFlowData getForWorld(World world) {

		TimeFlowData data = (TimeFlowData)world.mapStorage.loadData(TimeFlowData.class, TimeFlowData.PROPERTIES_ID);
		if (data == null) {
			NotEnoughTime.getLogger().info("Registering WorldSavedData for dimension {}", world.provider.dimensionId);
			data = new TimeFlowData();
			world.mapStorage.setData(TimeFlowData.PROPERTIES_ID, data);
		}
		
		return data;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound worldData) {
		NotEnoughTime.getLogger().info("Loading WorldSavedData");
		if (worldData.hasKey(PARTIAL_TICK_COUNTER)) {
			partialTickCounter = worldData.getFloat(PARTIAL_TICK_COUNTER);
		} 
		if (worldData.hasKey(DAY_MULTIPLIER)) {
			dayLengthMultiplier = worldData.getFloat(DAY_MULTIPLIER);
		} 
		if (worldData.hasKey(NIGHT_MULTIPLIER)) {
			nightLengthMultiplier = worldData.getFloat(NIGHT_MULTIPLIER);
		} 
		if (worldData.hasKey(IS_ENABLED)) {
			isEnabled = worldData.getBoolean(IS_ENABLED);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound worldData) {
		worldData.setFloat(PARTIAL_TICK_COUNTER, partialTickCounter);
		worldData.setFloat(DAY_MULTIPLIER, dayLengthMultiplier);
		worldData.setFloat(NIGHT_MULTIPLIER, nightLengthMultiplier);
		worldData.setBoolean(IS_ENABLED, isEnabled);
	}
	
	public String toString() {
		
		StringBuilder str = new StringBuilder();
		str.append("dayModifier: ").append(dayLengthMultiplier).append("\n");
		str.append("nightModifier: ").append(nightLengthMultiplier).append("\n");
		str.append("partialTickCounter: ").append(partialTickCounter).append("\n");
		str.append("isEnabled: ").append(isEnabled).append("\n");
		
		return str.toString();
	}
	
}