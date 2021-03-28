package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "workflow")
public class Workflow extends BasicEntity {

    @Column(name = "name", nullable = false)
    private String name;


}
