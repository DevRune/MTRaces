package com.rune.mtraces.commands.track;

import com.rune.mtraces.AbstractSubCommand;
import com.rune.mtraces.managers.TrackManager;
import com.rune.mtraces.tracks.Track;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TrackSetLightRotationCommand extends AbstractSubCommand {

    @Override
    public boolean onExecute(CommandSender sender, Command command, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("§cGebruik: /track setlightrotation <rotation>");
            return true;
        }

        String rotationStr = args[1];
        BlockFace rotation;

        try {
            rotation = BlockFace.valueOf(rotationStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage("§cOngeldige rotatie. Gebruik een geldige BlockFace.");
            return true;
        }

        Track track = TrackManager.getInstance().getSelectedTrack();

        if (track == null) {
            sender.sendMessage("§cEr is momenteel geen track geselecteerd.");
            return true;
        }

        track.setLightRotation(rotation);
        sender.sendMessage("§aLichtrotatie ingesteld op " + rotation.toString());
        return true;
    }

    @Override
    public String getDescription() {
        return "Stel de rotatie in van de lichtskulls.";
    }

    @Override
    public String getUsage() {
        return "<rotation>";
    }

    @Override
    public String getPermission() {
        return "mtraces.command.track.setlightrotation";
    }
}
