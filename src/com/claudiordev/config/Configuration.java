package com.claudiordev.config;

import java.util.List;

import static com.claudiordev.main.Main.getPlugin;

public class Configuration {

    static String tag;
    static String hologram_prefix;
    static boolean hologram_time_fixed;
    static int hologram_time;
    static int hologram_height;
    static int hologram_text_lines;

    static List<String> help_message;

    public Configuration() {
        getPlugin().getConfig().options().copyDefaults();
    }

    public static void load() {
        tag = getPlugin().getConfig().getString("Tag");
        hologram_prefix = getPlugin().getConfig().getString("Prefix");
        hologram_time_fixed = getPlugin().getConfig().getBoolean("Hologram_time_fixed");
        hologram_time = getPlugin().getConfig().getInt("Hologram_time");
        hologram_height = getPlugin().getConfig().getInt("Hologram_height");
        help_message = getPlugin().getConfig().getStringList("Help_message");
        hologram_text_lines = getPlugin().getConfig().getInt("Max_lines");
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

    public static int getHologram_height() {
        return hologram_height;
    }

    public static List<String> getHelp_message() {
        return help_message;
    }

    public static int getHologram_text_lines() {
        return hologram_text_lines;
    }
}