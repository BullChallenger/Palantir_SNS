package com.palantir.model;

import lombok.Builder;
import lombok.Getter;
<<<<<<< HEAD
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
=======

@Getter
>>>>>>> main
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
