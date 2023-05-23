package com.palantir.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class EmitterRepository {

    private Map<String, SseEmitter> emitterMap = new HashMap<>();

    public SseEmitter save(Long id, SseEmitter sseEmitter) {
        final String key = getKey(id);
        emitterMap.put(key, sseEmitter);
        log.info("Set sseEmitter {}", id);
        return sseEmitter;
    }

    public Optional<SseEmitter> get(Long id) {
        final String key = getKey(id);
        log.info("Set sseEmitter {}", id);
        return Optional.ofNullable(emitterMap.get(key));
    }

    public void delete(Long id) {
        emitterMap.remove(getKey(id));
    }

    private String getKey(Long id) {
        return "Emitter:UID:" + id;
    }
}
