package de.kernebeck.escaperoom.escaperoomgame.basic.database;

import org.testcontainers.containers.MariaDBContainer;

import javax.security.auth.DestroyFailedException;
import javax.security.auth.Destroyable;

public class MariaDBTestContainer extends MariaDBContainer<MariaDBTestContainer> implements Destroyable {

    private static MariaDBTestContainer INSTANCE;

    public MariaDBTestContainer() {
        super("mariadb:10.3.28-focal");
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", INSTANCE.getJdbcUrl());
        System.setProperty("DB_USERNAME", INSTANCE.getUsername());
        System.setProperty("DB_PASSWORD", INSTANCE.getPassword());
    }

    public static MariaDBTestContainer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MariaDBTestContainer();
            INSTANCE.start();
        }
        return INSTANCE;
    }

    @Override
    public void destroy() throws DestroyFailedException {
        super.stop();
    }
}
