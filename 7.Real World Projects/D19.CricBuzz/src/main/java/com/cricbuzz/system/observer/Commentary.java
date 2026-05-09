package com.cricbuzz.system.observer;

import com.cricbuzz.system.model.Ball;

public class Commentary implements Observer {
    @Override
    public void update(Ball ball) {
        String message = "Ball " + ball.getBallNumber() + ": " + ball.getBowler().getName() + " to " + ball.getStriker().getName() + ", ";
        
        if (ball.isWicket()) {
            message += "OUT! " + ball.getWicketType() + "!!";
        } else {
            message += ball.getRunType().getRuns() + " runs.";
        }
        
        System.out.println("[COMMENTARY] " + message);
    }
}
