package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.definition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RiddleHintDTO {

    private String name;

    private String content;

    public RiddleHintDTO() {
        //empty constructor for jackson
    }

    public RiddleHintDTO(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
