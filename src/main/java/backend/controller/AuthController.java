/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend.controller;

import backend.jwt.Jwt;
import backend.modelo.Administrador;
import backend.modelo.Mozo;
import backend.security.LoginRequest;
import backend.security.UsuarioDetailsService;
import backend.service.AdministradorService;
import backend.service.MozoService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private MozoService mozoService;

    @Autowired
    private AdministradorService administradorService;
    
    @Autowired
    private UsuarioDetailsService usuarioDetailsService;

    
    @Autowired
    private Jwt jwtUtil;

    @PostMapping("/login-mozo")
    public ResponseEntity<?> loginMozo(@RequestBody LoginRequest request) {
        try {
            Mozo mozo = mozoService.login(request.getCorreo(), request.getPassword());

            // carga UserDetails con los roles
            UserDetails userDetails = usuarioDetailsService.loadUserByUsername(mozo.getCorreoMoz());

            //generar el token (con roles correctos)
            String token = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok(Map.of("token", token, "role", "MOZO"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    @PostMapping("/login-admin")
    public ResponseEntity<?> loginAdmin(@RequestBody LoginRequest request) {
        try {
            Administrador admin = administradorService.login(request.getCorreo(), request.getPassword());

            UserDetails userDetails = usuarioDetailsService.loadUserByUsername(admin.getCorreoAdm());
            String token = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok(Map.of("token", token, "role", "ADMIN"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }
@PostMapping("/logout")
public ResponseEntity<?> logout(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        return ResponseEntity.badRequest().body("No se encontró el token");
    }
    String token = authHeader.substring(7);

    return ResponseEntity.ok("Sesión cerrada correctamente");
}

}
