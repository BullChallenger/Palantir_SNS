package com.palantir.controller.response;

import com.palantir.model.Alarm;
import com.palantir.model.AlarmArgs;
import com.palantir.model.AlarmType;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
public class AlarmResponse {
    private Long alarmId;

    private AlarmType alarmType;

    private String alarmContent;

    private AlarmArgs args;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Timestamp deletedAt;

    public static AlarmResponse fromAlarm(Alarm alarm) {
        return AlarmResponse.builder()
                                .alarmId(alarm.getAlarmId())
                                .alarmType(alarm.getAlarmType())
                                .args(alarm.getArgs())
                                .alarmContent(alarm.getAlarmType().getAlarmContent())
                                .createdAt(alarm.getCreatedAt())
                                .updatedAt(alarm.getUpdatedAt())
                                .deletedAt(alarm.getDeletedAt())
                            .build();
    }
}
