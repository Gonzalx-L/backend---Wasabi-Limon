/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend.controller;

import backend.dto.MozoDTO;
import backend.modelo.Administrador;
import backend.modelo.Mozo;
import backend.service.AdministradorService;
import backend.service.MozoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private MozoService mozoService;

    @Autowired
    private AdministradorService administradorService;

    @PostMapping("/login-mozo")
    public ResponseEntity<?> loginMozo(@RequestBody LoginRequest request) {
        try {
            Mozo mozo = mozoService.login(request.getCorreo(), request.getPassword());
            return ResponseEntity.ok(new MozoDTO(mozo));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    @PostMapping("/login-admin")
    public Administrador loginAdmin(@RequestBody LoginRequest request) {
        return administradorService.login(request.getCorreo(), request.getPassword());
    }

    public static class LoginRequest {
        private String correo;
        private String password;

        public String getCorreo() {
            return correo;
        }

        public void setCorreo(String correo) {
            this.correo = correo;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
