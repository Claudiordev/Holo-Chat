package com.claudiordev.main;

import com.claudiordev.config.Configuration;
import com.claudiordev.data.SQLLiteData;
import com.claudiordev.utils.HologramHandler;
import com.claudiordev.utils.RadiusHandler;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.sql.SQLException;
import java.util.HashMap;

public class Actions implements Listener {
    //Player Name (Key), Hologram; Hologram List
    static HashMap<String, Hologram> hologramas = new HashMap<>();
    //Player Name (Key), Visibilit(Boolean)
    static HashMap<Player, Boolean> hologramas_visibility = new HashMap<>();
    //Player Name (Key), HologramHandler
    static HashMap<String, HologramHandler> hologramThreads = new HashMap<>();
    //RadiusHandler (Key), Player Name
    static HashMap<RadiusHandler, String> radiusHandlers = new HashMap<>();
    HologramHandler hologramHandler;
    SQLLiteData sqlLiteData = new SQLLiteData();


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Hologram hologram = HologramsAPI.createHologram(Main.getPlugin(), player.getLocation().add(0, Configuration.getHologram_height(), 0));
        hologramas.put(player.getName(), hologram);

        try {
            String query = "SELECT uniqueid FROM visibility WHERE uniqueid = '"+ player.getUniqueId().toString() +"'";
            String playerid = player.getUniqueId().toString();

            if (((String) sqlLiteData.retrieveData(query,"uniqueid")) == null) {
                hologramas_visibility.put(player, true);
                sqlLiteData.insertData(playerid,1);
                Main.getPlugin().getLogger().info("New player detected, adding to the SQLLite Database");

            } else if (((String) sqlLiteData.retrieveData(query,"uniqueid")).equals(playerid)) {
                //Retrieve later
                String retrieve_query = "SELECT state FROM visibility WHERE uniqueid = '" + playerid +"'";

                if ((sqlLiteData.retrieveData(retrieve_query, "state")).equals("1")) {
                    hologramas_visibility.put(player, true);
                    Main.getPlugin().getLogger().info("Existent player detected, retrieving data from SQLLite Database, State 1");
                } else if ((sqlLiteData.retrieveData(retrieve_query, "state")).equals("0")) {
                    hologramas_visibility.put(player, false);
                    Main.getPlugin().getLogger().info("Existent player detected, retrieving data from SQLLite Database, State 0");
                }
            }

        }catch (Exception ex) {
            ex.printStackTrace();
        }

        //Log
        //plugin.getLogger().info("New Chat Hologram added to the list, Player: " + player.getName());

        //Radius Handler checker;
        if (Configuration.isRadius()) {
            RadiusHandler radiusHandler = new RadiusHandler(player,hologram);
            radiusHandlers.put(radiusHandler,player.getName());
            radiusHandler.start();
        }
    }

    @EventHandler
    public void onPlayerMovement(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        //plugin.getLogger().info("Movement detected from player: " + player.getLocation());
        for (String name : hologramas.keySet()) {

            //If name on Holograms Hash Map equals to Name of Moving Player, then get respective Hologram of it
            if (name.equals(player.getName())) {
                Hologram holograma = hologramas.get(name);
                holograma.teleport(player.getLocation().add(0,Configuration.getHologram_height(),0));
                //plugin.getLogger().info("Hologram teleported to: " + player.getLocation().add(0, 4, 0));
            }
        }
    }

    /**
     * Set visibility Action on all the existent Holograms
     * @param player Player invocating the action;
     * @param state true or false to show or hide;
     */
    public static void setVisibility(Player player, boolean state) {
        SQLLiteData sqlLiteData = new SQLLiteData();
        for (Hologram hologram : hologramas.values()) {
            if (state) {
                hologram.getVisibilityManager().showTo(player.getPlayer());
                hologramas_visibility.put(player, true);
            } else {
                hologram.getVisibilityManager().hideTo(player.getPlayer());
                hologramas_visibility.put(player, false);
            }
        }
    }

    /**
     * Player Quit Event, Write on SQLLite Data;
     * @param e
     */
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        for (Player playerCheck: hologramas_visibility.keySet()) {
            if (playerCheck.getName().equals(player.getName())) {
                if (hologramas_visibility.get(playerCheck)) {
                    try {
                        sqlLiteData.manageData("UPDATE visibility SET state = 1 WHERE uniqueid = '" + player.getUniqueId().toString() + "';");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    try {
                        sqlLiteData.manageData("UPDATE visibility SET state = 0 WHERE uniqueid = '" + player.getUniqueId().toString() + "';");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Chat Event
     * @param e Player Event
     */
    @EventHandler
    public void onPlayerInteractEvent(PlayerChatEvent e) {
        Player player = e.getPlayer();
        String message = e.getMessage();
        int size = message.length();
        if (!message.startsWith("/")) {
            //plugin.getLogger().info("Message detected by Player: " + player.getName() +  ",message: " + message);

            if (Configuration.getSpecial_chat()) {
                if (message.startsWith("!")) {
                    message = message.replaceFirst("!", "");
                    produceHologram(message, player,size);
                    e.setMessage(message);
                }
            } else {
                produceHologram(message, player,size);
            }

        }
    }

    /**
     * Produce the Hologram text lines in order
     * @param message New line message
     * @param player Player activating the event
     * @param size Size of the message
     */
    void produceHologram(String message, Player player, int size) {
        HologramHandler hologramHandler = null;
        for (String name : hologramas.keySet()) {

            //If name on Holograms Hash Map equals to Name of Moving Player, then get respective Hologram of it
            if (name.equals(player.getName())) {
                Hologram holograma = hologramas.get(name);

                for (String player_threadName : hologramThreads.keySet()) {
                    if (player_threadName.equals(name)) {
                        hologramHandler = hologramThreads.get(player_threadName);
                    }
                }

                if (hologramHandler == null) {
                    hologramHandler = new HologramHandler(holograma, size);
                    hologramThreads.put(player.getName(), hologramHandler);
                    hologramHandler.start();
                }

                if (holograma.size() < 1) {
                    TextLine textLine = holograma.appendTextLine(net.md_5.bungee.api.ChatColor.AQUA + player.getName() + ChatColor.GOLD + " " + Configuration.getHologram_prefix() + ":");
                    holograma.appendTextLine(ChatColor.YELLOW + message);

                } else if (holograma.size() > 1) {
                    if (holograma.size() < Configuration.getHologram_text_lines() + 1) {
                        holograma.appendTextLine(ChatColor.YELLOW + message);
                    } else {
                        holograma.removeLine(1);
                        holograma.appendTextLine(ChatColor.YELLOW + message);
                    }
                }

            }
        }
    }

    /**
     * Bug Fix, used to clear the Hologram on changing the world;
     * It will be with the lines on memory and it will not start with the prefix;
     * @param e
     */
    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent e) {
        Player player = e.getPlayer();

        for (String name : hologramas.keySet()) {
            if (name.equals(player.getName())) {
                hologramas.get(name).clearLines();
            }
        }
    }

    public static HashMap<String, HologramHandler> getHologramThreads() {
        return hologramThreads;
    }

    public static HashMap<String, Hologram> getHologramas() {
        return hologramas;
    }

    public static HashMap<Player, Boolean> getHologramas_visibility() {
        return hologramas_visibility;
    }
}