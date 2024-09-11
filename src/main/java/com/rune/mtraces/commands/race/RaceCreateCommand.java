package com.rune.mtraces.commands.race;

import com.rune.mtraces.AbstractSubCommand;
import com.rune.mtraces.managers.RaceManager;
import com.rune.mtraces.managers.TrackManager;
import com.rune.mtraces.tracks.Track;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class RaceCreateCommand extends AbstractSubCommand {

    @Override
    public boolean onExecute(CommandSender sender, Command command, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Dit commando kan alleen door spelers worden uitgevoerd.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 3) {
            sender.sendMessage(ChatColor.YELLOW + "Gebruik: /race create <type> <baan> [laps]");
            return true;
        }

        String type = args[1];
        String trackName = args[2];
        int laps = 1;

        if (type.equalsIgnoreCase("Slip") || type.equalsIgnoreCase("Drag")) {
            laps = 0; // 0 betekent dat laps niet worden gebruikt
        } else if (args.length >= 4) {
            try {
                laps = Integer.parseInt(args[3]) + 1;
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Aantal laps moet een nummer zijn.");
                return true;
            }
        }

        if (!RaceManager.getInstance().isValidRaceType(type)) {
            sender.sendMessage(ChatColor.RED + "Ongeldig race type. Beschikbare types: Knockout, Slip, Drag.");
            return true;
        }

        if (!TrackManager.getInstance().trackExists(trackName)) {
            sender.sendMessage(ChatColor.RED + "De opgegeven baan bestaat niet.");
            return true;
        }

        RaceManager.getInstance().createRace(type, trackName, laps, player);
        sender.sendMessage(ChatColor.GREEN + "Race gecreëerd: Type: " + ChatColor.AQUA + type + ChatColor.GREEN +
                ", Baan: " + ChatColor.AQUA + trackName + (laps > 0 ? ChatColor.GREEN + ", Laps: " + ChatColor.AQUA + laps : ""));
        return true;
    }

    @Override
    public String getDescription() {
        return "Creëer een nieuwe race.";
    }

    @Override
    public String getUsage() {
        return "<type> <baan> [laps]";
    }

    @Override
    public String getPermission() {
        return "mtraces.command.create";
    }

}
