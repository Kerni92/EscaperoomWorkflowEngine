package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.enumeration;

public enum SolutionType {
    TEXT("text"),
    MULTITEXT("multitext");

    private String enumerationValue;

    SolutionType(String enumerationValue) {
        this.enumerationValue = enumerationValue;
    }

    public String getEnumerationValue() {
        return enumerationValue;
    }

    public static SolutionType fromEnumerationValue(String value) {
        for (final SolutionType type : values()) {
            if (type.getEnumerationValue().equals(value)) {
                return type;
            }
        }
        return null;
    }
}
