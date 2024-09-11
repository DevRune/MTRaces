package com.rune.mtraces.commands.track;

import com.rune.mtraces.AbstractSubCommand;
import com.rune.mtraces.managers.TrackManager;
import com.rune.mtraces.tracks.Track;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class TrackCreateCommand extends AbstractSubCommand {

    @Override
    public boolean onExecute(CommandSender sender, Command command, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("§cGebruik: /track create <naam> <type>");
            return true;
        }

        String name = args[1];

        Track track = new Track(name);

        TrackManager.getInstance().addTrack(name, track);

        sender.sendMessage("§aTrack " + name + " is aangemaakt.");
        return true;
    }

    @Override
    public String getDescription() {
        return "Maak een nieuwe track aan.";
    }

    @Override
    public String getUsage() {
        return "<naam>";
    }

    @Override
    public String getPermission() {
        return "mtraces.command.track.create";
    }
}
