package com.acme.sica.acceso.domain.event;

/**
 * Evento generado cuando ocurre una acción de acceso.
 * Parte del patrón Observer para notificar cambios en visitas.
 */
public class AccesoEvent {

    public enum TipoEvento {
        VISITA_PENDIENTE,       // Nueva visita pendiente de aprobación
        VISITA_APROBADA,        // Visita aprobada por funcionario
        VISITA_RECHAZADA,       // Visita rechazada por funcionario
        CHECK_IN,               // Persona ingresó al complejo
        CHECK_OUT,              // Persona salió del complejo
        SALIDA_OLVIDADA         // Salida no registrada detectada
    }

    private final TipoEvento tipo;
    private final Integer visitaId;
    private final Integer personaId;
    private final String mensaje;
    private final Integer empresaDestinoId;

    public AccesoEvent(TipoEvento tipo, Integer visitaId, Integer personaId,
                       String mensaje, Integer empresaDestinoId) {
        this.tipo = tipo;
        this.visitaId = visitaId;
        this.personaId = personaId;
        this.mensaje = mensaje;
        this.empresaDestinoId = empresaDestinoId;
    }

    public TipoEvento getTipo() { return tipo; }
    public Integer getVisitaId() { return visitaId; }
    public Integer getPersonaId() { return personaId; }
    public String getMensaje() { return mensaje; }
    public Integer getEmpresaDestinoId() { return empresaDestinoId; }

    @Override
    public String toString() {
        return "[" + tipo + "] " + mensaje;
    }
}
