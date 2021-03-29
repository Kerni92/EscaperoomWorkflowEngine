package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity;

import javax.persistence.*;

@Entity
@Table(name = "workflow")
public class Workflow extends BasicEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne(targetEntity = WorkflowPart.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "workflowStart")
    private WorkflowPart workflowStart;


    public Workflow() {
        //empty constructor for hibernate
    }

    public Workflow(String name) {
        this.name = name;
    }


}
