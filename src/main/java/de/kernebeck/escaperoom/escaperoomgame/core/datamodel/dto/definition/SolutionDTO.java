package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.definition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SolutionDTO {

    private String name;

    private String description;

    private String type;

    private String solution;

    private List<String> solutionOptions;


    public SolutionDTO() {
        //empty constructor for jackson
    }

    public SolutionDTO(String name, String description, String type, String solution, List<String> solutionOptions) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.solution = solution;
        this.solutionOptions = solutionOptions;
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

    public String getSolution() {
        return solution;
    }

    public List<String> getSolutionOptions() {
        return solutionOptions;
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

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public void setSolutionOptions(List<String> solutionOptions) {
        this.solutionOptions = solutionOptions;
    }
}
