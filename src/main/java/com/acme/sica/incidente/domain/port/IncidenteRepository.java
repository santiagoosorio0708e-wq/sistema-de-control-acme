package com.acme.sica.incidente.domain.port;

import com.acme.sica.incidente.domain.model.Incidente;
import java.util.List;
import java.util.Optional;

public interface IncidenteRepository {
    Optional<Incidente> findById(int id);
    List<Incidente> findAll();
    List<Incidente> findByPersonaId(int personaId);
    List<Incidente> findByEstado(String estado);
    Incidente save(Incidente incidente);
    void updateEstado(int id, String estado);
}
