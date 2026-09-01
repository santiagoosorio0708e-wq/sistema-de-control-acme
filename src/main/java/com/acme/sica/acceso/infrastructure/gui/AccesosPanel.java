package com.acme.sica.acceso.infrastructure.gui;

import com.acme.sica.acceso.application.ControlAccesoService;
import com.acme.sica.acceso.application.NotificacionService;
import com.acme.sica.acceso.domain.model.Visita;
import com.acme.sica.persona.application.PersonaService;
import com.acme.sica.persona.domain.model.Persona;
import com.acme.sica.shared.infrastructure.gui.MainFrame;
import com.acme.sica.shared.security.SesionActual;
import com.acme.sica.usuario.domain.model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AccesosPanel extends JPanel {

    private final ControlAccesoService accesoService;
    private final PersonaService personaService;

    private DefaultTableModel tablaModelo;
    private JTable tablaAccesos;
    
    private JPanel actionBar; // Panel de Guarda (Check-In/Out)
    private JPanel funcActions; // Panel de Funcionario (Aprobar/Rechazar)

    public AccesosPanel(MainFrame parentFrame, 
                        ControlAccesoService accesoService, 
                        PersonaService personaService,
                        NotificacionService notificacionService) {
        this.accesoService = accesoService;
        this.personaService = personaService;

        setLayout(new BorderLayout());

        // Header and Back Button
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        headerPanel.setBackground(new Color(255, 255, 255));
        
        JButton backBtn = new JButton("<- Volver al Dashboard");
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backBtn.setBackground(new Color(236, 240, 241));
        backBtn.setForeground(new Color(44, 62, 80));
        backBtn.setFocusPainted(false);
        backBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> parentFrame.mostrarPanel("DASHBOARD"));
        headerPanel.add(backBtn);
        
        JLabel titleLabel = new JLabel("  |  Gestión de Accesos");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(44, 62, 80));
        headerPanel.add(titleLabel);
        
        add(headerPanel, BorderLayout.NORTH);

        // Content
        JPanel contentPanel = new JPanel(new BorderLayout(0, 15));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        contentPanel.setBackground(new Color(245, 247, 250));

        // Action Bar (Top of content) - Solo para Guarda / Admin
        actionBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        actionBar.setBackground(new Color(245, 247, 250));
        
        JButton btnCheckIn = new JButton("Check In Seleccionada");
        btnCheckIn.addActionListener(e -> manejarCheck(true));
        actionBar.add(btnCheckIn);
        
        actionBar.add(new JLabel(" | "));
        
        JButton btnCheckOut = new JButton("Check Out Seleccionada");
        btnCheckOut.addActionListener(e -> manejarCheck(false));
        actionBar.add(btnCheckOut);
        
        // Botón Registrar Visitante No Anunciado
        JButton btnNoAnunciado = new JButton("+ Visitante No Anunciado");
        btnNoAnunciado.addActionListener(e -> mostrarDialogoRegistroVisita(false));
        actionBar.add(new JLabel(" | "));
        actionBar.add(btnNoAnunciado);
        
        contentPanel.add(actionBar, BorderLayout.NORTH);

        // Table
        tablaModelo = new DefaultTableModel(new String[]{"ID", "Persona", "Empresa", "Estado", "Entrada", "Salida"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaAccesos = new JTable(tablaModelo);
        tablaAccesos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaAccesos.setRowHeight(30);
        tablaAccesos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaAccesos.getTableHeader().setBackground(new Color(236, 240, 241));

        JScrollPane scrollPane = new JScrollPane(tablaAccesos);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 224, 229)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Approve/Reject Panel (Only for Funcionario)
        funcActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        funcActions.setBackground(new Color(245, 247, 250));
        JButton btnAprobar = new JButton("Aprobar Seleccionada");
        btnAprobar.addActionListener(e -> manejarAprobacion(true));
        
        JButton btnRechazar = new JButton("Rechazar Seleccionada");
        btnRechazar.addActionListener(e -> manejarAprobacion(false));
        
        JButton btnPreRegistro = new JButton("+ Pre-registrar Visita");
        btnPreRegistro.addActionListener(e -> mostrarDialogoRegistroVisita(true));
        
        funcActions.add(btnPreRegistro);
        funcActions.add(btnAprobar);
        funcActions.add(btnRechazar);
        contentPanel.add(funcActions, BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.CENTER);
    }
    
    private void aplicarPermisos() {
        Usuario u = SesionActual.getUsuario();
        if (u != null) {
            String rol = u.getRolNombre();
            
            // Guarda o Admin pueden hacer CheckIn / CheckOut
            boolean esGuardaOAdmin = "GUARDA_SEGURIDAD".equals(rol) || "ADMINISTRADOR".equals(rol);
            actionBar.setVisible(esGuardaOAdmin);
            
            // Funcionario o Admin pueden Aprobar / Rechazar
            boolean esFuncionarioOAdmin = "FUNCIONARIO_EMPRESA".equals(rol) || "ADMINISTRADOR".equals(rol);
            funcActions.setVisible(esFuncionarioOAdmin);
        }
    }
    
    private void manejarAprobacion(boolean aprobar) {
        int selectedRow = tablaAccesos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una visita de la tabla");
            return;
        }
        
        int visitaId = Integer.parseInt(tablaModelo.getValueAt(selectedRow, 0).toString());
        String estadoActual = tablaModelo.getValueAt(selectedRow, 3).toString();
        
        if (!"PENDIENTE".equals(estadoActual)) {
            JOptionPane.showMessageDialog(this, "Solo se pueden aprobar/rechazar visitas pendientes");
            return;
        }
        
        try {
            int funcId = SesionActual.getUsuarioId();
            if (aprobar) {
                accesoService.aprobarVisita(visitaId, funcId, "Aprobado desde Desktop GUI");
                JOptionPane.showMessageDialog(this, "Visita Aprobada");
            } else {
                accesoService.rechazarVisita(visitaId, funcId, "Rechazado desde Desktop GUI");
                JOptionPane.showMessageDialog(this, "Visita Rechazada");
            }
            refrescarVisitas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void manejarCheck(boolean isCheckIn) {
        int selectedRow = tablaAccesos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una visita de la tabla");
            return;
        }
        
        int visitaId = Integer.parseInt(tablaModelo.getValueAt(selectedRow, 0).toString());
        String estadoActual = tablaModelo.getValueAt(selectedRow, 3).toString();
        
        try {
            if (isCheckIn) {
                if (!"APROBADO".equals(estadoActual)) {
                    JOptionPane.showMessageDialog(this, "Solo se puede dar ingreso (Check-In) a visitas APROBADAS.");
                    return;
                }
                accesoService.checkIn(visitaId);
                JOptionPane.showMessageDialog(this, "Check-in exitoso. La persona ha ingresado.");
            } else {
                if (!"DENTRO".equals(estadoActual)) {
                    JOptionPane.showMessageDialog(this, "Solo se puede dar salida (Check-Out) a visitas que están DENTRO.");
                    return;
                }
                accesoService.checkOut(visitaId);
                JOptionPane.showMessageDialog(this, "Check-out exitoso. La persona ha salido.");
            }
            refrescarVisitas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDialogoRegistroVisita(boolean esPreRegistro) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField txtDocumento = new JTextField();
        JTextField txtNombre = new JTextField();
        JTextField txtApellido = new JTextField();
        JTextField txtEmpresaId = new JTextField();
        JTextField txtMotivo = new JTextField();
        
        panel.add(new JLabel("Documento (CC):"));
        panel.add(txtDocumento);
        panel.add(new JLabel("Nombres (si es nuevo):"));
        panel.add(txtNombre);
        panel.add(new JLabel("Apellidos (si es nuevo):"));
        panel.add(txtApellido);
        panel.add(new JLabel("ID Empresa Destino:"));
        panel.add(txtEmpresaId);
        panel.add(new JLabel("Motivo:"));
        panel.add(txtMotivo);
        
        String titulo = esPreRegistro ? "Pre-registrar Visita" : "Registrar Visitante No Anunciado";
        int result = JOptionPane.showConfirmDialog(this, panel, titulo, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                String documento = txtDocumento.getText().trim();
                if (documento.isEmpty()) {
                    throw new RuntimeException("El documento es obligatorio.");
                }

                Persona persona = null;
                try {
                    persona = personaService.buscarPorDocumento(documento);
                } catch (Exception e) {
                    // Si no existe, crearla
                    String nombre = txtNombre.getText().trim();
                    String apellido = txtApellido.getText().trim();
                    
                    if (nombre.isEmpty() || apellido.isEmpty()) {
                        throw new RuntimeException("La persona no existe. Debe ingresar Nombre y Apellido para registrarla por primera vez.");
                    }
                    
                    persona = new Persona();
                    persona.setDocumento(documento);
                    persona.setTipoDocumento("CC");
                    persona.setNombre(nombre);
                    persona.setApellido(apellido);
                    persona.setTipoPersona("VISITANTE");
                    
                    persona = personaService.crearPersona(persona);
                }
                
                Visita visita = new Visita();
                visita.setPersonaId(persona.getId());
                visita.setEmpresaDestinoId(Integer.parseInt(txtEmpresaId.getText().trim()));
                visita.setMotivo(txtMotivo.getText().trim());
                visita.setEstado(esPreRegistro ? "APROBADO" : "PENDIENTE");
                if (esPreRegistro) {
                    visita.setFuncionarioAutorizaId(SesionActual.getUsuarioId());
                }
                
                accesoService.registrarVisita(visita);
                JOptionPane.showMessageDialog(this, "Visita registrada exitosamente.");
                refrescarVisitas();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void refrescarVisitas() {
        aplicarPermisos();
        try {
            List<Visita> visitas = accesoService.listarVisitas();
            tablaModelo.setRowCount(0);
            for (Visita v : visitas) {
                tablaModelo.addRow(new Object[]{
                    v.getId(),
                    v.getPersonaNombre() + " (" + v.getPersonaDocumento() + ")",
                    v.getEmpresaDestinoNombre() != null ? v.getEmpresaDestinoNombre() : "-",
                    v.getEstado(),
                    v.getFechaHoraEntrada() != null ? v.getFechaHoraEntrada() : "-",
                    v.getFechaHoraSalida() != null ? v.getFechaHoraSalida() : "-"
                });
            }
        } catch (Exception e) {
            System.err.println("Error al cargar accesos: " + e.getMessage());
        }
    }
}
