package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.BasicEntity;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowPart;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workflowpartinstance")
public class WorkflowPartInstance extends BasicEntity {

    @ManyToOne
    @JoinColumn(name = "fk_workflowpart")
    private WorkflowPart workflowPart;

    @Column(name = "endtime")
    private Timestamp endTime;

    @Column(name = "starttime")
    private Timestamp startTime;

    @Column(name = "laststarttime")
    private Timestamp lastStartTime;

    @Column(name = "totaltime")
    private Long totalTime;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_workflowpartinstance")
    private List<RiddleInstance> riddleInstanceList;

    @ManyToOne
    @JoinColumn(name = "fk_game")
    private Game game;


    public WorkflowPartInstance() {
        //default constructor required for hibernate
        this.riddleInstanceList = new ArrayList<>();
    }

    public WorkflowPartInstance(WorkflowPart workflowPart, Timestamp endTime, Timestamp startTime, Timestamp lastStartTime, Long totalTime, Game game) {
        this.workflowPart = workflowPart;
        this.endTime = endTime;
        this.startTime = startTime;
        this.totalTime = totalTime;
        this.riddleInstanceList = new ArrayList<>();
        this.game = game;
        this.lastStartTime = lastStartTime;
    }

    public WorkflowPart getWorkflowPart() {
        return workflowPart;
    }

    public Timestamp getLastStartTime() {
        return lastStartTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public Long getTotalTime() {
        return totalTime;
    }

    public List<RiddleInstance> getRiddleInstanceList() {
        return riddleInstanceList;
    }

    public Game getGame() {
        return game;
    }

    public void setWorkflowPart(WorkflowPart workflowPart) {
        this.workflowPart = workflowPart;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public void setLastStartTime(Timestamp lastStartTime) {
        this.lastStartTime = lastStartTime;
    }

    public void setTotalTime(Long totalTime) {
        this.totalTime = totalTime;
    }

    public void setRiddleInstanceList(List<RiddleInstance> riddleInstanceList) {
        this.riddleInstanceList = riddleInstanceList;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}



