package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.enumeration;

public enum WorkflowPartType {
    PART("part"),
    DECISION("decision"),
    ENDPART("endpart");


    private String enumerationValue;

    WorkflowPartType(String enumerationValue) {
        this.enumerationValue = enumerationValue;
    }

    public String getEnumerationValue() {
        return enumerationValue;
    }

    public static WorkflowPartType fromEnumerationValue(String value) {
        for (final WorkflowPartType parttype : values()) {
            if (parttype.getEnumerationValue().equals(value)) {
                return parttype;
            }
        }
        return null;
    }
}
