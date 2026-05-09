package com.cricbuzz.system.model;

import com.cricbuzz.system.model.enums.RunType;
import com.cricbuzz.system.model.enums.WicketType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Ball {
    private int ballNumber;
    private Player striker;
    private Player nonStriker;
    private Player bowler;
    private RunType runType;
    private WicketType wicketType;
    private boolean isWicket;
}
