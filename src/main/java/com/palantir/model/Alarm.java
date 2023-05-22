package com.palantir.model;

import com.palantir.model.entity.AlarmEntity;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
public class Alarm {
    private Long alarmId;

    private Account receiver;

    private AlarmType alarmType;

    private AlarmArgs args;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Timestamp deletedAt;

    public static Alarm fromEntity(AlarmEntity entity) {
        return Alarm.builder()
                        .alarmId(entity.getId())
                        .receiver(Account.fromEntity(entity.getReceiver()))
                        .alarmType(entity.getAlarmType())
                        .args(entity.getArgs())
                        .createdAt(entity.getCreatedAt())
                        .updatedAt(entity.getUpdatedAt())
                        .deletedAt(entity.getDeletedAt())
                    .build();
    }
}
