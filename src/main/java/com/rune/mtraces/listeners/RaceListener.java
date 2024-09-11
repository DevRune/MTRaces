package com.rune.mtraces.listeners;

import com.rune.mtraces.managers.RaceManager;
import com.rune.mtraces.managers.ScoreboardManager;
import com.rune.mtraces.races.*;
import com.rune.mtraces.tracks.Track;
import com.rune.mtraces.utils.WorldGuardUtils;
import nl.mtvehicles.core.events.VehicleRegionEnterEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class RaceListener implements Listener {
    private final Map<Player, Integer> playerCheckpointProgress = new HashMap<>();

    @EventHandler
    public void onPlayerEnterRegion(VehicleRegionEnterEvent event) {
        Player player = event.getPlayer();
        String enteredRegion = event.getRegionName(); // Verkrijg de naam van de regio

        if (RaceManager.getInstance().getCurrentRace() != null && RaceManager.getInstance().getCurrentRace().isRaceOngoing() && RaceManager.getInstance().getCurrentRace().getTrack() != null) {
            int currentCheckpointIndex = playerCheckpointProgress.getOrDefault(player, 0);
            Track track = RaceManager.getInstance().getCurrentRace().getTrack();

            String expectedCheckpoint = track.getCheckpointRegionNames().get(currentCheckpointIndex);

            if (enteredRegion.equals(expectedCheckpoint)) {
                handleCheckpoint(player, currentCheckpointIndex + 1);
            }
        }
    }

    private void handleCheckpoint(Player player, int nextCheckpointIndex) {
        playerCheckpointProgress.put(player, nextCheckpointIndex);

        // Verkrijg de huidige race
        AbstractRace currentRace = RaceManager.getInstance().getCurrentRace();
        if (currentRace != null) {
            if (currentRace instanceof DragRace) {
                ((DragRace) currentRace).updateCheckpointProgress(player, nextCheckpointIndex);
            } else if (currentRace instanceof LMSRace) {
                ((LMSRace) currentRace).updateLapProgress(player, nextCheckpointIndex);
            } else if (currentRace instanceof CircuitRace) {
                ((CircuitRace) currentRace).updateCheckpointProgress(player, nextCheckpointIndex);
            }
            currentRace.updateRaceStatus();
            ScoreboardManager.getInstance().updateScoreboard(currentRace);
        }
        if (nextCheckpointIndex >= RaceManager.getInstance().getCurrentRace().getTrack().getCheckpointRegionNames().size()) {
            playerCheckpointProgress.put(player, 0);
            long lapTime = System.currentTimeMillis() - currentRace.getLapStartTime().get(player);
            if(lapTime < currentRace.getFastestRoundTime().getOrDefault(player, Long.MAX_VALUE)){
                currentRace.getFastestRoundTime().put(player, lapTime);
            }
            currentRace.getLapStartTime().put(player, System.currentTimeMillis());
        }
    }
}
