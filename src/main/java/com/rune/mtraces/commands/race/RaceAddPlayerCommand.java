package com.rune.mtraces.commands.race;

import com.rune.mtraces.AbstractSubCommand;
import com.rune.mtraces.managers.RaceManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RaceAddPlayerCommand extends AbstractSubCommand {

    @Override
    public boolean onExecute(CommandSender sender, Command command, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Dit commando kan alleen door spelers worden uitgevoerd.");
            return true;
        }

        Player player = (Player) sender;
        if (args.length < 2) {
            sender.sendMessage(ChatColor.YELLOW + "Gebruik: " + ChatColor.GREEN + "/race addplayer <speler>");
            return true;
        }

        String targetPlayerName = args[1];
        Player targetPlayer = player.getServer().getPlayer(targetPlayerName);
        if (targetPlayer == null) {
            sender.sendMessage(ChatColor.RED + "De opgegeven speler is niet online.");
            return true;
        }

        if (!RaceManager.getInstance().isHost(player)) {
            sender.sendMessage(ChatColor.RED + "Alleen de host kan spelers toevoegen.");
            return true;
        }

        RaceManager.getInstance().addParticipant(targetPlayer);
        sender.sendMessage(ChatColor.GREEN + "Speler " + ChatColor.YELLOW + targetPlayerName + ChatColor.GREEN + " is toegevoegd aan de race.");
        return true;
    }

    @Override
    public String getDescription() {
        return "Voeg een speler toe aan de race.";
    }

    @Override
    public String getUsage() {
        return "<speler>";
    }

    @Override
    public String getPermission() {
        return "mtraces.command.addplayer";
    }
}
