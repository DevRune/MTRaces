package com.rune.mtraces;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract class AbstractSubCommand {

    public abstract boolean onExecute(CommandSender sender, Command command, String[] args);

    public abstract String getDescription();
    public abstract String getUsage();
    public abstract String getPermission();

}
