package com.acme.sica.shared.infrastructure;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

/**
 * Inicializador de la base de datos.
 * Ejecuta los scripts schema.sql y data.sql al arrancar la aplicación.
 * Solo inserta datos si la base de datos está vacía.
 */
public class DatabaseInitializer {

    private final Connection connection;

    public DatabaseInitializer(Connection connection) {
        this.connection = connection;
    }

    /**
     * Inicializa la base de datos: crea tablas y carga datos de prueba.
     */
    public void initialize() {
        try {
            executeScript("schema.sql");
            if (isDatabaseEmpty()) {
                executeScript("data.sql");
                System.out.println("  ✓ Datos de prueba cargados exitosamente.");
            } else {
                System.out.println("  ✓ Base de datos ya contiene datos. Se omitió la carga de seed data.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al inicializar la base de datos: " + e.getMessage(), e);
        }
    }

    /**
     * Verifica si la base de datos está vacía (sin roles).
     */
    private boolean isDatabaseEmpty() {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM roles")) {
            return rs.next() && rs.getInt(1) == 0;
        } catch (SQLException e) {
            return true; // Si la tabla no existe, está vacía
        }
    }

    /**
     * Ejecuta un script SQL desde los recursos del classpath.
     * Soporta sentencias multi-línea separadas por punto y coma.
     */
    private void executeScript(String resourceName) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            if (is == null) {
                throw new RuntimeException("No se encontró el recurso: " + resourceName);
            }

            String content = new BufferedReader(new InputStreamReader(is))
                    .lines()
                    .map(String::trim)
                    .filter(line -> !line.startsWith("--") && !line.isEmpty())
                    .collect(Collectors.joining("\n"));

            // Dividir por punto y coma
            String[] statements = content.split(";");

            connection.setAutoCommit(false);
            try (Statement stmt = connection.createStatement()) {
                for (String sql : statements) {
                    String trimmed = sql.trim();
                    if (!trimmed.isEmpty()) {
                        stmt.execute(trimmed);
                    }
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error SQL al ejecutar " + resourceName + ": " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error al leer " + resourceName + ": " + e.getMessage(), e);
        }
    }
}
