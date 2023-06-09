package com.palantir.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlarmArgs {

    // Cause of Alarm
    private Long fromAccountId;

    // Receiver
    private Long targetId;

    @Builder
    public AlarmArgs(Long fromAccountId, Long targetId) {
        this.fromAccountId = fromAccountId;
        this.targetId = targetId;
    }
}
