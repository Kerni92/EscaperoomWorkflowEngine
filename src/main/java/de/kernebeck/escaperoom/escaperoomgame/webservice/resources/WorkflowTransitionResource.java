package de.kernebeck.escaperoom.escaperoomgame.webservice.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkflowTransitionResource implements Serializable {

    private Long id;

    private String name;

    private String description;

    private Integer sortIndex;

    public WorkflowTransitionResource() {
        //default constructor required for jackson
    }

    public WorkflowTransitionResource(Long id, String name, String description, Integer sortIndex) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.sortIndex = sortIndex;
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

    public Integer getSortIndex() {
        return sortIndex;
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

    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }
}
