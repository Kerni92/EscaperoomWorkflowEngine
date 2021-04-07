package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.BasicEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "riddle")
public class Riddle extends BasicEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "sortindex")
    private int sortIndex;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "fk_workflowpart")
    private WorkflowPart workflowPart;

    @OneToMany(targetEntity = RiddleHint.class)
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
}
