package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.BasicEntity;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.enumeration.SolutionType;

import javax.persistence.*;

@Entity
@Table(name = "solution")
public class Solution extends BasicEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private SolutionType type;

    @Column(name = "solution")
    private String solution;

    @Column(name = "solutionoptions")
    private String solutionOptions;

    @ManyToOne
    @JoinColumn(name = "fk_workflowpart")
    private WorkflowPart workflowPart;

    public Solution() {
        super();
        //empty constructor for hibernate
    }

    public Solution(String name, String description, SolutionType type, String solution, String solutionOptions, WorkflowPart workflowPart) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.solution = solution;
        this.solutionOptions = solutionOptions;
        this.workflowPart = workflowPart;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public SolutionType getType() {
        return type;
    }

    public String getSolution() {
        return solution;
    }

    public String getSolutionOptions() {
        return solutionOptions;
    }

    public WorkflowPart getWorkflowPart() {
        return workflowPart;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(SolutionType type) {
        this.type = type;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public void setSolutionOptions(String solutionOptions) {
        this.solutionOptions = solutionOptions;
    }

    public void setWorkflowPart(WorkflowPart workflowPart) {
        this.workflowPart = workflowPart;
    }
}
