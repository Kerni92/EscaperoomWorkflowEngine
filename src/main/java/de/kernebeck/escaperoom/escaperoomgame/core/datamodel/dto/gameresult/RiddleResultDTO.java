package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.gameresult;

import java.io.Serializable;

public class RiddleResultDTO implements Serializable {

    private String name;

    private int sortIndex;

    private int attempts;


    public RiddleResultDTO(String name, int sortIndex, int attempts) {
        this.name = name;
        this.sortIndex = sortIndex;
        this.attempts = attempts;
    }

    public String getName() {
        return name;
    }

    public int getSortIndex() {
        return sortIndex;
    }

    public int getAttempts() {
        return attempts;
    }
}
