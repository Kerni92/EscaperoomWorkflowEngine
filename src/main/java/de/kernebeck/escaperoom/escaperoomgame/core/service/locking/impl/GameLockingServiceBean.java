package de.kernebeck.escaperoom.escaperoomgame.core.service.locking.impl;

import de.kernebeck.escaperoom.escaperoomgame.core.service.locking.GameLockingService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class GameLockingServiceBean implements GameLockingService {

    private final Map<Long, ReentrantLock> lockingMap = new ConcurrentHashMap<>();

    private final ReentrantLock lock = new ReentrantLock();

    @Override
    public void lockGame(Long gameId) {
        ReentrantLock gameLock = null;
        try {
            lock.lock();
            if (!lockingMap.containsKey(gameId)) {
                lockingMap.put(gameId, new ReentrantLock());
            }
            gameLock = lockingMap.get(gameId);
        }
        finally {
            lock.unlock();
        }
        gameLock.lock();
    }

    @Override
    public void unlockGame(Long gameId) {
        if (lockingMap.containsKey(gameId)) {
            try {
                lockingMap.get(gameId).unlock();
            }
            catch (IllegalMonitorStateException e) {
                //ignore
            }
        }
    }
}
