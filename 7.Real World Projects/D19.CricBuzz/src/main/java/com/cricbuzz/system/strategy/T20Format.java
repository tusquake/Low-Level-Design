package com.cricbuzz.system.strategy;

public class T20Format implements MatchFormat {
    @Override
    public int getMaxOvers() {
        return 20;
    }

    @Override
    public int getBallsPerOver() {
        return 6;
    }
}
