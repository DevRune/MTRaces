package com.rune.mtraces.commands.track;

import com.rune.mtraces.AbstractCommand;
import com.rune.mtraces.commands.TrackSelectCommand;
import com.rune.mtraces.commands.track.TrackCreateCommand;
import com.rune.mtraces.commands.track.TrackAddLightCommand;
import com.rune.mtraces.commands.track.TrackAddBarrierCommand;
import com.rune.mtraces.commands.track.TrackSetLightRotationCommand;
import com.rune.mtraces.commands.track.TrackAddCheckpointCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TrackCommand extends AbstractCommand {

    public TrackCommand() {
        registerSubCommand("create", new TrackCreateCommand());
        registerSubCommand("addlight", new TrackAddLightCommand());
        registerSubCommand("addbarrier", new TrackAddBarrierCommand());
        registerSubCommand("setlightrotation", new TrackSetLightRotationCommand());
        registerSubCommand("addcheckpoint", new TrackAddCheckpointCommand());
        registerSubCommand("select", new TrackSelectCommand());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        this.command = label;
        if(!(sender instanceof Player)){
            sender.sendMessage("§cEnkel spelers kunnen dit command uitvoeren.");
            return true;
        }

        if(args.length == 0){
            sendHelpMessage(sender);
            return true;
        }

        boolean goed = false;

        for(String subCommand : subCommands.keySet()){
            if(args[0].equalsIgnoreCase(subCommand)){
                goed = executeSubCommand(subCommands.get(subCommand), sender, command, args);
            }
        }

        if(!goed){
            sendHelpMessage(sender);
        }
        return true;
    }
}
