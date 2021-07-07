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

    @Column(name = "sortindex")
    private Integer sortIndex;

    @ManyToOne(targetEntity = WorkflowPart.class, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "fk_sourcepart")
    private WorkflowPart sourcePart;

    @ManyToOne(targetEntity = WorkflowPart.class, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "fk_destinationpart")
    private WorkflowPart destinationPart;

    public WorkflowTransition() {
        super();
        //empty Constructor for Hibernate object initialization
    }

    public WorkflowTransition(String name, String description, Integer sortIndex, WorkflowPart source, WorkflowPart destination) {
        this.name = name;
        this.description = description;
        this.sourcePart = source;
        this.destinationPart = destination;
        this.sortIndex = sortIndex;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public WorkflowPart getSourcePart() {
        return sourcePart;
    }

    public WorkflowPart getDestinationPart() {
        return destinationPart;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSourcePart(WorkflowPart source) {
        this.sourcePart = source;
    }

    public void setDestinationPart(WorkflowPart destination) {
        this.destinationPart = destination;
    }

    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }
}
