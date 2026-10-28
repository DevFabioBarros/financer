package br.com.financer.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConexaoMySQL {
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/financer?createDatabaseIfNotExist=true&serverTimezone=America/Sao_Paulo";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "";

    private ConexaoMySQL() {
    }

    public static Connection conectar() throws SQLException {
        String url = getConfig("db.url", "FINANCER_DB_URL", DEFAULT_URL);
        String user = getConfig("db.user", "FINANCER_DB_USER", DEFAULT_USER);
        String password = getConfig("db.password", "FINANCER_DB_PASSWORD", DEFAULT_PASSWORD);
        return DriverManager.getConnection(url, user, password);
    }

    private static String getConfig(String propertyName, String environmentName, String defaultValue) {
        String propertyValue = System.getProperty(propertyName);
        if (propertyValue != null && !propertyValue.isBlank()) {
            return propertyValue;
        }

        String environmentValue = System.getenv(environmentName);
        if (environmentValue != null && !environmentValue.isBlank()) {
            return environmentValue;
        }

        return defaultValue;
    }
}
