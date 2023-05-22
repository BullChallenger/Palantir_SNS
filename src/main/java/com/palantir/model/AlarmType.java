package com.palantir.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlarmType {
    NEW_COMMENT_ON_ARTICLE("New Comment on Article"),
    NEW_LIKE_ON_ARTICLE("New Like on Article"),
    ;

    private final String alarmContent;
}
