package de.kernebeck.escaperoom.escaperoomgame.webservice.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.RiddleHint;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RiddleHintResource implements Serializable {

    private Long id;

    private String name;

    private String content;

    private Integer sortIndex;

    private Long riddleId;

    public RiddleHintResource() {
        //default constructor requried for jackson
    }

    public RiddleHintResource(String name, String content, Integer sortIndex) {
        this.name = name;
        this.content = content;
        this.sortIndex = sortIndex;
    }

    public RiddleHintResource(RiddleHint r) {
        this.id = r.getId();
        this.name = r.getName();
        this.content = r.getContent();
        this.sortIndex = r.getSortIndex();
        this.riddleId = r.getRiddle().getId();
    }

    public Long getId() {
        return id;
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

    public Long getRiddleId() {
        return riddleId;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setRiddleId(Long riddleId) {
        this.riddleId = riddleId;
    }
}
