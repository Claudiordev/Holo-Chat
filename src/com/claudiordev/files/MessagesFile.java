package com.claudiordev.files;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class MessagesFile extends AbstractFile {

    public MessagesFile(File dataFolder, String fileName) {
        //Calls the AbstractFile construct and use uppon this class;
        super(dataFolder, fileName);
    }
}
