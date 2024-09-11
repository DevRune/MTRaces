package com.rune.mtraces;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

public abstract class AbstractCommand implements CommandExecutor {

    protected HashMap<String, AbstractSubCommand> subCommands = new HashMap<>();
    protected String command = "";

    public boolean executeSubCommand(AbstractSubCommand subCommand, CommandSender sender, Command command, String[] args){
        if(!sender.hasPermission(subCommand.getPermission())){
            sender.sendMessage("§cTo use this command, you need permission " + subCommand.getPermission() + ".");
            return true;
        }
        return subCommand.onExecute(sender, command, args);
    }

    public void registerSubCommand(String name, AbstractSubCommand subCommand){
        subCommands.put(name, subCommand);
    }

    public void sendHelpMessage(CommandSender sender){
        sender.sendMessage("§6/" + command + " <subcommand> <arg>...");
        for(String name : subCommands.keySet()){
            if(sender.hasPermission(subCommands.get(name).getPermission())){
                sender.sendMessage("§a/" + command + " §2" + name + " §a" + subCommands.get(name).getUsage() + " §f- §a" + subCommands.get(name).getDescription());
            }
        }
    }

}
