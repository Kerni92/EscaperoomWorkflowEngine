package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.BasicEntity;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.enumeration.WorkflowPartType;

import javax.persistence.*;
import java.util.ArrayList;
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

    @ManyToOne(targetEntity = Workflow.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_workflow", nullable = false)
    private Workflow workflow;

    @OneToMany(targetEntity = Riddle.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_workflowpart")
    private List<Riddle> riddles = new ArrayList<>();

    @OneToMany(targetEntity = WorkflowTransition.class, fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "fk_destinationpart")
    private Set<WorkflowTransition> ingoingTransitions = new LinkedHashSet<>();

    @OneToMany(targetEntity = WorkflowTransition.class, fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "fk_sourcepart")
    private Set<WorkflowTransition> outgoingTransitions = new LinkedHashSet<>();

    public WorkflowPart() {
        super();
        //empty constructor for hibernate
    }

    public WorkflowPart(String name, String description, WorkflowPartType partType, Workflow workflow, List<Riddle> riddles, Set<WorkflowTransition> ingoingTransitions, Set<WorkflowTransition> outgoingTransitions) {
        this.name = name;
        this.description = description;
        this.partType = partType;
        this.workflow = workflow;
        this.riddles = riddles;
        this.ingoingTransitions = ingoingTransitions;
        this.outgoingTransitions = outgoingTransitions;
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

    public List<Riddle> getRiddles() {
        return riddles;
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

    public void setRiddles(List<Riddle> riddles) {
        this.riddles = riddles;
    }
}
