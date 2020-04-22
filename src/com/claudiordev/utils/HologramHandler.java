package com.claudiordev.utils;

import com.claudiordev.config.Configuration;
import com.claudiordev.main.Main;
import com.gmail.filoghost.holographicdisplays.api.Hologram;

public class HologramHandler extends Thread {

    Hologram hologram;
    boolean running = true;
    int size;
    long id;
    HologramMessage hologramMessage;

    public HologramHandler(Hologram hologram, int size, long id, HologramMessage hologramMessage) {
        this.hologram = hologram;
        this.size = size;
        this.id = id;
        this.hologramMessage = hologramMessage;
        //Log
        //Main.getPlugin().getLogger().info("New thread, ID: " + id);
    }

    @Override
    public void run() {
        try {
            if (Configuration.isHologram_time_fixed()) {
                Thread.sleep(Configuration.getHologram_time());
            } else {
                double size_value = size / 30d;
                Thread.sleep((long) (size_value * 15000));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //If running through the same line on the Hologram, complets the Clearline
        if (running) {
            //hologram.clearLines();
            if (hologram.size() == 2) {
                hologram.clearLines();
            } else if (hologram.size() > 2) {
                hologram.removeLine(1);
            }
            Main.getPlugin().getLogger().info(Configuration.getTag() + " ClearLines on HologramHandler activated");
        }

    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
