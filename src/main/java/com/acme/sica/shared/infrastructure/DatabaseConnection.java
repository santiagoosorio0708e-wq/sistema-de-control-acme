package com.acme.sica.shared.infrastructure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Singleton Pattern — Conexión única a la base de datos SQLite.
 * Garantiza que toda la aplicación comparta una única instancia de conexión,
 * evitando problemas de concurrencia y desperdicio de recursos.
 */
public final class DatabaseConnection {

    private static final String DB_URL = "jdbc:sqlite:sica.db";
    private static volatile DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            this.connection = DriverManager.getConnection(DB_URL);
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
                stmt.execute("PRAGMA journal_mode = WAL");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar con la base de datos SQLite: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene la instancia única de la conexión (Double-Checked Locking).
     */
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    /**
     * Retorna la conexión activa a SQLite.
     * Si la conexión se ha cerrado, la reabre automáticamente.
     */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute("PRAGMA foreign_keys = ON");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al reconectar con la base de datos: " + e.getMessage(), e);
        }
        return connection;
    }

    /**
     * Cierra la conexión a la base de datos.
     */
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}
