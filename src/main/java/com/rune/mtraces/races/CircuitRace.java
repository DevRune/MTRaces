package com.rune.mtraces.races;

import com.rune.mtraces.tracks.Track;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class CircuitRace extends AbstractRace {

    private final Map<Player, Integer> checkpointProgress = new HashMap<>();
    private final Map<Player, Integer> lapProgress = new HashMap<>();
    private final Map<Player, Long> finishTimes = new HashMap<>();

    public CircuitRace(Track track, int laps) {
        this.track = track;
        this.type = "Circuit";
        this.laps = laps;
    }

    @Override
    public void stop() {
        Bukkit.broadcastMessage("§7------- §aFinish Times §7-------");
        super.stop();
        participants.stream()
                .sorted((p1, p2) -> Long.compare(finishTimes.getOrDefault(p2, Long.MAX_VALUE), finishTimes.getOrDefault(p1, Long.MAX_VALUE)))
                .forEach(player -> Bukkit.broadcastMessage(ChatColor.GRAY + player.getName() + ChatColor.DARK_GRAY + " - " + ChatColor.WHITE + formatTime(finishTimes.getOrDefault(player, Long.MAX_VALUE))));
    }

    @Override
    public void updateRaceStatus() {
        boolean allFinished = true;
        for (Player player : participants) {
            if(!lapProgress.containsKey(player)){
                lapProgress.put(player, 0);
            }
            if(!checkpointProgress.containsKey(player)){
                checkpointProgress.put(player, 0);
            }
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
    public void finishRaceForPlayer(Player player) {
        finishTimes.put(player, System.currentTimeMillis());
        updateScoreboard();
    }

    @Override
    public boolean isFinished(Player player) {
        return lapProgress.get(player) >= laps;
    }

    @Override
    public boolean isEliminated(Player player) {
        return false;
    }

    @Override
    public boolean hasFastestTime(Player player) {
        if (finishTimes.isEmpty()) {
            return false;
        }
        long playerTime = finishTimes.getOrDefault(player, Long.MAX_VALUE);
        return finishTimes.values().stream().min(Long::compareTo).orElse(Long.MAX_VALUE).equals(playerTime);
    }

    @Override
    public int getScoreForPlayer(Player player) {
        // Calculate score based on finish time, laps, and checkpoints
        int lapsCompleted = lapProgress.getOrDefault(player, 0);
        int checkpoints = checkpointProgress.getOrDefault(player, 0);

        // Calculate score based on finish time (ascending order, so earliest finish first)
        long finishTime = finishTimes.getOrDefault(player, Long.MAX_VALUE);
        return (int) (finishTime + (lapsCompleted * 1000) + (checkpoints * 10));
    }

    public Map<Player, Integer> getNormalizedScores() {
        // Get the number of participants
        int numParticipants = participants.size();

        // Create a map to hold the normalized scores
        Map<Player, Integer> normalizedScores = new HashMap<>();

        // Calculate the raw scores for each player
        Map<Player, Integer> rawScores = new HashMap<>();
        for (Player player : participants) {
            rawScores.put(player, getScoreForPlayer(player));
        }

        // Determine min and max scores
        int minScore = Collections.min(rawScores.values());
        int maxScore = Collections.max(rawScores.values());

        // Normalize scores to the range [1, numParticipants + 1]
        for (Map.Entry<Player, Integer> entry : rawScores.entrySet()) {
            Player player = entry.getKey();
            int rawScore = entry.getValue();

            // Normalize the score
            int normalizedScore = 1 + (int) ((double) (rawScore - minScore) / (maxScore - minScore) * numParticipants);
            normalizedScores.put(player, normalizedScore);
        }

        return normalizedScores;
    }

    public List<Player> getSortedPlayers() {
        // Get normalized scores
        Map<Player, Integer> normalizedScores = getNormalizedScores();

        // Create a list of players and sort based on the normalized scores
        List<Player> sortedPlayers = new ArrayList<>(participants);

        // Sort players based on their normalized scores
        sortedPlayers.sort((p1, p2) -> Integer.compare(normalizedScores.get(p2), normalizedScores.get(p1)));

        return sortedPlayers;
    }

    public void updateCheckpointProgress(Player player, int checkpoint) {
        checkpointProgress.put(player, checkpoint);
        if (checkpoint >= track.getCheckpointRegionNames().size()) {
            updateLapProgress(player, getLapProgress(player)+1);
        }
    }

    public int getCheckpointProgress(Player player) {
        return checkpointProgress.getOrDefault(player, 0);
    }

    public void updateLapProgress(Player player, int lap) {
        lapProgress.put(player, lap);
    }

    public int getLapProgress(Player player) {
        return lapProgress.getOrDefault(player, 0);
    }
}
