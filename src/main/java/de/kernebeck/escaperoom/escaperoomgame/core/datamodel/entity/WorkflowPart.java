package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "workflowpart")
public class WorkflowPart extends BasicEntity {

    @Column("name")
    private String name;

    @Column("description")
    private String description;

    @Column("parttype")
    @Enumerated(value = EnumType.STRING)
    private WorkflowPartType partType;

    @ManyToOne(targetEntity = Workflow.class, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id", nullable = false)
    private Workflow workflow;

    @OneToMany(targetEntity = WorkflowPart.class, mappedBy = "id")
    private List<WorkflowPart> precedessors;


}
