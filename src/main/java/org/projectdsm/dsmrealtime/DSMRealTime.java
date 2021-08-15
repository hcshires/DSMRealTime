package org.projectdsm.dsmrealtime;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.projectdsm.dsmrealtime.commands.ToggleCommand;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.logging.Logger;

/**
 * Configures and runs DSMRealTime time and weather sync via a Bukkit scheduled task
 */
public final class DSMRealTime extends JavaPlugin {

    private static final long interval = 1200; // Update every minute
    private static final String formattedMessage = ChatColor.GRAY + "[" + ChatColor.RED + "DSMRealTime" + ChatColor.GRAY + "]" + ChatColor.WHITE + " ";
    private static Logger logger;
    private static World world;
    private static String timezone, apiKey, location;
    private static boolean isTimeEnabled, isWeatherEnabled, debug;

    /**
     * Check if the config is set up properly and is returning values for time sync
     *
     * @return true if all values have been properly set in config.yml, false otherwise
     */
    public static boolean checkTimeValues() {
        boolean success = true;
        if (timezone.equals("")) {
            setTimeEnabled(false);
            createErrorMessage();
            success = false;
        }

        return success;
    }

    /**
     * Check if the config is set up properly and is returning values for weather sync
     *
     * @return true if all values have been properly set in config.yml, false otherwise
     */
    public static boolean checkWeatherValues() {
        boolean success = true;
        if (apiKey.equals("") || apiKey.equalsIgnoreCase("API_KEY") || location.equals("")) {
            setWeatherEnabled(false);
            createErrorMessage();
            success = false;
        }
        return success;
    }

    /**
     * Create and log an error message about the config file
     */
    private static void createErrorMessage() {
        String message = formattedMessage + "ERROR: Missing one or more config values, disabling. " +
                "To fix, verify the config.yml has been setup and reload";
        Bukkit.broadcastMessage(message);
        logger.severe(message); // Override debug value
    }

    /**
     * Get the standard formatted message prefix for all plugin chat output
     *
     * @return the formatted message prefix
     */
    public static String getFormattedMessage() {
        return formattedMessage;
    }

    /**
     * Get isTimeEnabled
     *
     * @return isTimeEnabled true or false
     */
    public static String getTimeEnabled() {
        if (isTimeEnabled) {
            return "enabled";
        } else {
            return "disabled";
        }
    }

    /**
     * Set isTimeEnabled to true or false
     *
     * @param enabled - if true, enable the time feature, if false, disable it.
     */
    public static void setTimeEnabled(boolean enabled) {
        isTimeEnabled = enabled;
    }

    /**
     * Get isWeatherEnabled
     *
     * @return isWeatherEnabled true or false
     */
    public static String getWeatherEnabled() {
        if (isWeatherEnabled) {
            return "enabled";
        } else {
            return "disabled";
        }
    }

    /**
     * Set isWeatherEnabled to true or false
     *
     * @param enabled - if true, enable the time feature, if false, disable it.
     */
    public static void setWeatherEnabled(boolean enabled) {
        isWeatherEnabled = enabled;
    }

    /**
     * Parses given JSON from OpenWeatherMap to return the unique weather condition ID
     *
     * @param str - the specified JSON string
     * @return the weather condition ID
     */
    public static int jsonToId(String str) {
        JsonObject json = new Gson().fromJson(str, JsonObject.class);

        JsonArray weather = json.get("weather").getAsJsonArray();
        JsonObject data = weather.get(0).getAsJsonObject();

        return data.get("id").getAsInt();
    }

