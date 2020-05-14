package org.projectdsm.dsmrealtime.dsmrealtime.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.projectdsm.dsmrealtime.dsmrealtime.DSMRealTime;

public class ToggleCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("synctime")) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    sender.sendMessage(DSMRealTime.getFormattedMessage() + " Sync Time is currently " + DSMRealTime.getTimeEnabled());
                }

                System.out.println("[DSMRealTime] Sync Time is currently " + DSMRealTime.getTimeEnabled());
                return true;
            } else if (args[0].equalsIgnoreCase("enable")) {
                DSMRealTime.setTimeEnabled(true);

                /* Change output based on sender */
                if (sender instanceof Player) {
                    sender.sendMessage(DSMRealTime.getFormattedMessage() + " Sync Time is now enabled");
                }
                System.out.println("[DSMRealTime] Sync Time is now enabled");
                return true;

            } else if (args[0].equalsIgnoreCase("disable")) {
                DSMRealTime.setTimeEnabled(false);

                if (sender instanceof Player) {
                    sender.sendMessage(DSMRealTime.getFormattedMessage() + " Sync Time is now disabled");
                }

                System.out.println("[DSMRealTime] Sync Time is now disabled");
                return true;
            } else {
                sender.sendMessage(DSMRealTime.getFormattedMessage() + " Improper usage:\nType /synctime enable to enable or /synctime disable to disable");
                return false;
            }
        } else if (command.getName().equalsIgnoreCase("syncweather")){
            if (args.length == 0) {
                if (sender instanceof Player) {
                    sender.sendMessage(DSMRealTime.getFormattedMessage() + " Sync Weather is currently " + DSMRealTime.getWeatherEnabled());
                }

                System.out.println("[DSMRealTime] Sync Weather is currently " + DSMRealTime.getWeatherEnabled());
                return true;
            } else if (args[0].equalsIgnoreCase("enable")) {
                DSMRealTime.setWeatherEnabled(true);

                /* Change output based on sender */
                if (sender instanceof Player) {
                    sender.sendMessage(DSMRealTime.getFormattedMessage() + " Sync Weather is now enabled");
                }
                System.out.println("[DSMRealTime] Sync Weather is now enabled");
                return true;

            } else if (args[0].equalsIgnoreCase("disable")) {
                DSMRealTime.setWeatherEnabled(false);

                if (sender instanceof Player) {
                    sender.sendMessage(DSMRealTime.getFormattedMessage() + " Sync Weather is now disabled");
                }

                System.out.println("[DSMRealTime] Sync Weather is now disabled");
                return true;
            } else {
                sender.sendMessage(DSMRealTime.getFormattedMessage() + " Improper usage:\nType /syncweather enable to enable or /syncweather disable to disable");
                return false;
            }
        }

        return false;
    }
}
