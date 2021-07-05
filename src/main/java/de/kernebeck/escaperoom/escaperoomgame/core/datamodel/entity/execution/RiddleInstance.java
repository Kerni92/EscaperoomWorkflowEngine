package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.BasicEntity;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Riddle;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.RiddleHint;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "riddleinstance")
public class RiddleInstance extends BasicEntity {

    @Column(name = "attempts")
    private Integer attempts;

    @Column(name = "resolved")
    private Boolean resolved;

    @ManyToOne(targetEntity = Riddle.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_riddle")
    private Riddle riddle;

    @ManyToMany
    @JoinTable(name = "riddleinstance_riddlehint",
            joinColumns = @JoinColumn(name = "fk_riddlehint", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "fk_riddleinstance", referencedColumnName = "id")
    )
    private List<RiddleHint> usedHints;

    @ManyToOne(targetEntity = WorkflowPartInstance.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_workflowpartinstance")
    private WorkflowPartInstance workflowPartInstance;

    public RiddleInstance() {
        //default constructor required for hibernate
        this.attempts = 0;
        this.resolved = Boolean.FALSE;
    }

    public RiddleInstance(Riddle riddle, List<RiddleHint> usedHints, WorkflowPartInstance workflowPartInstance, Integer attempts, Boolean resolved) {
        this.riddle = riddle;
        this.usedHints = usedHints;
        this.workflowPartInstance = workflowPartInstance;
        this.attempts = attempts != null ? attempts : 0;
        this.resolved = resolved != null ? resolved : false;
    }

    public Riddle getRiddle() {
        return riddle;
    }

    public List<RiddleHint> getUsedHints() {
        return usedHints;
    }

    public WorkflowPartInstance getWorkflowPartInstance() {
        return workflowPartInstance;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public Boolean isResolved() {
        return resolved;
    }

    public void setRiddle(Riddle riddle) {
        this.riddle = riddle;
    }

    public void setUsedHints(List<RiddleHint> usedHints) {
        this.usedHints = usedHints;
    }

    public void addUsedHint(RiddleHint hint) {
        if (this.usedHints == null) {
            this.usedHints = new ArrayList<>();
        }
        this.usedHints.add(hint);

    }

    public void setWorkflowPartInstance(WorkflowPartInstance workflowPartInstance) {
        this.workflowPartInstance = workflowPartInstance;
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
