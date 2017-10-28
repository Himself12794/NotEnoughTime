package com.himself12794.notenoughtime;

import java.util.Set;

import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;

/**
 * Manufactures the config GUI to be used. 
 * 
 * @author Himself12794
 *
 */
public class GuiFactory implements IModGuiFactory {
	
	private Minecraft mc;

	@Override
	public void initialize(Minecraft minecraftInstance) { 
		mc = minecraftInstance;
	}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return ModConfigGUI.class;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
		return null;
	}
	
	public static class ModConfigGUI extends GuiConfig {
		  public ModConfigGUI(GuiScreen parent) {
		    super(parent, new ConfigElement(NotEnoughTime.config().generalModConfig).getChildElements(),
		        NotEnoughTime.MODID, false, false, GuiConfig.getAbridgedConfigPath(NotEnoughTime.config().toString()));
		  }
		}

}