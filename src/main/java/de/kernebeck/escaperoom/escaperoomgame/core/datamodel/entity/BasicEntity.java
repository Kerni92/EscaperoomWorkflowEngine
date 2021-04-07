package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@MappedSuperclass
public class BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "dbcreationdate", nullable = false, updatable = false)
    private Timestamp dbcreationdate;

    @Column(name = "dbupdatedate", nullable = false)
    private Timestamp dbupdatedate;

    public BasicEntity() {
        //default constructor for hibernate
    }

    @PrePersist
    public void updateTimestamps() {
        if (dbcreationdate == null) {
            this.dbcreationdate = new Timestamp(System.currentTimeMillis());
        }
        this.dbupdatedate = new Timestamp(System.currentTimeMillis());
    }

    public Long getId() {
        return id;
    }

    public Timestamp getDbcreationdate() {
        return dbcreationdate;
    }

    public Timestamp getDbupdatedate() {
        return dbupdatedate;
    }

}
