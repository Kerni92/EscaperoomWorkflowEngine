package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.authorisation;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.BasicEntity;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "user")
@Entity
public class User extends BasicEntity {

    @Column(name = "firstname", nullable = false)
    private String firstName;

    @Column(name = "lastname", nullable = false)
    private String lastName;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    public User(String firstName, String lastName, String username, String password, Boolean enabled) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

    public User() {
        super();
        //default constructor required for hibernate
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
