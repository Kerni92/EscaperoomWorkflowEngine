package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.BasicEntity;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.enumeration.SolutionType;

import javax.persistence.*;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "solution")
public class Solution extends BasicEntity {

    private static final ObjectMapper OBJECTMAPPER = new ObjectMapper();

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
    @JoinColumn(name = "fk_riddle")
    private Riddle riddle;

    @ManyToOne
    @JoinColumn(name = "fk_workflowpart")
    private WorkflowPart workflowPart;

    public Solution() {
        super();
        //empty constructor for hibernate
    }

    public Solution(String name, String description, SolutionType type, String solution, WorkflowPart workflowPart) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.solution = solution;
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

    public List<String> getSolutionOptions() {
        if (this.solutionOptions != null && !this.solutionOptions.isEmpty()) {
            try {
                return OBJECTMAPPER.readValue(new StringReader(this.solutionOptions), new TypeReference<List<String>>() {
                });
            }
            catch (IOException e) {
                //ignore -> should not happen
            }
        }
        return Collections.emptyList();
    }

    public Riddle getRiddle() {
        return riddle;
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

    public void setSolutionOptions(List<String> solutionOptions) {
        try {
            this.solutionOptions = OBJECTMAPPER.writeValueAsString(solutionOptions);
        }
        catch (IOException e) {
            //ignore -> should not happen
        }
    }

    public void setRiddle(Riddle riddle) {
        this.riddle = riddle;
    }

    public void setWorkflowPart(WorkflowPart workflowPart) {
        this.workflowPart = workflowPart;
    }
}
