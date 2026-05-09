package com.cricbuzz.system;

import com.cricbuzz.system.model.Ball;
import com.cricbuzz.system.model.Innings;
import com.cricbuzz.system.model.Team;
import com.cricbuzz.system.model.enums.MatchStatus;
import com.cricbuzz.system.model.enums.RunType;
import com.cricbuzz.system.model.enums.WicketType;
import com.cricbuzz.system.observer.Observer;
import com.cricbuzz.system.strategy.MatchFormat;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

public class Match {
    private final String id;
    private final Team teamA;
    private final Team teamB;
    private final MatchFormat format;
    private final List<Observer> observers = new ArrayList<>();
    
    @Getter private MatchStatus status;
    @Getter private Innings currentInnings;

    public Match(String id, Team teamA, Team teamB, MatchFormat format) {
        this.id = id;
        this.teamA = teamA;
        this.teamB = teamB;
        this.format = format;
        this.status = MatchStatus.SCHEDULED;
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void startInnings(Team batting, Team bowling) {
        this.currentInnings = new Innings(batting, bowling);
        this.status = MatchStatus.IN_PROGRESS;
        System.out.println("\n--- Innings Started: " + batting.getName() + " is Batting ---");
    }

    public void recordBall(Ball ball) {
        if (status != MatchStatus.IN_PROGRESS) {
            System.out.println("Cannot record ball. Match is not in progress.");
            return;
        }

        // Update Innings Logic
        currentInnings.addRuns(ball.getRunType().getRuns());
        if (ball.isWicket()) {
            currentInnings.addWicket();
        }
        
        // Logical ball count (only for legitimate deliveries, simplifying here for now)
        if (ball.getRunType() != RunType.WIDE && ball.getRunType() != RunType.NO_BALL) {
            currentInnings.incrementBalls();
        }

        // Notify Observers
        notifyObservers(ball);
    }

    private void notifyObservers(Ball ball) {
        for (Observer observer : observers) {
            observer.update(ball);
        }
    }
}
