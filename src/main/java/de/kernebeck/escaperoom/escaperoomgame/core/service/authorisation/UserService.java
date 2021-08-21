package de.kernebeck.escaperoom.escaperoomgame.core.service.authorisation;

public interface UserService {

    boolean existsUser(String username);

    void createUser(String firstname, String lastname, String username, String password, boolean isEnabled);

    boolean checkAuthorisation(String username, String password);

}