    /**
     * Set the time of the world
     */
    private static void setTime(String timezone) {
        // Ticks = (Hours * 1000) - 6000
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(timezone)); // Get the time instance
        long time = (1000 * cal.get(Calendar.HOUR_OF_DAY)) + (16 * cal.get(Calendar.MINUTE)) - 6000;
        if (world != null) {
            world.setTime(time);
            debug("Time Updated!");
        } else {
            logger.severe(ChatColor.RED + "ERROR: Unable to find world. Make sure you've spelled it correctly in the config file.");
        }

    }

    /**
     * Get Current Weather JSON String from api.openweathermap.org
     *
     * @return a Java Map of the current weather condition data
     * Possible weather condition data include "clear", "rain", "snow", etc.
     */
    private static int getWeatherData(String location, String apiKey) {
        final String weather = "http://api.openweathermap.org/data/2.5/weather?q=" + location + "&appid=" + apiKey + "&units=imperial";

        /* Get Data from OpenWeatherMap page */
        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(weather);
            URLConnection connection = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while ((line = rd.readLine()) != null) { // Read in each JSON line
                result.append(line);
            }
            rd.close();
            debug("Received Weather Data...");

            int weatherOutput = jsonToId(result.toString());
            debug("Current Weather ID: " + weatherOutput);

            return weatherOutput; // Convert the String to a map

        } catch (IOException e) {
            logger.severe("ERROR: Weather Data Failed to Update"); // Override debug value
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Set the weather of the world given updated API weather data
     *
     * @param id - the specified weather data Java Map
     */
    private static void setWeather(int id) {
        boolean setStorm = id < 700;

        if (world != null) {
            world.setStorm(setStorm); // Set weather to clear if the data says clear, storm if not clear
            debug("Weather Updated!");
        } else {
            logger.severe(ChatColor.RED + "ERROR: Unable to find world. Make sure you've spelled it correctly in the config file.");
        }

    }

    /**
     * Set the config file values to the current state of SyncTime and SyncWeather based on player interaction with commands
     */
    private void setConfig() {
        File configFile = new File(JavaPlugin.getPlugin(DSMRealTime.class).getDataFolder().getPath(), "config.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        config.set("SyncTime", isTimeEnabled);
        config.set("SyncWeather", isWeatherEnabled);

        try {
            config.save(configFile);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
     * Print to the console only if the user has enabled the config to do so
     * @param message - the message to print
     */
    private static void debug(String message) {
        if (debug) {
            logger.info(message);
        }
    }

    /**
     * Plugin startup logic, including initializing a config file if one doesn't exist and parsing it for fields
     */
    @Override
    public void onEnable() {
        /* Setup config.yml */
        getConfig().options().copyHeader(true);
        getConfig().options().copyDefaults(true);
        saveConfig();

        /* Get Data from Config */
        world = getServer().getWorld(Objects.requireNonNull(getConfig().getString("World")));
        if (world == null) { // World isn't loaded or doesn't exist
            world = getServer().createWorld(new WorldCreator(getConfig().getString("World"))); // The createWorld() method will load a given world if it already exists
            getLogger().info("World \"" + getConfig().getString("World") + "\" wasn't loaded or didn't exist. If nothing is happening in the world you expected, check your spelling in config.yml and delete any new worlds that were generated with the misspelled name.");
        }
        timezone = getConfig().getString("Timezone");
        apiKey = getConfig().getString("APIKey");
        location = getConfig().getString("Location");
        isTimeEnabled = getConfig().getBoolean("SyncTime");
        isWeatherEnabled = getConfig().getBoolean("SyncWeather");
        debug = getConfig().getBoolean("Debug");

        /* Get Logger */
        logger = getLogger();

        logger.info("Starting..."); // Override debug value

        /* Set Command Executors */
        Objects.requireNonNull(getCommand("synctime")).setExecutor(new ToggleCommand());
        Objects.requireNonNull(getCommand("syncweather")).setExecutor(new ToggleCommand());

        /* Schedule an update time and weather task */
        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, () -> {
            setConfig(); // Write current enabled/disabled values to config
            if (isTimeEnabled && checkTimeValues()) {
                debug("Updating Time...");
                setTime(timezone);
            }

            if (isWeatherEnabled && checkWeatherValues()) { // Every two minutes (when set to negative)
                debug("Updating Weather...");
                int currentWeather = getWeatherData(location, apiKey);
                if (currentWeather != 0) {
                    setWeather(currentWeather);
                } else {
                    logger.warning("Weather Data is NULL. Is your API key valid?"); // Override debug value
                }
            }
        }, 0L, interval);
    }

    /**
     * Plugin shutdown logic
     */
    @Override
    public void onDisable() {
        setConfig();
        logger.info("Stopping..."); // Override debug value
    }
}