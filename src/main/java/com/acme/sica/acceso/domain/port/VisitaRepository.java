package com.acme.sica.acceso.domain.port;

import com.acme.sica.acceso.domain.model.Visita;
import java.util.List;
import java.util.Optional;

public interface VisitaRepository {
    Optional<Visita> findById(int id);
    List<Visita> findAll();
    List<Visita> findByPersonaId(int personaId);
    List<Visita> findByEstado(String estado);
    List<Visita> findByEmpresaDestinoId(int empresaId);
    List<Visita> findPendientes();
    List<Visita> findPendientesByEmpresaId(int empresaId);
    List<Visita> findDentro();
    Optional<Visita> findVisitaAbierta(int personaId);
    Visita save(Visita visita);
    void updateEstado(int id, String estado, String observaciones);
    void registrarEntrada(int id, String fechaHoraEntrada);
    void registrarSalida(int id, String fechaHoraSalida);
    void updateFuncionario(int visitaId, int funcionarioId);
}
