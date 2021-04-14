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

    @Column(name = "laststarttime")
    private Timestamp lastStartTime;

    @Column(name = "lastendtime")
    private Timestamp lastEndTime;

    @Column(name = "totaltime")
    private Long totalTime;

    @Column(name = "username")
    private String usernames;

    @ManyToOne(targetEntity = Workflow.class, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "fk_workflow", nullable = false)
    private Workflow workflow;

    public Game() {
        super();
        //empty constructor required for hibernatea
    }

    public Game(String gameId, Timestamp lastStartTime, Timestamp lastEndTime, Long totalTime, Workflow workflow) {
        this.gameId = gameId;
        this.lastStartTime = lastStartTime;
        this.lastEndTime = lastEndTime;
        this.totalTime = totalTime;
        this.workflow = workflow;
    }

    public String getGameId() {
        return gameId;
    }

    public Timestamp getLastStartTime() {
        return lastStartTime;
    }

    public Timestamp getLastEndTime() {
        return lastEndTime;
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

    public void setLastStartTime(Timestamp lastStartTime) {
        this.lastStartTime = lastStartTime;
    }

    public void setLastEndTime(Timestamp lastEndTime) {
        this.lastEndTime = lastEndTime;
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
