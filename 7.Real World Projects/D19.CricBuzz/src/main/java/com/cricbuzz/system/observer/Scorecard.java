package com.cricbuzz.system.observer;

import com.cricbuzz.system.model.Ball;

public class Scorecard implements Observer {
    @Override
    public void update(Ball ball) {
        System.out.println("\n[SCORECARD UPDATE]");
        System.out.println("Striker: " + ball.getStriker().getName());
        System.out.println("Bowler: " + ball.getBowler().getName());
        System.out.println("Result: " + ball.getRunType() + (ball.isWicket() ? " | WICKET!" : ""));
        System.out.println("-----------------------------------");
    }
}
