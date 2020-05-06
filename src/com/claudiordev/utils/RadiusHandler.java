package com.claudiordev.utils;

import com.claudiordev.config.Configuration;
import com.claudiordev.main.Actions;
import com.claudiordev.main.Main;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RadiusHandler extends Thread {

    Player player;
    Hologram hologram;
    int radius_distance = Configuration.getRadius_distance();

    public RadiusHandler(Player player, Hologram hologram) {
        this.player = player;
        this.hologram = hologram;
    }

    @Override
    public void run() {
        //Keep the Thread running infinitely
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            //Get the nearby entities in the radius chosen (by the config "radius-distance" value
            java.util.List<Entity> nearbyEntities = player.getNearbyEntities(radius_distance,radius_distance,radius_distance);

            java.util.List<Player> onlinePlayers = (List<Player>) Main.getPlugin().getServer().getOnlinePlayers();

            ArrayList<Player> nearbyPlayers = new ArrayList<>();

            for (Entity entity : nearbyEntities) {
                if (entity instanceof Player) {
                    Player radiusPlayer =  ((Player) entity).getPlayer();

                    //Complete the nearbyPlayers list with the List of Only players as entity
                    nearbyPlayers.add(radiusPlayer);
                }
            }

            for (Player nearbyPlayer : nearbyPlayers) {
                //Only works if the Player has the Hologram Toggle (on)
                if (Actions.getHologramas_visibility().get(nearbyPlayer)) {
                    hologram.getVisibilityManager().showTo(nearbyPlayer);
                }
            }

            for (Player onlinePlayer: onlinePlayers) {
                if (!nearbyPlayers.contains(onlinePlayer)) {
                    if (onlinePlayer != player) {
                        if (Actions.getHologramas_visibility().get(onlinePlayer)) {
                            hologram.getVisibilityManager().hideTo(onlinePlayer);
                        }
                    }
                }
            }

            nearbyPlayers.clear();
        }
    }
}
