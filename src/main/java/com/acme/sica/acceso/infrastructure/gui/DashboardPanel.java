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
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        headerPanel.setBackground(new Color(240, 240, 240));
        
        welcomeLabel = new JLabel("Bienvenido");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        
        JButton logoutBtn = new JButton("Cerrar Sesión");
        logoutBtn.addActionListener(e -> {
            SesionActual.cerrarSesion();
            parentFrame.mostrarPanel("LOGIN");
        });
        headerPanel.add(logoutBtn, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Main Content (SplitPane para menú lateral y contenido)
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(200);

        // Menú Lateral
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton btnDashboard = new JButton("Dashboard");
        btnDashboard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        menuPanel.add(btnDashboard);
        menuPanel.add(Box.createVerticalStrut(10));
        
        JButton btnAccesos = new JButton("Gestión Accesos");
        btnAccesos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnAccesos.addActionListener(e -> parentFrame.mostrarPanel("ACCESOS"));
        menuPanel.add(btnAccesos);

        splitPane.setLeftComponent(menuPanel);

        // Contenido Dashboard
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

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
        JScrollPane scrollPane = new JScrollPane(tablaAccesos);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Últimos Registros"));
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        splitPane.setRightComponent(contentPanel);
        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel crearStatPanel(String titulo, JLabel valorLabel) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setBackground(Color.WHITE);
        
        JLabel tit = new JLabel(titulo);
        tit.setForeground(Color.GRAY);
        valorLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
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
