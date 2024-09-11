package com.rune.mtraces.commands.track;

import com.rune.mtraces.AbstractSubCommand;
import com.rune.mtraces.managers.TrackManager;
import com.rune.mtraces.tracks.Track;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;
import java.lang.reflect.Field;

public class TrackAddLightCommand extends AbstractSubCommand {

    @Override
    public boolean onExecute(CommandSender sender, Command command, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cDit commando kan alleen door spelers worden uitgevoerd.");
            return true;
        }

        Player player = (Player) sender;
        Block block = player.getTargetBlockExact(5);

        if (block == null) {
            sender.sendMessage("§cJe kijkt naar geen blok of het blok is te ver weg.");
            return true;
        }

        if (block.getType() != Material.PLAYER_HEAD && block.getType() != Material.PLAYER_WALL_HEAD) {
            sender.sendMessage("§cJe moet een skull bekijken om deze als licht toe te voegen.");
            return true;
        }

        Track track = TrackManager.getInstance().getSelectedTrack();

        if (track == null) {
            sender.sendMessage("§cEr is momenteel geen track geselecteerd.");
            return true;
        }

        BlockState state = block.getState();
        if (!(state instanceof Skull)) {
            sender.sendMessage("§cHet blok is geen skull.");
            return true;
        }

        BlockFace rotation = ((Skull) state).getRotation();

        track.addLightLocation(block.getLocation());
        sender.sendMessage("§aLicht toegevoegd op locatie " + block.getLocation().toString() + " met rotatie " + rotation.toString());
        return true;
    }

    @Override
    public String getDescription() {
        return "Voeg een licht toe aan de track op de locatie van de skull.";
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public String getPermission() {
        return "mtraces.command.track.addlight";
    }
}
