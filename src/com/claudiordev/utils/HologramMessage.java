package com.claudiordev.utils;

import com.claudiordev.main.Main;

public class HologramMessage {

    String content;
    long id;

    public HologramMessage(String content, long id) {
        this.content = content;
        this.id = id;
        //Log
        //Main.getPlugin().getLogger().info("Hologram Message created with ID: " + id + " content: " + content);
    }

    public String getContent() {
        return content;
    }

}
