package de.kernebeck.escaperoom.escaperoomgame.core.service.locking;

public interface GameLockingService {

    void lockGame(Long gameId);

    void unlockGame(Long gameId);

}
