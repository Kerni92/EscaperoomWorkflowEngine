package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.definition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkflowDTO {

    private String name;

    private String startPartLinkId;

    private List<WorkflowPartDTO> workflowParts;

    private List<WorkflowTransitionDTO> workflowTransitionDTOS;

    public WorkflowDTO() {
        //empty constructor for hibernate
    }

    public WorkflowDTO(String name, String startPartId, List<WorkflowPartDTO> workflowParts, List<WorkflowTransitionDTO> workflowTransitionDTOS) {
        this.name = name;
        this.startPartLinkId = startPartId;
        this.workflowParts = workflowParts;
        this.workflowTransitionDTOS = workflowTransitionDTOS;
    }

    public String getName() {
        return name;
    }

    public String getStartPartLinkId() {
        return startPartLinkId;
    }

    public List<WorkflowPartDTO> getWorkflowParts() {
        return workflowParts;
    }

    public List<WorkflowTransitionDTO> getWorkflowTransitions() {
        return workflowTransitionDTOS;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartPartLinkId(String startPartLinkId) {
        this.startPartLinkId = startPartLinkId;
    }

    public void setWorkflowParts(List<WorkflowPartDTO> workflowParts) {
        this.workflowParts = workflowParts;
    }

    public void setWorkflowTransitions(List<WorkflowTransitionDTO> workflowTransitionDTOS) {
        this.workflowTransitionDTOS = workflowTransitionDTOS;
    }
}
