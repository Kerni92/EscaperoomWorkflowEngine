package de.kernebeck.escaperoom.escaperoomgame.webservice.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckRiddleSolutionResource {

    private String solution;

    public CheckRiddleSolutionResource() {
        //default constructor required for Jackson
    }

    public CheckRiddleSolutionResource(String solution) {
        this.solution = solution;
    }

    public String getSolution() {
        return solution;
    }


    public void setSolution(String solution) {
        this.solution = solution;
    }
}
