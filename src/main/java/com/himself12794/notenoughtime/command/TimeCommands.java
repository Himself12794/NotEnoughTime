package com.himself12794.notenoughtime.command;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.himself12794.notenoughtime.NotEnoughTime;
import com.himself12794.notenoughtime.world.TimeFlowData;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

/**
 * For configuring the flow of time.
 * Usage:
 * <pre><code>/tmod &lt;night|day|toggle|get&gt; &lt;multiplier&gt;</code></pre>
 * 
 * @author Himself12794
 *
 */
public class TimeCommands implements ICommand {

	public final List<String> aliases = Lists.newArrayList();
	
	public final Set<String> subCommands = Sets.newHashSet("day", "night", "toggle", "get", "reset");
	
	public TimeCommands() {
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
				
				switch (args[0]) {
				case "toggle": 
					commandToggle(sender, world);
					break;
				case "get":
					commandGet(sender, world);
					break;
				case "reset":
					commandReset(sender, world);
					break;
				case "day":
				case "night": {
					if (args.length > 1 && isValidFloat(args[1])) {
						commandDayNight(sender, world, args);
						break;
					}
				}
				default:
					sender.addChatMessage(new ChatComponentText("Invalid usage, usage is: " + getCommandUsage(sender)));
				}
				
			} else {
				sender.addChatMessage(new ChatComponentText("Invalid usage, usage is: " + getCommandUsage(sender)));
			}
			
		}
		
	}

	public void commandDayNight(ICommandSender sender, World world, String[] args) {

		TimeFlowData flowData = TimeFlowData.getForWorld(world);

		float modifier = Float.parseFloat(args[1]);
		if ("day".equals(args[0])) {
			flowData.dayLengthMultiplier = modifier;
		} else if ("night".equals(args[0])){
			flowData.nightLengthMultiplier = modifier;
		}
		flowData.markDirty();
		sendMessageToAll(sender, String.format("%s now has modifier %s for dimension id %d", args[0], modifier, world.provider.dimensionId));
	}
	
	public void commandToggle(ICommandSender sender, World world) {
		TimeFlowData ticker = TimeFlowData.getForWorld(world);
		ticker.isEnabled = !ticker.isEnabled;
		world.getGameRules().setOrCreateGameRule("doDaylightCycle", String.valueOf(!ticker.isEnabled));
		ticker.markDirty();
		
		sendMessageToAll(sender, "Time modding is now " + (ticker.isEnabled ? "on" : "off"));
	}
	
	public void commandGet(ICommandSender sender, World world) {
		TimeFlowData flowData = TimeFlowData.getForWorld(world);
		float modifierDay = flowData.dayLengthMultiplier;
		float modifierNight = flowData.nightLengthMultiplier;
		flowData.markDirty();
		
		String msg = String.format("Time mod is %s, dayLengthMod is %s, nightLengthMod is %s", (flowData.isEnabled ? "ON" : "OFF"), modifierDay, modifierNight) ;
		sender.addChatMessage(new ChatComponentText(msg));
	}
	
	public void commandReset(ICommandSender sender, World world) {
		TimeFlowData ticker = TimeFlowData.getForWorld(world);
		ticker.dayLengthMultiplier = 1.0F;
		ticker.nightLengthMultiplier = 1.0F;
		ticker.isEnabled = false;
		world.getGameRules().setOrCreateGameRule("doDaylightCycle", "true");
		ticker.markDirty();
		
		sendMessageToAll(sender, "Time flow has been reset to normal");
	}
	
	private boolean isValidFloat(String val) {
		try {
			Float.parseFloat(val);
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
		
		if (NotEnoughTime.getMetadata().authorList.contains(sender.getCommandSenderName())) {
			return true;
		}
		
		if (sender instanceof EntityPlayerMP) {
			int opLevel = ((EntityPlayerMP)sender).mcServer.getOpPermissionLevel();
			return sender.canCommandSenderUseCommand(opLevel, "gamerule");
		} 
		
		return true;
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
		if (args.length == 0) {
			return Lists.newArrayList(subCommands);
		}
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}

}
