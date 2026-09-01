package com.acme.sica.usuario.infrastructure.gui;

import com.acme.sica.shared.infrastructure.gui.MainFrame;
import com.acme.sica.shared.domain.exception.AccesoDenegadoException;
import com.acme.sica.usuario.application.AuthService;
import com.acme.sica.usuario.domain.model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginPanel extends JPanel {

    private final MainFrame parentFrame;
    private final AuthService authService;

    private JTextField userField;
    private JPasswordField passField;
    private JLabel errorLabel;

    public LoginPanel(MainFrame parentFrame, AuthService authService) {
        this.parentFrame = parentFrame;
        this.authService = authService;
        
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 247, 250)); // Fondo suave

        
        // Crear el panel central estilo "tarjeta"
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 224, 229), 1),
                BorderFactory.createEmptyBorder(40, 50, 40, 50)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Título
        JLabel title = new JLabel("SICA - Zona Acme");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(44, 62, 80));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        card.add(title, gbc);
        
        // Subtítulo
        JLabel subtitle = new JLabel("Inicio de Sesión");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitle.setForeground(new Color(127, 140, 141));
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        card.add(subtitle, gbc);
        
        // Usuario
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        JLabel userLabel = new JLabel("Usuario:");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(userLabel, gbc);
        
        userField = new JTextField(15);
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        gbc.gridx = 1;
        card.add(userField, gbc);
        
        // Contraseña
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel passLabel = new JLabel("Contraseña:");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(passLabel, gbc);
        
        passField = new JPasswordField(15);
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        gbc.gridx = 1;
        card.add(passField, gbc);
        
        // Error Label
        errorLabel = new JLabel(" ");
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        card.add(errorLabel, gbc);
        
        // Botón Login
        JButton loginButton = new JButton("Ingresar");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setBackground(new Color(52, 152, 219));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(this::handleLogin);
        gbc.gridy = 5;
        gbc.insets = new Insets(20, 10, 10, 10);
        card.add(loginButton, gbc);
        
        // Cuentas de prueba (Helper) con explicaciones
        JPanel helperPanel = new JPanel();
        helperPanel.setLayout(new BoxLayout(helperPanel, BoxLayout.Y_AXIS));
        helperPanel.setBackground(Color.WHITE);
        helperPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(220, 224, 229)),
            "Cuentas de Prueba (Roles)", 
            0, 0, new Font("Segoe UI", Font.BOLD, 12), new Color(127, 140, 141)
        ));
        
        helperPanel.add(crearFilaAyuda("Admin", "admin", "admin123", "Tiene acceso a todo el sistema."));
        helperPanel.add(Box.createVerticalStrut(5));
        helperPanel.add(crearFilaAyuda("Guarda", "guarda1", "guarda123", "Controla portería (Check-in/out)."));
        helperPanel.add(Box.createVerticalStrut(5));
        helperPanel.add(crearFilaAyuda("Func.", "funcionario1", "func123", "Aprueba visitas y pre-registra."));
        
        gbc.gridy = 6;
        gbc.insets = new Insets(15, 10, 0, 10);
        card.add(helperPanel, gbc);

        add(card);
    }
    
    private JPanel crearFilaAyuda(String titulo, String user, String pass, String desc) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(Color.WHITE);
        
        JButton btn = new JButton(titulo);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setBackground(new Color(236, 240, 241));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(80, 25));
        btn.addActionListener(e -> {
            userField.setText(user);
            passField.setText(pass);
        });
        
        JLabel lblDesc = new JLabel(desc);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblDesc.setForeground(new Color(100, 100, 100));
        
        panel.add(btn, BorderLayout.WEST);
        panel.add(lblDesc, BorderLayout.CENTER);
        
        return panel;
    }

    private void handleLogin(ActionEvent e) {
        String username = userField.getText();
        String password = new String(passField.getPassword());
        
        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            errorLabel.setText("Por favor ingrese credenciales");
            return;
        }
        
        try {
            Usuario user = authService.login(username, password);
            errorLabel.setText(" ");
            passField.setText("");
            
            // Navegar al dashboard
            parentFrame.mostrarPanel("DASHBOARD");
            
        } catch (AccesoDenegadoException ex) {
            errorLabel.setText("Credenciales inválidas");
        } catch (Exception ex) {
            errorLabel.setText("Error en el servidor");
            ex.printStackTrace();
        }
    }
}
