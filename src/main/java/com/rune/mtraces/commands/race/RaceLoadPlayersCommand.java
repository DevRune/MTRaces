package com.rune.mtraces.commands.race;

import com.rune.mtraces.AbstractSubCommand;
import com.rune.mtraces.managers.RaceManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RaceLoadPlayersCommand extends AbstractSubCommand {

    @Override
    public boolean onExecute(CommandSender sender, Command command, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Dit commando kan alleen door spelers worden uitgevoerd.");
            return true;
        }

        Player player = (Player) sender;

        if (!RaceManager.getInstance().isHost(player)) {
            sender.sendMessage(ChatColor.RED + "Je moet de host van de race zijn om de spelers te laden.");
            return true;
        }

        // Laad de spelers voor de race
        RaceManager.getInstance().loadPlayers(player);
        sender.sendMessage(ChatColor.GREEN + "Spelers zijn geladen.");
        return true;
    }

    @Override
    public String getDescription() {
        return "Laad de spelers voor de race.";
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public String getPermission() {
        return "mtraces.command.loadplayers";
    }
}
