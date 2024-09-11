package com.rune.mtraces.commands.race;

import com.rune.mtraces.AbstractSubCommand;
import com.rune.mtraces.managers.RaceManager;
import com.rune.mtraces.races.AbstractRace;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RaceListCommand extends AbstractSubCommand {

    @Override
    public boolean onExecute(CommandSender sender, Command command, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Dit commando kan alleen door spelers worden uitgevoerd.");
            return true;
        }

        Player player = (Player) sender;

        // Verkrijg de huidige race
        AbstractRace currentRace = RaceManager.getInstance().getCurrentRace();

        if (currentRace == null) {
            sender.sendMessage(ChatColor.RED + "Er is momenteel geen actieve race.");
            return true;
        }

        // Controleer of de speler deelneemt aan de huidige race
        if (!currentRace.getParticipants().contains(player)) {
            sender.sendMessage(ChatColor.RED + "Je neemt niet deel aan de huidige race.");
            return true;
        }

        // Toon de deelnemers
        sender.sendMessage(ChatColor.GREEN + "Deelnemers aan de huidige race:");
        for (Player participant : currentRace.getParticipants()) {
            sender.sendMessage(ChatColor.YELLOW + " - " + participant.getName());
        }

        return true;
    }

    @Override
    public String getDescription() {
        return "Toon de spelers die deelnemen aan de huidige race.";
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public String getPermission() {
        return "mtraces.command.list";
    }
}
