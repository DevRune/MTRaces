package com.rune.mtraces.managers;

import com.rune.mtraces.races.AbstractRace;
import com.rune.mtraces.races.DragRace;
import com.rune.mtraces.races.LMSRace;
import com.rune.mtraces.races.CircuitRace;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.List;

public class ScoreboardManager {
    private static ScoreboardManager instance;
    private Scoreboard scoreboard;
    private Objective objective;

    private ScoreboardManager() {
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = scoreboard.registerNewObjective("race", "dummy", "Race Scoreboard");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public static ScoreboardManager getInstance() {
        if (instance == null) {
            instance = new ScoreboardManager();
        }
        return instance;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public void updateScoreboard(AbstractRace race) {
        for(String entry : scoreboard.getEntries()){
            scoreboard.resetScores(entry);
        }
        List<Player> participants = race.getParticipants();
        String raceType = race instanceof CircuitRace ? "Circuit Race" : race instanceof DragRace ? "Drag Race" : "LMS Race";

        if (raceType.equals("Circuit Race")) {
            updateCircuitRaceScoreboard(participants, (CircuitRace) race);
        } else if (raceType.equals("Drag Race")) {
            updateDragRaceScoreboard(participants, (DragRace) race);
        } else {
            updateLMSRaceScoreboard(participants, (LMSRace) race);
        }
    }

    private void updateLMSRaceScoreboard(List<Player> participants, LMSRace race) {
        participants.stream()
                .sorted((p1, p2) -> Integer.compare(race.getScoreForPlayer(p2), race.getScoreForPlayer(p1)))
                .forEach(player -> {
                    String prefix;
                    if (race.isEliminated(player)) {
                        prefix = race.hasFastestTime(player) ? "&4" : "&c";
                    } else {
                        prefix = race.hasFastestTime(player) ? "&e" : "&7";
                    }
                    objective.getScore(ChatColor.translateAlternateColorCodes('&', prefix) + player.getName()).setScore(race.getScoreForPlayer(player));
                });
    }

    private void updateDragRaceScoreboard(List<Player> participants, DragRace race) {
        participants.stream()
                .sorted((p1, p2) -> Integer.compare(race.getScoreForPlayer(p2), race.getScoreForPlayer(p1)))
                .forEach(player -> {
                    String prefix;
                    if (race.isFinished(player)) {
                        prefix = race.getScoreForPlayer(player) == 1 ? "&a" : "&c";
                    } else {
                        prefix = "&7";
                    }
                    objective.getScore(ChatColor.translateAlternateColorCodes('&', prefix) + player.getName()).setScore(race.getScoreForPlayer(player));
                });
    }

    private void updateCircuitRaceScoreboard(List<Player> participants, CircuitRace race) {
        participants.stream()
                .sorted((p1, p2) -> Integer.compare(race.getScoreForPlayer(p2), race.getScoreForPlayer(p1)))
                .forEach(player -> {
                    String prefix;
                    if (race.isFinished(player)) {
                        prefix = race.hasFastestTime(player) ? "&2" : "&a";
                    } else {
                        prefix = race.hasFastestTime(player) ? "&e" : "&7";
                    }
                    objective.getScore(ChatColor.translateAlternateColorCodes('&', prefix) + player.getName()).setScore(race.getNormalizedScores().get(player));
                });
    }
}
