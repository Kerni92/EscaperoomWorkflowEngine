package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.definition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkflowTransitionDTO {

    private String name;

    private String description;

    private String linkIdentifier;

    private String linkIdentifierSourceWorkflowPart;

    private String linkIdentifierTargetWorkflowPart;

    public WorkflowTransitionDTO() {
        //empty constructor for hibernate
    }

    public WorkflowTransitionDTO(String name, String description, String linkIdentifier, String linkIdentifierSourceWorkflowPart, String linkIdentifierTargetWorkflowPart) {
        this.name = name;
        this.description = description;
        this.linkIdentifier = linkIdentifier;
        this.linkIdentifierSourceWorkflowPart = linkIdentifierSourceWorkflowPart;
        this.linkIdentifierTargetWorkflowPart = linkIdentifierTargetWorkflowPart;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLinkIdentifier() {
        return linkIdentifier;
    }

    public String getLinkIdentifierSourceWorkflowPart() {
        return linkIdentifierSourceWorkflowPart;
    }

    public String getLinkIdentifierTargetWorkflowPart() {
        return linkIdentifierTargetWorkflowPart;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLinkIdentifier(String linkIdentifier) {
        this.linkIdentifier = linkIdentifier;
    }

    public void setLinkIdentifierSourceWorkflowPart(String linkIdentifierSourceWorkflowPart) {
        this.linkIdentifierSourceWorkflowPart = linkIdentifierSourceWorkflowPart;
    }

    public void setLinkIdentifierTargetWorkflowPart(String linkIdentifierTargetWorkflowPart) {
        this.linkIdentifierTargetWorkflowPart = linkIdentifierTargetWorkflowPart;
    }
}
