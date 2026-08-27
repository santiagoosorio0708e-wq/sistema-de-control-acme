package com.acme.sica.persona.domain.port;

import com.acme.sica.persona.domain.model.Persona;
import java.util.List;
import java.util.Optional;

public interface PersonaRepository {
    Optional<Persona> findById(int id);
    Optional<Persona> findByDocumento(String documento);
    List<Persona> findAll();
    List<Persona> findByTipoPersona(String tipoPersona);
    List<Persona> findByEmpresaId(int empresaId);
    Persona save(Persona persona);
    void update(Persona persona);
    void delete(int id);
}
