package com.rune.mtraces.races;

import com.rune.mtraces.tracks.Track;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class DragRace extends AbstractRace {

    private final Map<Player, Long> finishTimes = new HashMap<>(); // Stores finish times for players
    private boolean raceFinished = false;

    public DragRace(Track track) {
        this.track = track;
        this.type = "Drag";
        this.laps = 0; // Drag race heeft geen laps
    }

    @Override
    public void updateRaceStatus() {
        boolean allFinished = true;
        for (Player player : participants) {
            if (isFinished(player)) {
                finishRaceForPlayer(player);
            } else {
                allFinished = false;
            }
        }
        if (allFinished) {
            stop();
        }

        updateScoreboard();
    }

    @Override
    public boolean isFinished(Player player) {
        return finishTimes.containsKey(player);
    }

    @Override
    protected boolean isEliminated(Player player) {
        return false;
    }

    @Override
    protected boolean hasFastestTime(Player player) {
        if (finishTimes.isEmpty()) {
            return false;
        }

        Long playerFinishTime = finishTimes.get(player);
        if (playerFinishTime == null) {
            return false;
        }

        // Check if the player has the fastest finish time
        return finishTimes.values().stream().min(Long::compareTo).orElse(Long.MAX_VALUE).equals(playerFinishTime);
    }

    @Override
    public int getScoreForPlayer(Player player) {
        if (!raceFinished) {
            return 0;
        }

        // Determine player's score based on finish time
        long playerFinishTime = finishTimes.getOrDefault(player, Long.MAX_VALUE);

        // Sort players by finish time
        int position = 1;
        for (Long time : finishTimes.values()) {
            if (time < playerFinishTime) {
                position++;
            }
        }

        return position;
    }

    @Override
    public void finishRaceForPlayer(Player player) {
        finishTimes.put(player, System.currentTimeMillis());
    }
}
