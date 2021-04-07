package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.definition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RiddleDTO {

    private String name;

    private Integer sortIndex = 0;

    private String content;

    private List<RiddleHintDTO> hints;

    public RiddleDTO() {
        //empty constructor for jackson
    }

    public RiddleDTO(String name, Integer sortIndex, String content, List<RiddleHintDTO> hints) {
        this.name = name;
        this.sortIndex = sortIndex;
        this.content = content;
        this.hints = hints;
    }

    public String getName() {
        return name;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    public String getContent() {
        return content;
    }

    public List<RiddleHintDTO> getHints() {
        return hints;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setHints(List<RiddleHintDTO> hints) {
        this.hints = hints;
    }
}
