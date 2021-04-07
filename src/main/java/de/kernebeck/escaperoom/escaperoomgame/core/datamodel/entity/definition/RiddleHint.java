package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.BasicEntity;

import javax.persistence.*;

@Entity
@Table(name = "riddlehint")
public class RiddleHint extends BasicEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "fk_riddle")
    private Riddle riddle;

    public RiddleHint() {
        super();
        //default construtor for hibnerate
    }

    public RiddleHint(String name, String content, Riddle riddle) {
        super();
        this.name = name;
        this.content = content;
        this.riddle = riddle;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
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

    public void setRiddle(Riddle riddle) {
        this.riddle = riddle;
    }
}
