package org.projectdsm.dsmrealtime.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.projectdsm.dsmrealtime.DSMRealTime;

/**
 * Controls commands to enable and disable plugin features
 */
public class ToggleCommand implements CommandExecutor {

    private final String syncTimeNode = "dsmrealtime.synctime";
    private final String syncTimeCommand = syncTimeNode.substring(12);

    private final String syncWeatherNode = "dsmrealtime.syncweather";
    private final String syncWeatherCommand = syncWeatherNode.substring(12);

    /**
     * Controls /synctime and /syncweather commands. Use /synctime to enable or disable the time sync feature.
     * Use /syncweather to enable or disable the weather sync feature.
     * @param sender - the sender of the command
     * @param command - the command alias
     * @param label - the command label
     * @param args - the arguments given following the command alias in an array
     * @return whether the command was successfully triggered by the user or not
     */ 
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(syncTimeCommand)) {
            if (sender.hasPermission(syncTimeNode)) {
                if (args.length == 0) {
                    sender.sendMessage(DSMRealTime.getFormattedMessage() + "Time Sync is currently " + DSMRealTime.getTimeEnabled());
                    return true;

                } else if (args[0].equalsIgnoreCase("enable")) {
                    DSMRealTime.setTimeEnabled(true);
                    sender.sendMessage(DSMRealTime.getFormattedMessage() + "Time Sync is now enabled");
                    return true;

                } else if (args[0].equalsIgnoreCase("disable")) {
                    DSMRealTime.setTimeEnabled(false);
                    sender.sendMessage(DSMRealTime.getFormattedMessage() + "Time Sync is now disabled");
                    return true;

                } else {
                    sender.sendMessage(ChatColor.RED + "Improper usage of " + command.getName());
                    return false;
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use command " + command.getName());
                return false;
            }
        } else if (command.getName().equalsIgnoreCase(syncWeatherCommand)){
            if (sender.hasPermission(syncWeatherNode)) {
                if (args.length == 0) {
                    sender.sendMessage(DSMRealTime.getFormattedMessage() + "Weather Sync is currently " + DSMRealTime.getWeatherEnabled());
                    return true;

                } else if (args[0].equalsIgnoreCase("enable")) {
                    DSMRealTime.setWeatherEnabled(true);
                    sender.sendMessage(DSMRealTime.getFormattedMessage() + "Weather sync is now enabled");
                    return true;

                } else if (args[0].equalsIgnoreCase("disable")) {
                    DSMRealTime.setWeatherEnabled(false);
                    sender.sendMessage(DSMRealTime.getFormattedMessage() + "Weather Sync is now disabled");
                    return true;

                } else {
                    sender.sendMessage(ChatColor.RED + "Improper usage of " + command.getName());
                    return false;
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use command " + command.getName());
                return false;
            }
        }

        return false;
    }
}
