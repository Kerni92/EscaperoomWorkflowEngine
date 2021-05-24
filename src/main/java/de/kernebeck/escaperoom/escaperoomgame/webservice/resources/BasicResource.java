package de.kernebeck.escaperoom.escaperoomgame.webservice.resources;

import java.io.Serializable;
import java.sql.Timestamp;

public abstract class BasicResource implements Serializable {

    private Long id;

    private Timestamp dbcreationdate;

    private Timestamp dbupdatedate;

    public BasicResource() {
        //empty constructor for jackson
    }

    public BasicResource(Long id, Timestamp dbcreationdate, Timestamp dbupdatedate) {
        this.id = id;
        this.dbcreationdate = dbcreationdate;
        this.dbupdatedate = dbupdatedate;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setDbcreationdate(Timestamp dbcreationdate) {
        this.dbcreationdate = dbcreationdate;
    }

    public void setDbupdatedate(Timestamp dbupdatedate) {
        this.dbupdatedate = dbupdatedate;
    }
}
