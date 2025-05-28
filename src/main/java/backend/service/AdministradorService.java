/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend.service;

import backend.dao.AdministradorRepository;
import backend.modelo.Administrador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdministradorService {
    @Autowired
    private AdministradorRepository administradorDAO;

    public Administrador login(String correo, String password) {
        return administradorDAO.findAll().stream()
            .filter(a -> a.getCorreoAdm().equalsIgnoreCase(correo) && a.getContraAdm().equals(password))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Credenciales inválidas para administrador"));
    }
}
