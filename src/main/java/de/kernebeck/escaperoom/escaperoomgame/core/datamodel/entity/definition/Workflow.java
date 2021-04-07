package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.BasicEntity;

import javax.persistence.*;

@Entity
@Table(name = "workflow")
public class Workflow extends BasicEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne(targetEntity = WorkflowPart.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_workflowstartpart")
    private WorkflowPart workflowStart;

    public Workflow() {
        super();
        //empty constructor for hibernate
    }

    public Workflow(String name, WorkflowPart workflowStart) {
        this.name = name;
        this.workflowStart = workflowStart;
    }

    public String getName() {
        return name;
    }

    public WorkflowPart getWorkflowStart() {
        return workflowStart;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWorkflowStart(WorkflowPart workflowStart) {
        this.workflowStart = workflowStart;
    }
}
