package com.cricbuzz.system;

import com.cricbuzz.system.model.Ball;
import com.cricbuzz.system.model.Player;
import com.cricbuzz.system.model.Team;
import com.cricbuzz.system.model.enums.RunType;
import com.cricbuzz.system.model.enums.WicketType;
import com.cricbuzz.system.observer.Commentary;
import com.cricbuzz.system.observer.Scorecard;
import com.cricbuzz.system.strategy.T20Format;
import java.util.Arrays;

public class CricBuzzDemo {
    public static void main(String[] args) {
        // 1. Setup Players and Teams
        Player p1 = new Player("1", "Virat Kohli");
        Player p2 = new Player("2", "Rohit Sharma");
        Player b1 = new Player("3", "Jasprit Bumrah");

        Team india = new Team("India", Arrays.asList(p1, p2, b1));
        Team australia = new Team("Australia", Arrays.asList(new Player("4", "Pat Cummins")));

        // 2. Initialize Match
        Match match = new Match("M1", india, australia, new T20Format());

        // 3. Attach Observers
        match.addObserver(new Scorecard());
        match.addObserver(new Commentary());

        // 4. Simulate Innings
        match.startInnings(india, australia);

        // Ball 1: 4 runs
        Ball ball1 = Ball.builder()
                .ballNumber(1)
                .striker(p1)
                .nonStriker(p2)
                .bowler(b1)
                .runType(RunType.FOUR)
                .isWicket(false)
                .build();
        match.recordBall(ball1);

        // Ball 2: 1 run
        Ball ball2 = Ball.builder()
                .ballNumber(2)
                .striker(p1)
                .nonStriker(p2)
                .bowler(b1)
                .runType(RunType.ONE)
                .isWicket(false)
                .build();
        match.recordBall(ball2);

        // Ball 3: Wicket!
        Ball ball3 = Ball.builder()
                .ballNumber(3)
                .striker(p2)
                .nonStriker(p1)
                .bowler(b1)
                .runType(RunType.ZERO)
                .isWicket(true)
                .wicketType(WicketType.BOWLED)
                .build();
        match.recordBall(ball3);

        System.out.println("\nMatch Simulation Completed.");
    }
}
