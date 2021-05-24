package de.kernebeck.escaperoom.escaperoomgame.webservice.resources;

import java.sql.Timestamp;

public class WorkflowResource extends BasicResource {

    private String name;

    private Long workflowStartElement;

    public WorkflowResource() {
        super();
        //empty constructor for jackson
    }

    public WorkflowResource(Long id, Timestamp dbcreationdate, Timestamp dbupdatedate, String name, Long workflowStartElement) {
        super(id, dbcreationdate, dbupdatedate);
        this.name = name;
        this.workflowStartElement = workflowStartElement;
    }

    public String getName() {
        return name;
    }

    public Long getWorkflowStartElement() {
        return workflowStartElement;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWorkflowStartElement(Long workflowStartElement) {
        this.workflowStartElement = workflowStartElement;
    }
}
