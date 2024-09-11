package com.rune.mtraces;

import com.rune.mtraces.commands.race.RaceCommand;
import com.rune.mtraces.commands.track.TrackCommand;
import com.rune.mtraces.listeners.RaceListener;
import com.rune.mtraces.managers.ScoreboardManager;
import com.rune.mtraces.managers.TrackManager;
import com.rune.mtraces.utils.WorldGuardUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MTRaces extends JavaPlugin {

    private static MTRaces instance;
    private ScoreboardManager scoreboardManager;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("MTRaces has been enabled");
        this.getCommand("race").setExecutor(new RaceCommand());
        this.getCommand("track").setExecutor(new TrackCommand());
        Bukkit.getServer().getPluginManager().registerEvents(new RaceListener(), MTRaces.getInstance());
        TrackManager.getInstance().loadTracks();
        scoreboardManager = ScoreboardManager.getInstance();
    }

    @Override
    public void onDisable() {
        getLogger().info("MTRaces has been disabled");
        instance = null;
        TrackManager.getInstance().saveTracks();
    }

    public static MTRaces getInstance() {
        return instance;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }
}
