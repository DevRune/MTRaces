package com.rune.mtraces.commands.race;

import com.rune.mtraces.AbstractCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RaceCommand extends AbstractCommand {

    public RaceCommand() {
        // Register all subcommands
        registerSubCommand("addplayer", new RaceAddPlayerCommand());
        registerSubCommand("removeplayer", new RaceRemovePlayerCommand());
        registerSubCommand("create", new RaceCreateCommand());
        registerSubCommand("list", new RaceListCommand());
        registerSubCommand("start", new RaceStartCommand());
        registerSubCommand("stop", new RaceStopCommand());
        registerSubCommand("loadplayers", new RaceLoadPlayersCommand());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        this.command = label;
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cEnkel spelers kunnen dit command uitvoeren.");
            return true;
        }

        if (args.length == 0) {
            sendHelpMessage(sender);
            return true;
        }

        boolean goed = false;

        for (String subCommand : subCommands.keySet()) {
            if (args[0].equalsIgnoreCase(subCommand)) {
                goed = executeSubCommand(subCommands.get(subCommand), sender, command, args);
                break; // Stop iterating once the correct subcommand is found
            }
        }

        if (!goed) {
            sendHelpMessage(sender);
        }
        return true;
    }
}
