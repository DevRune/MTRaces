package com.rune.mtraces.commands.track;

import com.rune.mtraces.AbstractSubCommand;
import com.rune.mtraces.managers.TrackManager;
import com.rune.mtraces.tracks.Track;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TrackAddCheckpointCommand extends AbstractSubCommand {

    @Override
    public boolean onExecute(CommandSender sender, Command command, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("§cGebruik: /track addcheckpoint <region>");
            return true;
        }

        String regionName = args[1];

        Track track = TrackManager.getInstance().getSelectedTrack();

        if (track == null) {
            sender.sendMessage("§cEr is momenteel geen track geselecteerd.");
            return true;
        }

        track.addCheckpoint(regionName);
        sender.sendMessage("§aCheckpoint toegevoegd met naam " + regionName);
        return true;
    }

    @Override
    public String getDescription() {
        return "Voeg een checkpoint toe aan de track met de opgegeven regionnaam.";
    }

    @Override
    public String getUsage() {
        return "<region>";
    }

    @Override
    public String getPermission() {
        return "mtraces.command.track.addcheckpoint";
    }
}
