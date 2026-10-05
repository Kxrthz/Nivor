package com.nivor.ai;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

/** Per-process guard against accidental retry loops; can be replaced with a shared store for multi-instance deployments. */
@Component
public class AiRateLimiter {
    private static final int LIMIT = 30;
    private final ConcurrentHashMap<String, ArrayDeque<Instant>> requests = new ConcurrentHashMap<>();
    public void check(String userKey) {
        ArrayDeque<Instant> queue = requests.computeIfAbsent(userKey, ignored -> new ArrayDeque<>());
        synchronized (queue) {
            Instant cutoff = Instant.now().minusSeconds(60);
            while (!queue.isEmpty() && queue.peekFirst().isBefore(cutoff)) queue.removeFirst();
            if (queue.size() >= LIMIT) throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "AI request limit reached. Please wait a minute.");
            queue.addLast(Instant.now());
        }
    }
}
