package de.kernebeck.escaperoom.escaperoomgame.webservice.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckRiddleResult implements Serializable {

    private Boolean success = false;

    public CheckRiddleResult() {
        //default constructor required for hibernate
    }

    public CheckRiddleResult(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
