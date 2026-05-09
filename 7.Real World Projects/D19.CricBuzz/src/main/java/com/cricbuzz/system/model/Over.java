package com.cricbuzz.system.model;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class Over {
    private final int overNumber;
    private final List<Ball> balls = new ArrayList<>();

    public void addBall(Ball ball) {
        balls.add(ball);
    }
}
