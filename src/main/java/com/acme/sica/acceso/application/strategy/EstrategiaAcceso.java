package com.acme.sica.acceso.application.strategy;

import com.acme.sica.acceso.domain.model.Visita;
import com.acme.sica.persona.domain.model.Persona;

/**
 * Strategy Pattern — Interfaz base para los diferentes flujos de acceso.
 * Cada tipo de acceso (pre-registrado, no anunciado, etc.)
 * implementará esta interfaz para procesar el ingreso.
 */
public interface EstrategiaAcceso {
    
    /**
     * Procesa un intento de acceso al complejo.
     * 
     * @param persona La persona que intenta ingresar
     * @param visita La visita asociada (puede ser nueva o existente)
     * @return true si el acceso fue concedido inmediatamente, false si requiere aprobación o fue denegado
     */
    boolean procesarAcceso(Persona persona, Visita visita);
}
