package com.claudiordev.main;

import com.claudiordev.config.Configuration;
import com.claudiordev.utils.HologramHandler;
import com.claudiordev.utils.HologramMessage;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.util.ArrayList;
import java.util.HashMap;

import static com.claudiordev.main.Main.getPlugin;
import static com.claudiordev.main.Main.plugin;

public class Actions implements Listener {
    //Player Name (Key), Hologram; Hologram List
    static HashMap<String, Hologram> hologramas = new HashMap<>();
    //Player Name (Key), Visibilit(Boolean)
    static HashMap<Player, Boolean> hologramas_visibility = new HashMap<>();
    static ArrayList<HologramMessage> hologramMessages = new ArrayList<>();
    HologramHandler hologramHandler;


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Hologram hologram = HologramsAPI.createHologram(Main.getPlugin(), player.getLocation().add(0, Configuration.getHologram_height(), 0));
        hologramas.put(player.getName(), hologram);
        hologramas_visibility.put(player, true);
        //Log
        //plugin.getLogger().info("New Chat Hologram added to the list, Player: " + player.getName());
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

                //Improvement Needed
                for (Player player_obj : hologramas_visibility.keySet()) {
                    if (hologramas_visibility.get(player_obj) == false) {
                        if (holograma.getVisibilityManager().isVisibleTo(player_obj)) {
                            holograma.getVisibilityManager().hideTo(player_obj);
                            //Log
                            //getPlugin().getLogger().info("Holograms Disabled to player: " + player_obj.getName());
                        }
                    } else if (hologramas_visibility.get(player_obj) == true) {
                        if (!holograma.getVisibilityManager().isVisibleTo(player_obj)) {
                            holograma.getVisibilityManager().showTo(player_obj);
                            //Log
                            //getPlugin().getLogger().info("Holograms Enabled to player: " + player_obj.getName());
                        }
                    }
                }
            }
        }
    }

    @Deprecated
    public static void setVisibility(Player player, boolean state) {
        for (Hologram hologram : hologramas.values()) {
            if (state)
                hologram.getVisibilityManager().showTo(player.getPlayer());
            else {
                hologram.getVisibilityManager().hideTo(player.getPlayer());
            }
            getPlugin().getLogger().info("set Visibility invocated!");
        }
    }

    //Chat Event
    @EventHandler
    public void onPlayerInteractEvent(PlayerChatEvent e) {
        Player player = e.getPlayer();
        String message = e.getMessage();
        int size = message.length();
        if (!message.startsWith("/")) {
            //plugin.getLogger().info("Message detected by Player: " + player.getName() +  ",message: " + message);

            if (Configuration.getSpecial_chat()) {
                if (message.startsWith("!")) {
                    message = message.replace("!", "");

                    for (String name : hologramas.keySet()) {

                        //If name on Holograms Hash Map equals to Name of Moving Player, then get respective Hologram of it
                        if (name.equals(player.getName())) {
                            Hologram holograma = hologramas.get(name);

                            long random_id = (long) Math.random();

                            HologramMessage hologramMessage = new HologramMessage(message, random_id);
                            hologramMessages.add(hologramMessage);

                            hologramHandler = new HologramHandler(holograma, size, random_id, hologramMessage);

                            hologramHandler.start();

                            if (holograma.size() < 1) {
                                TextLine textLine = holograma.appendTextLine(net.md_5.bungee.api.ChatColor.AQUA + player.getName() + ChatColor.GOLD + " " + Configuration.getHologram_prefix() + ":");
                                holograma.appendTextLine(ChatColor.YELLOW + hologramMessage.getContent());
                            } else if (holograma.size() > 1) {
                                if (holograma.size() < Configuration.getHologram_text_lines() + 1) {
                                    holograma.appendTextLine(ChatColor.YELLOW + message);
                                } else {
                                    holograma.removeLine(1);
                                    holograma.appendTextLine(ChatColor.YELLOW + message);
                                }
                            }

                            //Handle the clearLines of each Hologram through a separated Thread on HologramHandler
                            /**if (hologramHandler != null) {
                             hologramHandler.setRunning(false);
                             hologramHandler = null;
                             }**/
                        }
                    }

                } else {

                }
            } else {

                for (String name : hologramas.keySet()) {

                    //If name on Holograms Hash Map equals to Name of Moving Player, then get respective Hologram of it
                    if (name.equals(player.getName())) {
                        Hologram holograma = hologramas.get(name);

                        long random_id = (long) Math.random();

                        HologramMessage hologramMessage = new HologramMessage(message, random_id);
                        hologramMessages.add(hologramMessage);

                        hologramHandler = new HologramHandler(holograma, size, random_id, hologramMessage);

                        hologramHandler.start();

                        if (holograma.size() < 1) {
                            TextLine textLine = holograma.appendTextLine(net.md_5.bungee.api.ChatColor.AQUA + player.getName() + ChatColor.GOLD + " " + Configuration.getHologram_prefix() + ":");
                            holograma.appendTextLine(ChatColor.YELLOW + hologramMessage.getContent());
                        } else if (holograma.size() > 1) {
                            if (holograma.size() < Configuration.getHologram_text_lines() + 1) {
                                holograma.appendTextLine(ChatColor.YELLOW + message);
                            } else {
                                holograma.removeLine(1);
                                holograma.appendTextLine(ChatColor.YELLOW + message);
                            }
                        }

                        //Handle the clearLines of each Hologram through a separated Thread on HologramHandler
                        /**if (hologramHandler != null) {
                         hologramHandler.setRunning(false);
                         hologramHandler = null;
                         }**/
                    }
                }
            }

        }
    }
}
