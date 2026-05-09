package com.cricbuzz.system.model;

import lombok.Data;
import java.util.List;

@Data
public class Team {
    private final String name;
    private final List<Player> players;
}
