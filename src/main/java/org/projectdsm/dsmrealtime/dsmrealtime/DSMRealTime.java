package org.projectdsm.dsmrealtime.dsmrealtime;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.projectdsm.dsmrealtime.dsmrealtime.commands.TimeCommand;

import java.util.Calendar;
import java.util.TimeZone;

public final class DSMRealTime extends JavaPlugin {

    private static final World world = Bukkit.getWorld("world");
    private static Calendar cal;

    private static long interval; // Schedule to update every 100 ticks
    private static long time;
    private static boolean isTimeEnabled = true;
    // private static boolean isWeatherEnabled;
    private static String timezone;

    @Override
    public void onEnable() {
        // Plugin startup logic

        /* Setup config.yml */
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        interval = getConfig().getInt("UpdateInterval");
        timezone = getConfig().getString("Timezone");

        if (interval <= 0) {
            System.out.println("[DSM Real Time]: ERROR - Stopping...");
            throw new IllegalArgumentException("The update interval must be greater than 0");
        } else {
            System.out.println("[DSM Real Time]: Starting...");

            /* Set Command Executors */
            getCommand("synctime").setExecutor(new TimeCommand());
            // getCommand("syncweather").setExecutor(new WeatherCommand());

            /* Schedule an update time and weather task */
            BukkitScheduler scheduler = getServer().getScheduler();
            scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
                @Override
                public void run() {
                    if (isTimeEnabled) {
                        setTime();
                    }

                    //if (isWeatherEnabled) {
                        // setWeather();
                    //}
                }
            }, 0L, interval);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("[DSM Real Time]: Stopping...");
    }

    /** Set isTimeEnabled to true or false
     *
     * @param enabled - true or false
     */
    public static void setTimeEnabled(boolean enabled) {
        isTimeEnabled = enabled;
    }

    /** Set the time of the world
     *
     */
    private static void setTime() {
        cal = Calendar.getInstance(TimeZone.getTimeZone(timezone));
        // Ticks = (Hours * 1000) - 6000
        time = (1000 * cal.get(Calendar.HOUR_OF_DAY)) + (16 * cal.get(Calendar.MINUTE)) - 6000;
        world.setFullTime(time);
    }

    /** Set the weather of the world
     *
     */
    private static void setWeather(boolean isStorm) {
        world.setStorm(isStorm);
    }
}
