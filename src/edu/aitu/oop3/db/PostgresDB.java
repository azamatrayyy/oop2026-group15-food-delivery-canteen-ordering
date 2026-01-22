package edu.aitu.oop3.db;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class PostgresDB implements IDB {
    private final String url;
    private final String user;
    private final String password;

    public PostgresDB() {
        Properties props = new Properties();

        try (FileInputStream in = new FileInputStream("config.properties")) {
            props.load(in);
        } catch (Exception ignored) {

        }

        this.url = get(props, "DB_URL");
        this.user = get(props, "DB_USER");
        this.password = get(props, "DB_PASSWORD");
    }

    private String get(Properties props, String key) {
        String v = props.getProperty(key);
        if (v == null || v.isBlank()) v = System.getenv(key);
        if (v == null || v.isBlank()) {
            throw new RuntimeException(key + " is not set (config.properties or env vars)");
        }
        return v.trim();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
