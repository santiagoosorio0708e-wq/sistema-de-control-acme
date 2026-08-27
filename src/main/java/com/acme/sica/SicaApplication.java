package com.acme.sica;

import com.acme.sica.shared.infrastructure.DatabaseConnection;
import com.acme.sica.shared.infrastructure.DatabaseInitializer;
import com.acme.sica.shared.infrastructure.gui.MainFrame;

import javax.swing.SwingUtilities;
import java.sql.Connection;

/**
 * Punto de entrada principal de la aplicación SICA.
 */
public class SicaApplication {

    public static void main(String[] args) {
        System.out.println("Iniciando Sistema Integrado de Control de Acceso (SICA)...");

        // 1. Inicializar Base de Datos (Singleton)
        Connection connection = DatabaseConnection.getInstance().getConnection();
        DatabaseInitializer dbInit = new DatabaseInitializer(connection);
        dbInit.initialize();

        // 2. Iniciar Interfaz Gráfica (Swing)
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(connection);
            mainFrame.setVisible(true);
        });
    }
}
