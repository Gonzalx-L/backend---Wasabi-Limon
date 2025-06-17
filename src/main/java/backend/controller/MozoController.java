package backend.controller;

import java.util.List;
import backend.modelo.Mozo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import backend.service.MozoService;
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
@CrossOrigin(origins = "http://http://localhost:4200")
public class MozoController {

    @Autowired
    private MozoService mozoService;

    @GetMapping("/listar")
    public List<Mozo> findMozos() {
        return mozoService.findMozos();
    }

    @GetMapping("/listar2")
    public List<Map<String, Object>> findMozoSinImg() {
        return mozoService.findMozoSinImg();
    }

    @GetMapping("/listar/{id}")
    public ResponseEntity<?> obtenerMozoPorId(@PathVariable String id) {
        try {
            Mozo mozo = mozoService.findById(id);
            return ResponseEntity.ok(mozo);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<Map<String, String>> actualizarMozo(@PathVariable String id, @RequestBody Mozo mozoDetalles) {
        mozoService.actualizarMozo(id, mozoDetalles);
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Mozo actualizado correctamente");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/agregar")
    public ResponseEntity<Map<String, String>> guardarMozo(@RequestBody Mozo mozo) {
        Map<String, String> response = new HashMap<>();
        try {
            mozoService.guardarMozo(mozo);
            response.put("mensaje", "Mozo ingresado correctamente");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

}
