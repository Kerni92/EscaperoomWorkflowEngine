package de.kernebeck.escaperoom.escaperoomgame.basic.database;

import org.testcontainers.containers.MariaDBContainer;

public class MariaDBTestContainer extends MariaDBContainer<MariaDBTestContainer> {

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

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }

    public static MariaDBTestContainer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MariaDBTestContainer();
            INSTANCE.start();
        }
        return INSTANCE;
    }
}
