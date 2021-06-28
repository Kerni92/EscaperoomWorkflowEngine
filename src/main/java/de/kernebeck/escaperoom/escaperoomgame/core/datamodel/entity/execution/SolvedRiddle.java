package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.BasicEntity;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Riddle;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.RiddleHint;

import javax.persistence.*;
import java.util.List;

@Entity
public class SolvedRiddle extends BasicEntity {

    @Column(name = "attempts")
    private Integer attempts;

    @Column(name = "resolved")
    private Boolean resolved;

    @ManyToOne(targetEntity = Riddle.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_riddle")
    private Riddle riddle;

    @ManyToMany
    @JoinTable(name = "solvedriddle_riddlehint",
            joinColumns = @JoinColumn(name = "fk_riddlehint", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "fk_solvedriddle", referencedColumnName = "id")
    )
    private List<RiddleHint> usedHints;

    @ManyToOne(targetEntity = ExecutedWorkflowPart.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_executedworkflowpart")
    private ExecutedWorkflowPart executedWorkflowPart;

    public SolvedRiddle() {
        //default constructor required for hibernate
        this.attempts = 0;
        this.resolved = Boolean.FALSE;
    }

    public SolvedRiddle(Riddle riddle, List<RiddleHint> usedHints, ExecutedWorkflowPart executedWorkflowPart, Integer attempts, Boolean resolved) {
        this.riddle = riddle;
        this.usedHints = usedHints;
        this.executedWorkflowPart = executedWorkflowPart;
        this.attempts = attempts != null ? attempts : 0;
        this.resolved = resolved != null ? resolved : false;
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

    public Integer getAttempts() {
        return attempts;
    }

    public Boolean getResolved() {
        return resolved;
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

    public void setAttempts(Integer attempts) {
        if (attempts == null || attempts < 0) {
            attempts = 0;
        }
        this.attempts = attempts;
    }

    public void setResolved(Boolean resolved) {
        this.resolved = resolved;
    }
}
