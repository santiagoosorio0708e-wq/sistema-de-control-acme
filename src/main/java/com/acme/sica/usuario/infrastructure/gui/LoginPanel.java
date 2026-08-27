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
        
        // Crear el panel central estilo "tarjeta"
        JPanel card = new JPanel(new GridBagLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Título
        JLabel title = new JLabel("SICA - Zona Acme");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        card.add(title, gbc);
        
        // Subtítulo
        JLabel subtitle = new JLabel("Inicio de Sesión");
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        card.add(subtitle, gbc);
        
        // Usuario
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        card.add(new JLabel("Usuario:"), gbc);
        
        userField = new JTextField(15);
        gbc.gridx = 1;
        card.add(userField, gbc);
        
        // Contraseña
        gbc.gridx = 0;
        gbc.gridy = 3;
        card.add(new JLabel("Contraseña:"), gbc);
        
        passField = new JPasswordField(15);
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
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.addActionListener(this::handleLogin);
        gbc.gridy = 5;
        card.add(loginButton, gbc);
        
        // Cuentas de prueba (Helper)
        JPanel helperPanel = new JPanel();
        helperPanel.add(new JLabel("Cuentas (User/Pass):"));
        helperPanel.add(crearBotonAyuda("Admin", "admin", "admin123"));
        helperPanel.add(crearBotonAyuda("Guarda", "guarda1", "guarda123"));
        helperPanel.add(crearBotonAyuda("Func.", "funcionario1", "func123"));
        
        gbc.gridy = 6;
        card.add(helperPanel, gbc);

        add(card);
    }
    
    private JButton crearBotonAyuda(String titulo, String user, String pass) {
        JButton btn = new JButton(titulo);
        btn.setFont(new Font("Arial", Font.PLAIN, 10));
        btn.addActionListener(e -> {
            userField.setText(user);
            passField.setText(pass);
        });
        return btn;
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
