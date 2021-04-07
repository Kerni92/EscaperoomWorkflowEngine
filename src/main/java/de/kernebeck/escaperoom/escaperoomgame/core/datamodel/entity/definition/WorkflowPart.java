package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.BasicEntity;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.enumeration.WorkflowPartType;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "workflowpart")
public class WorkflowPart extends BasicEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "parttype")
    @Enumerated(value = EnumType.STRING)
    private WorkflowPartType partType;

    @ManyToOne(targetEntity = Workflow.class, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "fk_workflow", nullable = false)
    private Workflow workflow;

    @OneToMany(targetEntity = WorkflowTransition.class, fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "fk_target")
    private Set<WorkflowTransition> ingoingTransitions = new LinkedHashSet<>();

    @OneToMany(targetEntity = WorkflowTransition.class, fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "fk_source")
    private Set<WorkflowTransition> outgoingTransitions = new LinkedHashSet<>();

    public WorkflowPart() {
        super();
        //empty constructor for hibernate
    }

    public WorkflowPart(String name, String description, WorkflowPartType partType, Workflow workflow, List<WorkflowTransition> ingoingTransitions, List<WorkflowTransition> outgoingTransitions) {
        this.name = name;
        this.description = description;
        this.partType = partType;
        this.workflow = workflow;
        this.ingoingTransitions.addAll(ingoingTransitions);
        this.outgoingTransitions.addAll(outgoingTransitions);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public WorkflowPartType getPartType() {
        return partType;
    }

    public Workflow getWorkflow() {
        return workflow;
    }

    public Set<WorkflowTransition> getIngoingTransitions() {
        return ingoingTransitions;
    }

    public Set<WorkflowTransition> getOutgoingTransitions() {
        return outgoingTransitions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPartType(WorkflowPartType partType) {
        this.partType = partType;
    }

    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
    }

    public void setIngoingTransitions(Set<WorkflowTransition> ingoingTransitions) {
        this.ingoingTransitions = ingoingTransitions;
    }

    public void setOutgoingTransitions(Set<WorkflowTransition> outgoingTransitions) {
        this.outgoingTransitions = outgoingTransitions;
    }
}
