package com.rune.mtraces.races;

import com.rune.mtraces.MTRaces;
import com.rune.mtraces.listeners.RaceListener;
import com.rune.mtraces.managers.ScoreboardManager;
import com.rune.mtraces.tracks.Track;
import com.rune.mtraces.utils.WorldGuardUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class AbstractRace implements Race {
    protected String type;
    protected Track track;
    protected int laps;
    protected List<Player> participants = new ArrayList<>();
    protected boolean raceOngoing = false;
    protected Player host;
    private final Map<Player, Long> fastestRoundTime = new HashMap<>();
    private final Map<Player, Long> lapStartTime = new HashMap<>();

    // Track checkpoint progress
    protected Map<Player, Integer> checkpointProgress = new HashMap<>();
    // Track lap-checkpoint timestamps
    protected Map<Player, Map<Integer, Long>> lapCheckpointTimestamps = new HashMap<>();

    @Override
    public void start() {
        raceOngoing = true;
        updateScoreboard();
        for(Player player : participants){
            lapStartTime.put(player, System.currentTimeMillis());
        }
    }

    @Override
    public void stop() {
        raceOngoing = false;
        Bukkit.broadcastMessage("§7------- §aFastest Round Time §7-------");
        participants.stream()
                .sorted((p1, p2) -> Long.compare(fastestRoundTime.getOrDefault(p2, Long.MAX_VALUE), fastestRoundTime.getOrDefault(p1, Long.MAX_VALUE)))
                .forEach(player -> Bukkit.broadcastMessage(ChatColor.GRAY + player.getName() + ChatColor.DARK_GRAY + " - " + ChatColor.WHITE + formatTime(fastestRoundTime.getOrDefault(player, Long.MAX_VALUE))));
        clearScoreboard();
    }

    public static String formatTime(long milliseconds) {
        // Convert milliseconds to seconds and milliseconds
        long seconds = milliseconds / 1000;
        long millis = milliseconds % 1000;

        // Calculate minutes and remaining seconds
        long minutes = seconds / 60;
        seconds = seconds % 60;

        // Format time based on the length
        if (minutes > 0) {
            // Format: mm:ss.ms
            return String.format("%d:%02d.%03d", minutes, seconds, millis);
        } else {
            // Format: ss.mmmm
            return String.format("%d.%03d", seconds, millis);
        }
    }

    @Override
    public void addParticipant(Player player) {
        if (!participants.contains(player)) {
            participants.add(player);
            checkpointProgress.put(player, 0); // Initialize checkpoint progress
            lapCheckpointTimestamps.put(player, new HashMap<>()); // Initialize lap-checkpoint timestamps
            updateScoreboard();
        }
    }

    @Override
    public void removeParticipant(Player player) {
        participants.remove(player);
        checkpointProgress.remove(player);
        lapCheckpointTimestamps.remove(player);
        updateScoreboard();
    }

    @Override
    public boolean isRaceOngoing() {
        return raceOngoing;
    }

    @Override
    public List<Player> getParticipants() {
        return participants;
    }

    public abstract void updateRaceStatus();

    public void updateScoreboard() {
        for (Player participant : participants) {
            participant.setScoreboard(MTRaces.getInstance().getScoreboardManager().getScoreboard());
        }
        host.setScoreboard(MTRaces.getInstance().getScoreboardManager().getScoreboard());
    }

    public void clearScoreboard(){
        for (Player participant : participants) {
            participant.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

        }
        host.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

    }

    // Update checkpoint progress for a player
    public void updateCheckpointProgress(Player player, int checkpoint) {
        checkpointProgress.put(player, checkpoint);
    }

    // Update lap checkpoint timestamp for a player
    public void updateLapCheckpointTimestamp(Player player, int lap, long timestamp) {
        lapCheckpointTimestamps.computeIfAbsent(player, k -> new HashMap<>()).put(lap, timestamp);
    }

    // Retrieve checkpoint progress
    public int getCheckpointProgress(Player player) {
        return checkpointProgress.getOrDefault(player, 0);
    }

    // Retrieve lap checkpoint timestamp
    public long getLapCheckpointTimestamp(Player player, int lap) {
        return lapCheckpointTimestamps.getOrDefault(player, new HashMap<>()).getOrDefault(lap, 0L);
    }

    public Track getTrack() {
        return track;
    }

    // Abstract methods to be implemented in subclasses
    public abstract boolean isFinished(Player player);

    protected abstract boolean isEliminated(Player player);

    protected abstract boolean hasFastestTime(Player player);

    public abstract int getScoreForPlayer(Player player);

    public void setHost(Player host) {
        this.host = host;
    }

    public Map<Player, Long> getFastestRoundTime() {
        return fastestRoundTime;
    }

    public Map<Player, Long> getLapStartTime() {
        return lapStartTime;
    }
}
