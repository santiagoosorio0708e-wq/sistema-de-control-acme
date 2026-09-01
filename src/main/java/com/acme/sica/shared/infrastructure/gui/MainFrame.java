package com.acme.sica.shared.infrastructure.gui;

import com.acme.sica.acceso.application.ControlAccesoService;
import com.acme.sica.acceso.application.NotificacionService;
import com.acme.sica.acceso.domain.port.VisitaRepository;
import com.acme.sica.acceso.infrastructure.persistence.VisitaRepositoryImpl;
import com.acme.sica.auditoria.application.AuditoriaService;
import com.acme.sica.auditoria.domain.port.BitacoraRepository;
import com.acme.sica.auditoria.infrastructure.persistence.BitacoraRepositoryImpl;
import com.acme.sica.incidente.application.IncidenteService;
import com.acme.sica.incidente.domain.port.IncidenteRepository;
import com.acme.sica.incidente.infrastructure.persistence.IncidenteRepositoryImpl;
import com.acme.sica.persona.application.PersonaService;
import com.acme.sica.persona.domain.port.PersonaRepository;
import com.acme.sica.persona.infrastructure.persistence.PersonaRepositoryImpl;
import com.acme.sica.shared.security.AutorizacionService;
import com.acme.sica.usuario.application.AuthService;
import com.acme.sica.usuario.domain.port.PermisoRepository;
import com.acme.sica.usuario.domain.port.UsuarioRepository;
import com.acme.sica.usuario.infrastructure.persistence.PermisoRepositoryImpl;
import com.acme.sica.usuario.infrastructure.persistence.UsuarioRepositoryImpl;

import com.acme.sica.usuario.infrastructure.gui.LoginPanel;
import com.acme.sica.acceso.infrastructure.gui.DashboardPanel;
import com.acme.sica.acceso.infrastructure.gui.AccesosPanel;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class MainFrame extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    // Servicios
    private AuthService authService;
    private ControlAccesoService accesoService;
    private PersonaService personaService;
    private IncidenteService incidenteService;
    private NotificacionService notificacionService;

    public MainFrame(Connection connection) {
        super("SICA - Sistema Integrado de Control de Acceso");

        // Inicializar Servicios
        inicializarServicios(connection);

        // Configuración de la Ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 768);
        setLocationRelativeTo(null);

        // Configuración del layout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Instanciar Paneles
        LoginPanel loginPanel = new LoginPanel(this, authService);
        DashboardPanel dashboardPanel = new DashboardPanel(this, accesoService, incidenteService, notificacionService);
        AccesosPanel accesosPanel = new AccesosPanel(this, accesoService, personaService, notificacionService);

        // Agregar paneles al CardLayout
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(dashboardPanel, "DASHBOARD");
        mainPanel.add(accesosPanel, "ACCESOS");

        add(mainPanel);

        // Mostrar Login por defecto
        mostrarPanel("LOGIN");
    }

    private void inicializarServicios(Connection connection) {
        // Repositorios
        UsuarioRepository usuarioRepo = new UsuarioRepositoryImpl(connection);
        PermisoRepository permisoRepo = new PermisoRepositoryImpl(connection);
        PersonaRepository personaRepo = new PersonaRepositoryImpl(connection);
        VisitaRepository visitaRepo = new VisitaRepositoryImpl(connection);
        IncidenteRepository incidenteRepo = new IncidenteRepositoryImpl(connection);
        BitacoraRepository bitacoraRepo = new BitacoraRepositoryImpl(connection);

        // Servicios
        AutorizacionService autorizacionService = new AutorizacionService(permisoRepo);
        AuditoriaService auditoriaService = new AuditoriaService(bitacoraRepo, autorizacionService);
        this.authService = new AuthService(usuarioRepo, auditoriaService);
        
        this.personaService = new PersonaService(personaRepo, autorizacionService, auditoriaService);
        
        this.notificacionService = new NotificacionService();
        this.accesoService = new ControlAccesoService(visitaRepo, personaRepo, notificacionService, autorizacionService, auditoriaService);
        
        this.incidenteService = new IncidenteService(incidenteRepo, autorizacionService, auditoriaService);
    }

    public void mostrarPanel(String nombrePanel) {
        cardLayout.show(mainPanel, nombrePanel);
        
        // Ejecutar lógicas de refresco al mostrar paneles
        Component current = null;
        for (Component comp : mainPanel.getComponents()) {
            if (comp.isVisible()) {
                current = comp;
                break;
            }
        }
        
        if (current instanceof DashboardPanel) {
            ((DashboardPanel) current).refrescarDatos();
        } else if (current instanceof AccesosPanel) {
            ((AccesosPanel) current).refrescarVisitas();
        }
    }
}
