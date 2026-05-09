package com.cricbuzz.system.model.enums;

public enum RunType {
    ZERO(0),
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    SIX(6),
    WIDE(1),
    NO_BALL(1);

    private final int runs;

    RunType(int runs) {
        this.runs = runs;
    }

    public int getRuns() {
        return runs;
    }
}
