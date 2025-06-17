package backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import backend.modelo.*;
import backend.service.*;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/admi")
@CrossOrigin(origins = "http://http://localhost:4200")
public class AdministradorController {
    
    @Autowired
    private AdministradorService administradorService;
    
    @GetMapping("/listar")
    public List<Administrador> findAdmi() {
        return administradorService.findAdmi(null);
    }
}
