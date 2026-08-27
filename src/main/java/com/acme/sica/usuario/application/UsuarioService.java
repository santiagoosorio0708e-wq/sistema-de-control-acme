package com.acme.sica.usuario.application;

import com.acme.sica.auditoria.application.AuditoriaService;
import com.acme.sica.shared.domain.exception.EntidadNoEncontradaException;
import com.acme.sica.shared.security.AutorizacionService;
import com.acme.sica.usuario.domain.model.Usuario;
import com.acme.sica.usuario.domain.port.UsuarioRepository;

import java.util.List;

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final AutorizacionService autorizacionService;
    private final AuditoriaService auditoriaService;

    public UsuarioService(UsuarioRepository usuarioRepository, 
                          AutorizacionService autorizacionService, 
                          AuditoriaService auditoriaService) {
        this.usuarioRepository = usuarioRepository;
        this.autorizacionService = autorizacionService;
        this.auditoriaService = auditoriaService;
    }

    public List<Usuario> listarUsuarios() {
        autorizacionService.verificarPermiso("gestionar_usuarios");
        return usuarioRepository.findAll();
    }

    public Usuario obtenerUsuario(int id) {
        autorizacionService.verificarPermiso("gestionar_usuarios");
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Usuario", id));
    }

    public Usuario crearUsuario(Usuario usuario, String rawPassword) {
        autorizacionService.verificarPermiso("gestionar_usuarios");
        
        // Hashear contraseña
        usuario.setPasswordHash(AuthService.hashPassword(rawPassword));
        usuario.setActivo(true);
        
        Usuario guardado = usuarioRepository.save(usuario);
        
        auditoriaService.registrarAuditoria(
                "CREAR", "USUARIOS", guardado.getId(), 
                "Usuario creado: " + guardado.getUsername()
        );
        
        return guardado;
    }

    public void actualizarUsuario(int id, Usuario actualizacion) {
        autorizacionService.verificarPermiso("gestionar_usuarios");
        
        Usuario existente = obtenerUsuario(id);
        existente.setUsername(actualizacion.getUsername());
        existente.setNombreCompleto(actualizacion.getNombreCompleto());
        existente.setRolId(actualizacion.getRolId());
        existente.setActivo(actualizacion.isActivo());
        
        usuarioRepository.update(existente);
        
        auditoriaService.registrarAuditoria(
                "ACTUALIZAR", "USUARIOS", id, 
                "Usuario actualizado: " + existente.getUsername()
        );
    }
}
