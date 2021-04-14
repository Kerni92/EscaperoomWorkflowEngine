package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.definition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkflowPartDTO {

    private String name;

    private String description;

    private String type;

    private String linkIdentifier;

    private List<SolutionDTO> solutions;

    private List<RiddleDTO> riddles;

    public WorkflowPartDTO() {
        //empty constructor for jackson
    }

    public WorkflowPartDTO(String name, String description, String type, String linkIdentifier, List<SolutionDTO> solutions, List<RiddleDTO> riddles) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.linkIdentifier = linkIdentifier;
        this.solutions = solutions;
        this.riddles = riddles;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getLinkIdentifier() {
        return linkIdentifier;
    }

    public List<RiddleDTO> getRiddles() {
        return riddles;
    }

    public List<SolutionDTO> getSolutions() {
        return solutions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLinkIdentifier(String linkIdentifier) {
        this.linkIdentifier = linkIdentifier;
    }

    public void setRiddles(List<RiddleDTO> riddles) {
        this.riddles = riddles;
    }

    public void setSolutions(List<SolutionDTO> solutions) {
        this.solutions = solutions;
    }
}
