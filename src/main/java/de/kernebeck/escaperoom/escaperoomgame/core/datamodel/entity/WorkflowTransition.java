package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity;

import javax.persistence.*;

@Entity
@Table(name = "workflowtransition")
public class WorkflowTransition extends BasicEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne(targetEntity = WorkflowPart.class, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id", nullable = false)
    private WorkflowPart source;

    @ManyToOne(targetEntity = WorkflowPart.class, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id", nullable = false)
    private WorkflowPart destination;

    public WorkflowTransition() {
        //empty Constructor for Hibernate object initialization
    }

    public WorkflowTransition(String name, String description, WorkflowPart source, WorkflowPart destination) {
        this.name = name;
        this.description = description;
        this.source = source;
        this.destination = destination;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public WorkflowPart getSource() {
        return source;
    }

    public WorkflowPart getDestination() {
        return destination;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSource(WorkflowPart source) {
        this.source = source;
    }

    public void setDestination(WorkflowPart destination) {
        this.destination = destination;
    }
}
