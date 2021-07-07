package de.kernebeck.escaperoom.escaperoomgame.webservice.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RiddleInstanceResource implements Serializable {

    private Long riddleId;

    private String name;

    private String description;

    private Boolean resolved;

    private List<RiddleHintResource> usedHints;

    public RiddleInstanceResource() {
        //default constructor required for jackson
    }

    public RiddleInstanceResource(Long riddleId, String name, String description, Boolean resolved, List<RiddleHintResource> usedHints) {
        this.riddleId = riddleId;
        this.name = name;
        this.description = description;
        this.resolved = resolved;
        this.usedHints = usedHints;
    }

    public Long getRiddleId() {
        return riddleId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getResolved() {
        return resolved;
    }

    public List<RiddleHintResource> getUsedHints() {
        return usedHints;
    }

    public void setRiddleId(Long id) {
        this.riddleId = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setResolved(Boolean resolved) {
        this.resolved = resolved;
    }

    public void setUsedHints(List<RiddleHintResource> usedHints) {
        this.usedHints = usedHints;
    }
}
