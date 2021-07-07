package de.kernebeck.escaperoom.escaperoomgame.webservice.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkflowPartResource implements Serializable {

    private Long id;

    private String name;

    private String description;

    private List<RiddleInstanceResource> riddles;

    public WorkflowPartResource() {
        //default constructor required for jackson
    }

    public WorkflowPartResource(Long id, String name, String description, List<RiddleInstanceResource> riddles) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.riddles = riddles;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<RiddleInstanceResource> getRiddles() {
        return riddles;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRiddles(List<RiddleInstanceResource> riddles) {
        this.riddles = riddles;
    }
}
