package com.himself12794.timeywimey;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.himself12794.timeywimey.TimeyWimey.WorldTicker;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class TimeCommands implements ICommand {

	public final List<String> aliases = Lists.newArrayList();
	
	public final Set<String> subCommands = Sets.newHashSet("day", "night", "toggle", "get", "reset");
	
	public TimeCommands() {
		aliases.add("timey");
		aliases.add("tmod");
	}
	
	@Override
	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "timemod";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "timemod <night|day|toggle|get> <multiplier>";
	}

	@Override
	public List<String> getCommandAliases() {
		return aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {

		World world = sender.getEntityWorld();
		if (!world.isRemote) {
			
			if (args.length > 0 && subCommands.contains(args[0])) {
				
				if (!"toggle".equals(args[0]) && args.length > 1 && isValidInteger(args[1])) {
					WorldTicker ticker = WorldTicker.getForWorld(world);

					int modifier = Integer.parseInt(args[1]);
					int timeSpan = modifier;
					timeSpan = timeSpan <= 0 ? 1 : timeSpan;
					if ("day".equals(args[0])) {
						ticker.dayLengthMultiplier = timeSpan;
					} else if ("night".equals(args[0])){
						ticker.nightLengthMultiplier = timeSpan;
					}
					ticker.markDirty();
					sendMessageToAll(sender, String.format("%s now has modifier %d for dimension id %d", args[0], modifier, world.provider.dimensionId));
					
				} else if ("toggle".equals(args[0])) {
					WorldTicker ticker = WorldTicker.getForWorld(world);
					ticker.isEnabled = !ticker.isEnabled;
					ticker.markDirty();
					
					sendMessageToAll(sender, "Time modding is now " + (ticker.isEnabled ? "on" : "off"));
				} else if ("get".equals(args[0])) {
					WorldTicker ticker = WorldTicker.getForWorld(world);
					int modifierDay = ticker.dayLengthMultiplier;
					int modifierNight = ticker.nightLengthMultiplier;
					ticker.markDirty();
					
					String msg = String.format("Time mod is %s, dayLengthMod is %d, nightLengthMod is %d", (ticker.isEnabled ? "ON" : "OFF"), modifierDay, modifierNight) ;
					
					sender.addChatMessage(new ChatComponentText(msg));
				} else if ("reset".equals(args[0])) {
					WorldTicker ticker = WorldTicker.getForWorld(world);
					ticker.dayLengthMultiplier = 1;
					ticker.nightLengthMultiplier = 1;
					ticker.markDirty();
					
					sendMessageToAll(sender, "Time flow has been reset to normal");
				} 
				
			} else {
				sender.addChatMessage(new ChatComponentText("Invalid usage, usage is: " + getCommandUsage(sender)));
			}
			
		}
		
	}

	private boolean isValidInteger(String val) {
		try {
			Integer.parseInt(val);
			return true;
		} catch (Exception e) {
			
		}
		return false;
	}
	
	private void sendMessageToAll(ICommandSender sender, String msg) {
		if (sender instanceof EntityPlayerMP) {
			((EntityPlayerMP)sender).mcServer.getConfigurationManager().sendChatMsg(new ChatComponentText(msg));
		} else {
			MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText(msg));
		}
	}
	
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		
		if (sender instanceof EntityPlayerMP) {
			int opLevel = ((EntityPlayerMP)sender).mcServer.getOpPermissionLevel();
			return sender.canCommandSenderUseCommand(opLevel, "gamerule");
		}
		
		return true;
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		// TODO Auto-generated method stub
		return false;
	}

}
