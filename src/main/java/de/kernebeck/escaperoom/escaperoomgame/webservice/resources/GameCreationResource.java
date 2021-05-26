package de.kernebeck.escaperoom.escaperoomgame.webservice.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GameCreationResource implements Serializable {

    public Long techicalGameId;

    private List<String> usernames;


    public GameCreationResource() {
        //empty constructor required for jackson
    }

    public GameCreationResource(Long techicalGameId, List<String> usernames) {
        this.techicalGameId = techicalGameId;
        this.usernames = usernames;
    }

    public Long getTechicalGameId() {
        return techicalGameId;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public void setTechicalGameId(Long techicalGameId) {
        this.techicalGameId = techicalGameId;
    }

    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
    }
}
