package com.rune.mtraces.commands.track;

import com.rune.mtraces.AbstractSubCommand;
import com.rune.mtraces.managers.TrackManager;
import com.rune.mtraces.tracks.Track;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TrackAddBarrierCommand extends AbstractSubCommand {

    @Override
    public boolean onExecute(CommandSender sender, Command command, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cDit commando kan alleen door spelers worden uitgevoerd.");
            return true;
        }

        Player player = (Player) sender;
        Block block = player.getTargetBlock(null, 5);

        if (block.getType() != Material.BARRIER) {
            sender.sendMessage("§cJe moet een barrier bekijken om deze als barrier toe te voegen.");
            return true;
        }

        Track track = TrackManager.getInstance().getSelectedTrack();

        if (track == null) {
            sender.sendMessage("§cEr is momenteel geen track geselecteerd.");
            return true;
        }

        track.addBarrierLocation(block.getLocation());
        sender.sendMessage("§aBarrier toegevoegd op locatie " + block.getLocation().toString());
        return true;
    }

    @Override
    public String getDescription() {
        return "Voeg een barrier toe aan de track op de locatie van de barrier.";
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public String getPermission() {
        return "mtraces.command.track.addbarrier";
    }
}
