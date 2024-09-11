package com.rune.mtraces.managers;

import com.rune.mtraces.races.AbstractRace;
import com.rune.mtraces.races.LMSRace;
import com.rune.mtraces.races.DragRace;
import com.rune.mtraces.races.CircuitRace;
import com.rune.mtraces.tracks.Track;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RaceManager {
    private static RaceManager instance;
    private AbstractRace currentRace;
    private Player raceHost;
    private Set<String> validRaceTypes;

    private RaceManager() {
        validRaceTypes = new HashSet<>();
        validRaceTypes.add("LMS");
        validRaceTypes.add("Circuit");
        validRaceTypes.add("Drag");
    }


    public static RaceManager getInstance() {
        if (instance == null) {
            instance = new RaceManager();
        }
        return instance;
    }

    public boolean isValidRaceType(String type) {
        return validRaceTypes.contains(type);
    }

    public void createRace(String type, String trackName, int laps, Player host) {
        Track track = TrackManager.getInstance().getTrack(trackName);
        placeBarriers(track);
        switch (type.toLowerCase()) {
            case "lms":
                currentRace = new LMSRace(track);
                break;
            case "circuit":
                currentRace = new CircuitRace(track, laps);
                break;
            case "drag":
                currentRace = new DragRace(track);
                break;
            default:
                throw new IllegalArgumentException("Ongeldig racetype.");
        }
        currentRace.setHost(host);
        raceHost = host;
    }

    public boolean isHost(Player player) {
        return player.equals(raceHost);
    }

    public void startRace(Player player) {
        if (currentRace != null && isHost(player)) {
            currentRace.start();
            if(currentRace instanceof DragRace){
                currentRace.getTrack().getLightManager().startLights(true);
            }else{
                currentRace.getTrack().getLightManager().startLights();
            }
        }
    }

    public void stopRace(Player player) {
        if (currentRace != null && isHost(player)) {
            currentRace.stop();
        }
    }

    public void addParticipant(Player player) {
        if (currentRace != null) {
            currentRace.addParticipant(player);
        }
    }

    public void removeParticipant(Player player) {
        if (currentRace != null) {
            currentRace.removeParticipant(player);
        }
    }

    public void loadPlayers(Player player) {
        if (currentRace != null && isHost(player)) {
            for(Player target : Bukkit.getOnlinePlayers()){
                if(player != target){
                    if(target.getLocation().distance(player.getLocation()) <= 10){
                        addParticipant(target);
                    }
                }
            }
        }
    }

    public AbstractRace getCurrentRace() {
        return currentRace;
    }

    public void placeBarriers(Track track) {
        List<Location> barrierLocations = track.getBarrierLocations();
        for (Location location : barrierLocations) {
            Block block = location.getBlock();
            block.setType(Material.BARRIER);
        }
    }
}
