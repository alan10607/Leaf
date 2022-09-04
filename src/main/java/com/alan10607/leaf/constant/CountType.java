package com.alan10607.leaf.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CountType {
    GOOD(1, "good"),
    BAD(2, "bad");
    private int voteFor;
    private String field;
}