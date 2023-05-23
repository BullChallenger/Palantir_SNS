package com.palantir.model.event;

import com.palantir.model.AlarmArgs;
import com.palantir.model.AlarmType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlarmEvent {
    private Long receiverId;
    private AlarmType alarmType;
    private AlarmArgs args;

    @Builder
    public AlarmEvent(Long receiverId, AlarmType alarmType, AlarmArgs args) {
        this.receiverId = receiverId;
        this.alarmType = alarmType;
        this.args = args;
    }
}
