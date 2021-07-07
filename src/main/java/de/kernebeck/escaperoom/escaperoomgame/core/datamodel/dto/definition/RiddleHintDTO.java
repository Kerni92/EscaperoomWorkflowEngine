package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.definition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RiddleHintDTO {

    private String name;

    private String content;

    private Integer sortIndex;

    public RiddleHintDTO() {
        //empty constructor for jackson
    }

    public RiddleHintDTO(String name, String content, Integer sortIndex) {
        this.name = name;
        this.content = content;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }
}
