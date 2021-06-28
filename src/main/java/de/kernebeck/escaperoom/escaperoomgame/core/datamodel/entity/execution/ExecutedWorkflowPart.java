package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.BasicEntity;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowPart;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
public class ExecutedWorkflowPart extends BasicEntity {

    @ManyToOne
    @JoinColumn(name = "fk_workflowpart")
    private WorkflowPart workflowPart;

    @Column(name = "endtime")
    private Timestamp endTime;

    @Column(name = "starttime")
    private Timestamp startTime;

    @Column(name = "totaltime")
    private Long totalTime;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_executedworkflowpart")
    private List<SolvedRiddle> solvedRiddleList;


    public ExecutedWorkflowPart() {
        //empty constructor required for hibernate
    }

    public ExecutedWorkflowPart(WorkflowPart workflowPart, Timestamp endTime, Timestamp startTime, Long totalTime, List<SolvedRiddle> solvedRiddleList) {
        this.workflowPart = workflowPart;
        this.endTime = endTime;
        this.startTime = startTime;
        this.totalTime = totalTime;
        this.solvedRiddleList = solvedRiddleList;
    }

    public WorkflowPart getWorkflowPart() {
        return workflowPart;
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

    public List<SolvedRiddle> getSolvedRiddleList() {
        return solvedRiddleList;
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

    public void setTotalTime(Long totalTime) {
        this.totalTime = totalTime;
    }

    public void setSolvedRiddleList(List<SolvedRiddle> solvedRiddleList) {
        this.solvedRiddleList = solvedRiddleList;
    }
}



