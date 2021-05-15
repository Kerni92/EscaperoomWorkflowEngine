package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.imports;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WorkflowImportResult implements Serializable {

    private boolean success;

    private List<String> errorMessages = new ArrayList<>();

    public WorkflowImportResult() {
        //empty constructor required for jackson deserialzation
    }

    public WorkflowImportResult(boolean success) {
        this.success = success;
    }

    public WorkflowImportResult(boolean success, List<String> errorMessages) {
        this.success = success;
        if (errorMessages != null && !errorMessages.isEmpty()) {
            this.errorMessages.addAll(errorMessages);
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }
}
