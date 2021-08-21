package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.gameresult;

import java.io.Serializable;

public class RiddleResultDTO implements Serializable {

    private String name;

    private int sortIndex;

    private int attempts;

    private int usedHints;


    public RiddleResultDTO(String name, int sortIndex, int attempts, int usedHints) {
        this.name = name;
        this.sortIndex = sortIndex;
        this.attempts = attempts;
        this.usedHints = usedHints;
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

    public int getUsedHints() {
        return usedHints;
    }
}
