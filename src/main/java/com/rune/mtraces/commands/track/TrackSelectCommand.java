package com.rune.mtraces.commands;

import com.rune.mtraces.AbstractSubCommand;
import com.rune.mtraces.managers.TrackManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class TrackSelectCommand extends AbstractSubCommand {

    @Override
    public boolean onExecute(CommandSender sender, Command command, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("Gebruik: /track select <naam>");
            return true;
        }

        String trackName = args[1];
        TrackManager trackManager = TrackManager.getInstance();

        if (!trackManager.trackExists(trackName)) {
            sender.sendMessage("Track met naam " + trackName + " bestaat niet.");
            return true;
        }

        trackManager.selectTrack(trackName);
        sender.sendMessage("Track " + trackName + " is geselecteerd.");
        return true;
    }

    @Override
    public String getDescription() {
        return "Selecteer een track om mee te werken.";
    }

    @Override
    public String getUsage() {
        return "<naam>";
    }

    @Override
    public String getPermission() {
        return "mtraces.command.track.select";
    }
}
