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

    @Column(name = "usernames")
    private String usernames;

    @ManyToOne(targetEntity = Workflow.class, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "fk_workflow", nullable = false)
    private Workflow workflow;

    @OneToMany
    @JoinColumn(name = "fk_game")
    private List<ExecutedWorkflowPart> executedWorkflowParts;

    public Game() {
        super();
        //empty constructor required for hibernate
    }

    public Game(String gameId, Timestamp starttime, Timestamp lastEndTime, Long totalTime, Workflow workflow) {
        this.gameId = gameId;
        this.starttime = starttime;
        this.endTime = lastEndTime;
        this.totalTime = totalTime;
        this.workflow = workflow;
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

    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
    }
}
