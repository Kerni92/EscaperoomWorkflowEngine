package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.BasicEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "riddlehint")
public class RiddleHint extends BasicEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "content")
    private String content;

    @Column(name = "sortindex")
    private Integer sortIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_riddle")
    private Riddle riddle;

    public RiddleHint() {
        super();
        //default construtor for hibnerate
    }

    public RiddleHint(String name, String content, Integer sortIndex, Riddle riddle) {
        super();
        this.name = name;
        this.content = content;
        this.riddle = riddle;
        this.sortIndex = sortIndex;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    public Riddle getRiddle() {
        return riddle;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }

    public void setRiddle(Riddle riddle) {
        this.riddle = riddle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RiddleHint that = (RiddleHint) o;
        return Objects.equals(name, that.name) && Objects.equals(content, that.content) && Objects.equals(sortIndex, that.sortIndex) && Objects.equals(riddle, that.riddle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, content, sortIndex, riddle);
    }
}
