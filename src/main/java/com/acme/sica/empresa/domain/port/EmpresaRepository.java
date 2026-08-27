package com.acme.sica.empresa.domain.port;

import com.acme.sica.empresa.domain.model.Empresa;
import java.util.List;
import java.util.Optional;

public interface EmpresaRepository {
    Optional<Empresa> findById(int id);
    Optional<Empresa> findByNit(String nit);
    List<Empresa> findAll();
    List<Empresa> findActivas();
    Empresa save(Empresa empresa);
    void update(Empresa empresa);
    void delete(int id);
}
