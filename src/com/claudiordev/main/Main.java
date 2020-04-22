package com.claudiordev.main;

import com.claudiordev.config.Configuration;
import com.gmail.filoghost.holographicdisplays.HolographicDisplays;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static Main plugin;
    private static PluginManager pm;

    /** Main Method **/
    private static void main(String args[]) {

    }

    /** Method Launched on Load of Server **/
    public void onEnable() {
        plugin = this;

        //Configuration File
        new Configuration();
        Configuration.load();
        saveDefaultConfig();

        //Get the Plugin Manager
        pm = getServer().getPluginManager();

        getLogger().info("Skills Plugin Activated");

        //Register a new event, of the Class Actions as a New Instance of it, on the plugin variable referent to Skills,
        pm.registerEvents(new Actions(), plugin);

        //Register the command "/hdchat", Set an instance of the Commands Class as executor
        this.getCommand("hdchat").setExecutor(new Commands());

        if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
            getLogger().severe("*** HolographicDisplays is not installed or enabled. ***");
            getLogger().severe("*** This plugin will be disabled. ***");
            this.setEnabled(false);
        }

        if (!Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            getLogger().severe("* ProtocolLib is not installed or enabled. *");
            getLogger().severe("* The \"/hdchat toggle\" function will be disabled*");
        }
    }

    //Return this class in everywhere on the code
    public static Main getPlugin() {
        return plugin;
    }
}
