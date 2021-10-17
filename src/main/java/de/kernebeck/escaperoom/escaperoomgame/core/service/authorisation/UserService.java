package de.kernebeck.escaperoom.escaperoomgame.core.service.authorisation;

public interface UserService {

    /**
     * Überprüft ob der Benutzer in der Datenbank existiert
     *
     * @param username - Benutzername der überprüft werden soll
     * @return true  falls eher existiert sonst false
     */
    boolean existsUser(String username);

    /**
     * Erzeugt einen neuen Benutzer in der Datenbank
     *
     * @param firstname Vorname des Benutzers
     * @param lastname  Nachname des Benutzers
     * @param username  Benutzername des Benutzers
     * @param password  Passwort des Benutzers
     * @param isEnabled Flag ob der Benutzer aktiv ist und verwendet werden darf
     */
    void createUser(String firstname, String lastname, String username, String password, boolean isEnabled);

    /**
     * Überprüft ob der angegebene Benutzername und das Password eine gültige Kombination sind
     *
     * @param username Benutzername
     * @param password Password
     * @return true wenn Benutzer und Password matchen, sonst false
     */
    boolean checkAuthorisation(String username, String password);

}
