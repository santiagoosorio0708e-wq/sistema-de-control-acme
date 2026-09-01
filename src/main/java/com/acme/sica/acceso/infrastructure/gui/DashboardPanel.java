package com.acme.sica.acceso.infrastructure.gui;

import com.acme.sica.acceso.application.ControlAccesoService;
import com.acme.sica.acceso.application.NotificacionService;
import com.acme.sica.acceso.domain.model.Visita;
import com.acme.sica.incidente.application.IncidenteService;
import com.acme.sica.incidente.domain.model.Incidente;
import com.acme.sica.shared.infrastructure.gui.MainFrame;
import com.acme.sica.shared.security.SesionActual;
import com.acme.sica.usuario.domain.model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DashboardPanel extends JPanel {

    private final MainFrame parentFrame;
    private final ControlAccesoService accesoService;
    private final IncidenteService incidenteService;
    private final NotificacionService notificacionService;

    private JLabel welcomeLabel;
    private JLabel statActivasLabel;
    private JLabel statPendientesLabel;
    
    private DefaultTableModel tablaModelo;
    private JTable tablaAccesos;

    public DashboardPanel(MainFrame parentFrame, 
                          ControlAccesoService accesoService, 
                          IncidenteService incidenteService,
                          NotificacionService notificacionService) {
        this.parentFrame = parentFrame;
        this.accesoService = accesoService;
        this.incidenteService = incidenteService;
        this.notificacionService = notificacionService;

        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        headerPanel.setBackground(new Color(255, 255, 255));
        
        welcomeLabel = new JLabel("Bienvenido");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        welcomeLabel.setForeground(new Color(44, 62, 80));
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        
        JButton logoutBtn = new JButton("Cerrar Sesión");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutBtn.setBackground(new Color(231, 76, 60));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            SesionActual.cerrarSesion();
            parentFrame.mostrarPanel("LOGIN");
        });
        headerPanel.add(logoutBtn, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Main Content (SplitPane para menú lateral y contenido)
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(220);
        splitPane.setDividerSize(1);
        splitPane.setBorder(null);

        // Menú Lateral
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        menuPanel.setBackground(new Color(44, 62, 80));

        JButton btnDashboard = new JButton("Dashboard");
        btnDashboard.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btnDashboard.setForeground(Color.WHITE);
        btnDashboard.setBackground(new Color(52, 73, 94));
        btnDashboard.setFocusPainted(false);
        btnDashboard.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btnDashboard.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDashboard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        menuPanel.add(btnDashboard);
        menuPanel.add(Box.createVerticalStrut(10));
        
        JButton btnAccesos = new JButton("Gestión Accesos");
        btnAccesos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnAccesos.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btnAccesos.setForeground(Color.WHITE);
        btnAccesos.setBackground(new Color(52, 73, 94));
        btnAccesos.setFocusPainted(false);
        btnAccesos.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btnAccesos.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAccesos.addActionListener(e -> parentFrame.mostrarPanel("ACCESOS"));
        menuPanel.add(btnAccesos);

        splitPane.setLeftComponent(menuPanel);

        // Contenido Dashboard
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        contentPanel.setBackground(new Color(245, 247, 250));

        // Stats (Top of content)
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        
        JPanel stat1 = crearStatPanel("Visitas Activas", statActivasLabel = new JLabel("0"));
        JPanel stat2 = crearStatPanel("Solicitudes Pendientes", statPendientesLabel = new JLabel("0"));
        
        statsPanel.add(stat1);
        statsPanel.add(stat2);
        
        contentPanel.add(statsPanel, BorderLayout.NORTH);

        // Tabla de últimos accesos
        tablaModelo = new DefaultTableModel(new String[]{"Persona", "Destino", "Estado", "Entrada"}, 0);
        tablaAccesos = new JTable(tablaModelo);
        tablaAccesos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaAccesos.setRowHeight(30);
        tablaAccesos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaAccesos.getTableHeader().setBackground(new Color(236, 240, 241));
        
        JScrollPane scrollPane = new JScrollPane(tablaAccesos);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(20, 0, 0, 0),
            BorderFactory.createLineBorder(new Color(220, 224, 229))
        ));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        splitPane.setRightComponent(contentPanel);
        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel crearStatPanel(String titulo, JLabel valorLabel) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 224, 229), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panel.setBackground(Color.WHITE);
        
        JLabel tit = new JLabel(titulo);
        tit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tit.setForeground(new Color(127, 140, 141));
        
        valorLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valorLabel.setForeground(new Color(44, 62, 80));
        
        panel.add(tit, BorderLayout.NORTH);
        panel.add(valorLabel, BorderLayout.CENTER);
        return panel;
    }

    public void refrescarDatos() {
        Usuario u = SesionActual.getUsuario();
        if (u != null) {
            welcomeLabel.setText("Bienvenido, " + u.getNombreCompleto() + " (" + u.getRolNombre() + ")");
        }
        
        try {
            List<Visita> visitas = accesoService.listarVisitas();
            
            long activas = visitas.stream().filter(v -> "DENTRO".equals(v.getEstado())).count();
            long pendientes = visitas.stream().filter(v -> "PENDIENTE".equals(v.getEstado())).count();
            
            statActivasLabel.setText(String.valueOf(activas));
            statPendientesLabel.setText(String.valueOf(pendientes));
            
            tablaModelo.setRowCount(0); // Limpiar tabla
            for (int i = 0; i < Math.min(visitas.size(), 20); i++) {
                Visita v = visitas.get(i);
                tablaModelo.addRow(new Object[]{
                    v.getPersonaNombre(),
                    v.getEmpresaDestinoNombre() != null ? v.getEmpresaDestinoNombre() : "-",
                    v.getEstado(),
                    v.getFechaHoraEntrada() != null ? v.getFechaHoraEntrada() : "-"
                });
            }
        } catch (Exception e) {
            System.err.println("Error al cargar datos del dashboard: " + e.getMessage());
        }
    }
}
