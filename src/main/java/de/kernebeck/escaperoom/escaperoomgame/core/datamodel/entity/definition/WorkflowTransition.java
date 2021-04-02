package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.BasicEntity;

import javax.persistence.*;

@Entity
@Table(name = "workflowtransition")
public class WorkflowTransition extends BasicEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne(targetEntity = WorkflowPart.class, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id", table = "workflowpart", nullable = false)
    private WorkflowPart sourcePart;

    @ManyToOne(targetEntity = WorkflowPart.class, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id", table = "workflowpart", nullable = false)
    private WorkflowPart destinationPart;

    public WorkflowTransition() {
        //empty Constructor for Hibernate object initialization
    }

    public WorkflowTransition(String name, String description, WorkflowPart source, WorkflowPart destination) {
        this.name = name;
        this.description = description;
        this.sourcePart = source;
        this.destinationPart = destination;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public WorkflowPart getSource() {
        return sourcePart;
    }

    public WorkflowPart getDestination() {
        return destinationPart;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSource(WorkflowPart source) {
        this.sourcePart = source;
    }

    public void setDestination(WorkflowPart destination) {
        this.destinationPart = destination;
    }
}
