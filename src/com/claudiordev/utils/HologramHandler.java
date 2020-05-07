package com.claudiordev.utils;

import com.claudiordev.config.Configuration;
import com.gmail.filoghost.holographicdisplays.api.Hologram;

public class HologramHandler extends Thread {

    Hologram hologram;
    boolean running = true;
    int size;

    public HologramHandler(Hologram hologram, int size) {
        this.hologram = hologram;
        this.size = size;
    }

    @Override
    public void run() {
        while (running) {
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

            if (hologram.size() == 2) {
                hologram.clearLines();
            } else if (hologram.size() > 2) {
                hologram.removeLine(1);
            }
        }

    }
}
