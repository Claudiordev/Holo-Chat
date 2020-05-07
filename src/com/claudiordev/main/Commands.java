package com.claudiordev.main;

import com.claudiordev.config.Configuration;
import com.claudiordev.files.MessagesFile;
import com.claudiordev.utils.ColorCodes;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    ColorCodes colorCodes = new ColorCodes();

    /**
     *
     * @param commandSender represents whatever is sending the command.
     *                      This could be a Player, ConsoleCommandSender, or BlockCommandSender (a command block)
     * @param command represents which is the command being called. This will almost always be known ahead of time.
     * @param label represents the exact first word of the command (excluding arguments) that was entered by the sender
     * @param strings is the remainder of the command statement (the arguments after the label) split up by spaces and put into an array.
     * @return true if the command is executed
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] strings) {
        if (label.equals("hdchat")) {
            if (strings.length > 0) {
                //Only available with ProtocolLib
                if (strings[0].equals("toggle")) {
                    if (commandSender instanceof Player) {
                        Player player = (Player) commandSender;
                        if (Main.isToggleState()) {
                            if (Actions.getHologramas_visibility().get(player)) {
                                Actions.setVisibility(player, false);
                                player.sendMessage(colorCodes.executeReplace(MessagesFile.getFileConfiguration().getString("Hologram-Deactivated")));
                            } else {
                                Actions.setVisibility(player, true);
                                player.sendMessage(colorCodes.executeReplace(MessagesFile.getFileConfiguration().getString("Hologram-Activated")));
                            }
                        } else {
                            player.sendMessage(colorCodes.executeReplace(MessagesFile.getFileConfiguration().getString("Cmd-Error")));
                        }
                    } else {
                        Main.getPlugin().getLogger().info(MessagesFile.getFileConfiguration().getString("Cmd-Only-Player"));
                    }
                }
            } else {
                //If non argument stated, show the help message defined on the config.yml
                for (String e: Configuration.getHelp_message()) {
                    //player.sendMessage(new ColorCodes().executeReplace(e));
                    commandSender.sendMessage(colorCodes.executeReplace(e));
                }
            }
        }

        //If true it will run the command silently, without the notification of the tip of Bukkit
        return true;
    }
}
