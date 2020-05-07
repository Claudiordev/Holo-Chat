package com.claudiordev.config;

import java.util.List;

import static com.claudiordev.main.Main.getPlugin;

public class Configuration {

    static String tag;
    static String hologram_prefix;
    static boolean hologram_time_fixed;
    static int hologram_time;
    static double hologram_height;
    static int hologram_text_lines;
    static boolean special_chat;
    static boolean radius;
    static int radius_distance;
    static List<String> help_message;
    static int chat_type;
    static boolean spectator_enabled;

    public Configuration() {
        getPlugin().getConfig().options().copyDefaults();
    }

    /**
     * Load all the Configuration File parameters into their respective properties of the Class
     */
    public static void load() {
        tag = getPlugin().getConfig().getString("Tag");
        hologram_prefix = getPlugin().getConfig().getString("Prefix");
        hologram_time_fixed = getPlugin().getConfig().getBoolean("Hologram_time_fixed");
        hologram_time = getPlugin().getConfig().getInt("Hologram_time");
        hologram_height = getPlugin().getConfig().getInt("Hologram_height");
        help_message = getPlugin().getConfig().getStringList("Help_message");
        hologram_text_lines = getPlugin().getConfig().getInt("Max_lines");
        special_chat = getPlugin().getConfig().getBoolean("Special_chat");
        radius = getPlugin().getConfig().getBoolean("Radius");
        radius_distance = getPlugin().getConfig().getInt("Radius_distance");
        chat_type = getPlugin().getConfig().getInt("Chat-type");
        spectator_enabled = getPlugin().getConfig().getBoolean("Spectator-enabled");

    }

    public static String getTag() {
        return tag;
    }

    public static String getHologram_prefix() {
        return hologram_prefix;
    }

    public static boolean isHologram_time_fixed() {
        return hologram_time_fixed;
    }

    public static int getHologram_time() {
        return hologram_time;
    }

    public static double getHologram_height() {
        return hologram_height;
    }

    public static List<String> getHelp_message() {
        return help_message;
    }

    public static int getHologram_text_lines() {
        return hologram_text_lines;
    }

    public static boolean getSpecial_chat() {
        return special_chat;
    }

    public static int getRadius_distance() {
        return radius_distance;
    }

    public static boolean isRadius() {
        return radius;
    }

    public static int getChat_type() {
        return chat_type;
    }

    public static boolean isSpectator_enabled() {
        return spectator_enabled;
    }
}
