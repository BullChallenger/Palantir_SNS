package com.palantir.service;

import com.palantir.exception.ErrorCode;
import com.palantir.exception.PalantirException;
import com.palantir.model.AlarmArgs;
import com.palantir.model.AlarmType;
import com.palantir.model.entity.AccountEntity;
import com.palantir.model.entity.AlarmEntity;
import com.palantir.repository.AccountEntityRepository;
import com.palantir.repository.AlarmEntityRepository;
import com.palantir.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {

    private final EmitterRepository emitterRepository;
    private final AlarmEntityRepository alarmEntityRepository;
    private final AccountEntityRepository accountEntityRepository;

    private final static Long DEFAULT_TIME_OUT = 60L * 1000 * 60;
    private final static String ALARM_NAME = "alarm";

    public void send(AlarmType alarmType, AlarmArgs args, Long receiverId) {
        AccountEntity theAccount =
            accountEntityRepository.findById(receiverId).orElseThrow(() -> new PalantirException(ErrorCode.ACCOUNT_NOT_FOUND));
        AlarmEntity theAlarm = alarmEntityRepository.save(AlarmEntity.of(theAccount, alarmType, args));

        emitterRepository.get(receiverId).ifPresentOrElse(sseEmitter -> {
            try {
                sseEmitter.send(SseEmitter.event().id(theAlarm.getId().toString()).name(ALARM_NAME).data("New Alarm"));
            } catch (IOException e) {
                emitterRepository.delete(receiverId);
                throw new PalantirException(ErrorCode.ALARM_CONNECT_ERROR);
            }
        }, () -> log.info("No Emitter founded."));
    }

    public SseEmitter connectAlarm(Long id) {
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIME_OUT);
        emitterRepository.save(id, sseEmitter);
        sseEmitter.onCompletion(() -> emitterRepository.delete(id));
        sseEmitter.onTimeout(() -> emitterRepository.delete(id));

        try {
            sseEmitter.send(SseEmitter.event().id("").name(ALARM_NAME).data("connect completed"));
        } catch (IOException e) {
            throw new PalantirException(ErrorCode.ALARM_CONNECT_ERROR);
        }

        return sseEmitter;
    }
}
