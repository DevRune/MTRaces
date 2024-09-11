package com.rune.mtraces.races;

import com.rune.mtraces.tracks.Track;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class LMSRace extends AbstractRace {

    private final Map<Player, Integer> lapProgress = new HashMap<>();
    private final Map<Player, Long> fastestRoundTimes = new HashMap<>(); // Track finish times
    private int lastEliminationLab = 2; // first lap doesn't count.
    private int eliminatedPlayers = 0;

    public LMSRace(Track track) {
        this.track = track;
        this.type = "LMS";
        this.laps = laps; // Set laps based on track configuration or parameter
    }

    @Override
    public void stop() {
        Bukkit.broadcastMessage("§7------- §aPlacings §7-------");
        super.stop();
        participants.stream()
                .sorted((p1, p2) -> Integer.compare(getScoreForPlayer(p2), getScoreForPlayer(p1)))
                .forEach(player -> Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "" + getScoreForPlayer(player) + "." + ChatColor.GRAY + player.getName()));
    }

    @Override
    public void updateRaceStatus() {
        boolean allPlayersCompletedExtraLap = true;
        for (Player player : participants) {

            // Check if player should be eliminated based on progress
            if (isEliminated(player)) {
                continue;
            }

            // Check if all players have completed the extra lap
            if (getLapProgress(player) < lastEliminationLab) {
                allPlayersCompletedExtraLap = false;
            }
        }

        // Eliminate the player with the least progress if all players completed an extra lap
        if (allPlayersCompletedExtraLap) {
            Player playerToEliminate = participants.stream()
                    .min((p1, p2) -> Integer.compare(getLapProgress(p1), getLapProgress(p2)))
                    .orElse(null);

            if (playerToEliminate != null) {
                Bukkit.broadcastMessage(ChatColor.RED + playerToEliminate.getName() + " has been eliminated from the race!");
            }
            lastEliminationLab++;
        }

        if(eliminatedPlayers == participants.size()){
            stop();
        }

        updateScoreboard(); // Update the scoreboard to reflect the current state of the race
    }

    @Override
    public boolean isFinished(Player player) {
        return false;
    }

    @Override
    public boolean isEliminated(Player player) {
        return lapProgress.getOrDefault(player, 0) < lastEliminationLab;
    }

    @Override
    public boolean hasFastestTime(Player player) {
        if (fastestRoundTimes.isEmpty()) {
            return false;
        }
        long playerTime = fastestRoundTimes.getOrDefault(player, Long.MAX_VALUE);
        return fastestRoundTimes.values().stream().min(Long::compareTo).orElse(Long.MAX_VALUE).equals(playerTime);
    }

    @Override
    public int getScoreForPlayer(Player player) {
        return participants.size() - getLapProgress(player) + 1; // Score based on lap progress
    }

    public void updateLapProgress(Player player, int lap) {
        lapProgress.put(player, lap);
    }

    public int getLapProgress(Player player) {
        return lapProgress.getOrDefault(player, 0);
    }

    public Map<Player, Long> getFastestRoundTimes() {
        return fastestRoundTimes;
    }

    @Override
    public void finishRaceForPlayer(Player player) {
        return;
    }
}
