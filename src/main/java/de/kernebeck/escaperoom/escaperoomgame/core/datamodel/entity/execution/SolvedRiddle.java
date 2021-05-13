package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.BasicEntity;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Riddle;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.RiddleHint;

import javax.persistence.*;
import java.util.List;

@Entity
public class SolvedRiddle extends BasicEntity {

    @OneToOne
    private Riddle riddle;

    @OneToMany
    @JoinColumn
    private List<RiddleHint> usedHints;

    @ManyToOne
    @JoinColumn(name = "fk_executedworkflowpart")
    private ExecutedWorkflowPart executedWorkflowPart;

    public SolvedRiddle() {
        //empty constructor required for hibernate
    }

    public SolvedRiddle(Riddle riddle, List<RiddleHint> usedHints, ExecutedWorkflowPart executedWorkflowPart) {
        this.riddle = riddle;
        this.usedHints = usedHints;
        this.executedWorkflowPart = executedWorkflowPart;
    }

    public Riddle getRiddle() {
        return riddle;
    }

    public List<RiddleHint> getUsedHints() {
        return usedHints;
    }

    public ExecutedWorkflowPart getExecutedWorkflowPart() {
        return executedWorkflowPart;
    }

    public void setRiddle(Riddle riddle) {
        this.riddle = riddle;
    }

    public void setUsedHints(List<RiddleHint> usedHints) {
        this.usedHints = usedHints;
    }

    public void setExecutedWorkflowPart(ExecutedWorkflowPart executedWorkflowPart) {
        this.executedWorkflowPart = executedWorkflowPart;
    }
}
