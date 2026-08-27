package com.acme.sica.usuario.application;

import com.acme.sica.shared.domain.exception.AccesoDenegadoException;
import com.acme.sica.shared.security.SesionActual;
import com.acme.sica.usuario.domain.model.Usuario;
import com.acme.sica.usuario.domain.port.UsuarioRepository;
import com.acme.sica.auditoria.application.AuditoriaService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

/**
 * Servicio de aplicación para la autenticación de usuarios.
 */
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final AuditoriaService auditoriaService;

    public AuthService(UsuarioRepository usuarioRepository, AuditoriaService auditoriaService) {
        this.usuarioRepository = usuarioRepository;
        this.auditoriaService = auditoriaService;
    }

    /**
     * Autentica a un usuario y establece la sesión actual.
     *
     * @param username el nombre de usuario
     * @param password la contraseña en texto plano
     * @return el usuario autenticado
     * @throws AccesoDenegadoException si las credenciales son inválidas
     */
    public Usuario login(String username, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);

        if (usuarioOpt.isEmpty()) {
            throw new AccesoDenegadoException("Credenciales inválidas", "N/A");
        }

        Usuario usuario = usuarioOpt.get();

        if (!usuario.isActivo()) {
            throw new AccesoDenegadoException("Usuario inactivo. Contacte al administrador.", "N/A");
        }

        String hashedInputPassword = hashPassword(password);
        if (!usuario.getPasswordHash().equals(hashedInputPassword)) {
            throw new AccesoDenegadoException("Credenciales inválidas", "N/A");
        }

        SesionActual.setUsuario(usuario);
        
        auditoriaService.registrarAuditoria(
            "LOGIN",
            "USUARIOS",
            usuario.getId(),
            "Inicio de sesión exitoso desde web"
        );

        return usuario;
    }

    /**
     * Cierra la sesión del usuario actual.
     */
    public void logout() {
        Usuario usuario = SesionActual.getUsuario();
        if (usuario != null) {
            auditoriaService.registrarAuditoria(
                "LOGOUT",
                "USUARIOS",
                usuario.getId(),
                "Cierre de sesión desde web"
            );
            SesionActual.cerrarSesion();
        }
    }

    /**
     * Utilidad para encriptar contraseñas usando SHA-256.
     * En producción se recomendaría BCrypt, pero SHA-256 es suficiente
     * para este proyecto educativo.
     */
    public static String hashPassword(String plainPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(plainPassword.getBytes());
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No se encontró el algoritmo de encriptación", e);
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
