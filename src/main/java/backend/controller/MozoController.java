package backend.controller;

import java.util.List;
import backend.modelo.Mozo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import backend.service.MozoService;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/mozo")
public class MozoController {
    
    @Autowired
    private MozoService mozoService;
    
    @GetMapping("/listar")
    public ResponseEntity<List<Map<String, Object>>> listarMozos() {
        List<Map<String, Object>> mozos = mozoService.findMozos();
        return ResponseEntity.ok(mozos);
    }
    
    @GetMapping("/listar/{id}")
    public ResponseEntity<?> obtenerMozoPorId(@PathVariable String id) {
        try {
            Map<String, Object> mozo = mozoService.findById(id);
            return ResponseEntity.ok(mozo);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    @PutMapping("/editar/{id}")
    public ResponseEntity<Map<String, String>> actualizarMozo(@PathVariable String id, @RequestBody Mozo mozoDetalles) {
        Map<String, String> response = new HashMap<>();
        try {
            mozoService.editarMozo(id, mozoDetalles);
            response.put("mensaje", "Mozo actualizado correctamente");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PostMapping("/agregar")
    public ResponseEntity<Map<String, String>> agregarMozo(@RequestBody Map<String, Object> body) {
        Map<String, String> response = new HashMap<>();
        try {
            String nomMoz = (String) body.get("nomMoz");
            String correoMoz = (String) body.get("correoMoz");
            String contraMoz = (String) body.get("contraMoz");
            String imgBase64 = (String) body.get("img1Moz"); // <-- base64 desde el frontend
            String codAdm = (String) body.get("cod_adm");            
            
            byte[] imgBytes = null;
            if (imgBase64 != null && !imgBase64.isEmpty()) {
                imgBytes = Base64.getDecoder().decode(imgBase64);
            }
            
            mozoService.agregarMozo(nomMoz, correoMoz, contraMoz, imgBytes, codAdm);
            response.put("mensaje", "Mozo agregado correctamente");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Map<String, String>> eliminarMozo(@PathVariable String id) {
        Map<String, String> response = new HashMap<>();
        try {
            mozoService.eliminarMozo(id);
            response.put("mensaje", "Mozo eliminado correctamente");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
}
