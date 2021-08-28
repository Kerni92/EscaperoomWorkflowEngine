package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.BasicEntity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "riddle")
public class Riddle extends BasicEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "sortindex")
    private int sortIndex;

    @Column(name = "content")
    private String content;

    @OneToMany(targetEntity = Solution.class)
    private List<Solution> solutions;

    @ManyToOne
    @JoinColumn(name = "fk_workflowpart")
    private WorkflowPart workflowPart;

    @OneToMany(targetEntity = RiddleHint.class, cascade = CascadeType.DETACH)
    private List<RiddleHint> hints;

    public Riddle() {
        super();
        //empty constructor for hibernate
    }

    public Riddle(String name, int sortIndex, String content, List<RiddleHint> hints) {
        this.name = name;
        this.sortIndex = sortIndex;
        this.content = content;
        this.hints = hints;
    }

    public String getName() {
        return name;
    }

    public int getSortIndex() {
        return sortIndex;
    }

    public String getContent() {
        return content;
    }

    public List<RiddleHint> getHints() {
        return hints;
    }

    public WorkflowPart getWorkflowPart() {
        return workflowPart;
    }

    public List<Solution> getSolutions() {
        return solutions;
    }

    public void setName(String riddleName) {
        this.name = riddleName;
    }

    public void setSortIndex(int sortIndex) {
        this.sortIndex = sortIndex;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setHints(List<RiddleHint> hints) {
        this.hints = hints;
    }

    public void setWorkflowPart(WorkflowPart workflowPart) {
        this.workflowPart = workflowPart;
    }

    public void setSolutions(List<Solution> solutions) {
        this.solutions = solutions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Riddle riddle = (Riddle) o;
        return sortIndex == riddle.sortIndex && Objects.equals(name, riddle.name) && Objects.equals(content, riddle.content) && Objects.equals(solutions, riddle.solutions) && Objects.equals(workflowPart, riddle.workflowPart) && Objects.equals(hints, riddle.hints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, sortIndex, content, solutions, workflowPart, hints);
    }
}
