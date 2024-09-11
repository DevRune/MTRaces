package com.rune.mtraces.races;

import org.bukkit.entity.Player;
import java.util.List;

public interface Race {
    void start();
    void stop();
    void addParticipant(Player player);
    void removeParticipant(Player player);
    void finishRaceForPlayer(Player player);
    List<Player> getParticipants();
    boolean isRaceOngoing();
}
