package org.projectdsm.dsmrealtime.dsmrealtime.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.projectdsm.dsmrealtime.dsmrealtime.DSMRealTime;

public class TimeCommand implements CommandExecutor {

    private static final String formattedMessage = ChatColor.GRAY + "[" + ChatColor.RED + "DSMRealTime" + ChatColor.GRAY + "]" + ChatColor.WHITE;

    /**
     * Enable sync time
     * @param sender - the sender of the command
     * @param command - the command
     * @param label - the command alias
     * @param args - the command arguments
     * @return if command execution was successful
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args[0].equalsIgnoreCase("enable")) {
            DSMRealTime.setTimeEnabled(true);

            /* Change output based on sender */
            if (sender instanceof Player) {
                sender.sendMessage(formattedMessage + " Sync Time is now enabled");
            }
            System.out.println("[DSMRealTime] Sync Time is now enabled");
            return true;

        } else if (args[0].equalsIgnoreCase("disable")) {
            DSMRealTime.setTimeEnabled(false);

            if (sender instanceof Player) {
                sender.sendMessage(formattedMessage + " Sync Time is now disabled");
            }

            System.out.println("Sync Time is now disabled");
            return true;
        } else {
            sender.sendMessage(formattedMessage + " Improper usage:\nType /synctime enable to enable or /synctime disable to disable");
            return false;
        }
    }
}
