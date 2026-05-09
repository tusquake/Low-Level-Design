package com.cricbuzz.system.model;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class Innings {
    private final Team battingTeam;
    private final Team bowlingTeam;
    private final List<Over> overs = new ArrayList<>();
    private int totalRuns = 0;
    private int totalWickets = 0;
    private int totalBalls = 0;

    public void addRuns(int runs) {
        this.totalRuns += runs;
    }

    public void addWicket() {
        this.totalWickets++;
    }

    public void incrementBalls() {
        this.totalBalls++;
    }
}
