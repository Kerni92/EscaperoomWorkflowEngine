package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.BasicEntity;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Workflow;

import javax.persistence.*;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "game")
public class Game extends BasicEntity {

    private static final ObjectMapper OBJECTMAPPER = new ObjectMapper();

    @Column(name = "gameid")
    private String gameId;

    @Column(name = "starttime")
    private Timestamp starttime;

    @Column(name = "endtime")
    private Timestamp endTime;

    @Column(name = "totaltime")
    private Long totalTime;

    @Column(name = "finished")
    private Boolean finished;

    @Column(name = "usernames")
    private String usernames;

    @ManyToOne(targetEntity = Workflow.class, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "fk_workflow", nullable = false)
    private Workflow workflow;

    @OneToMany
    @JoinColumn(name = "fk_game")
    private List<WorkflowPartInstance> workflowPartInstances;

    @OneToOne(targetEntity = WorkflowPartInstance.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_workflowpartinstance")
    private WorkflowPartInstance currentWorkflowpart;

    public Game() {
        super();
        //empty constructor required for hibernate
    }

    public Game(String gameId, Timestamp starttime, Timestamp lastEndTime, Boolean finished, Long totalTime, Workflow workflow) {
        super();
        this.gameId = gameId;
        this.starttime = starttime;
        this.endTime = lastEndTime;
        this.totalTime = totalTime;
        this.workflow = workflow;
        this.finished = finished;
    }

    public String getGameId() {
        return gameId;
    }

    public Timestamp getStarttime() {
        return starttime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public Long getTotalTime() {
        return totalTime;
    }

    public Boolean isFinished() {
        return finished;
    }

    public List<String> getUsernames() {
        if (this.usernames != null && !this.usernames.isEmpty()) {
            try {
                return OBJECTMAPPER.readValue(new StringReader(this.usernames), new TypeReference<List<String>>() {
                });
            }
            catch (IOException e) {
                //ignore
            }
        }
        return Collections.emptyList();
    }

    public Workflow getWorkflow() {
        return workflow;
    }

    public List<WorkflowPartInstance> getExecutedWorkflowParts() {
        return workflowPartInstances;
    }

    public WorkflowPartInstance getActiveWorkflowPartInstance() {
        return currentWorkflowpart;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public void setStarttime(Timestamp lastStartTime) {
        this.starttime = lastStartTime;
    }

    public void setEndTime(Timestamp lastEndTime) {
        this.endTime = lastEndTime;
    }

    public void setTotalTime(Long totalTime) {
        this.totalTime = totalTime;
    }

    public void setUsernames(List<String> usernames) {
        if (usernames != null) {
            try {
                this.usernames = OBJECTMAPPER.writeValueAsString(usernames);
            }
            catch (IOException e) {
                //ignore -> should not happen
            }
        }
        else {
            this.usernames = null;
        }
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public void setCurrentWorkflowpart(WorkflowPartInstance currentWorkflowpart) {
        this.currentWorkflowpart = currentWorkflowpart;
    }

    public void setExecutedWorkflowParts(List<WorkflowPartInstance> workflowPartInstances) {
        this.workflowPartInstances = workflowPartInstances;
    }

    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
    }
}
