package backend.controller;

import backend.modelo.Comida;
import backend.service.ComidaService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comida")
public class ComidaController {

    @Autowired
    private ComidaService comidaService;

    @PostMapping("/agregar")
    public ResponseEntity<Map<String, String>> agregarComida(@RequestBody Map<String, Object> body) {
        Map<String, String> response = new HashMap<>();
        try {
            String nom_com = (String) body.get("nomCom");
            float prec_nom = Float.parseFloat(body.get("precNom").toString());
            String desc_com = (String) body.get("descCom");
            String cod_cat = (String) body.get("cod_cat");

            comidaService.agregarComida(nom_com, prec_nom, desc_com, cod_cat);

            response.put("mensaje", "Comida ingresada correctamente");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/listar")
    public List<Map<String, Object>> findComida() {
        return comidaService.findComida();
    }

    @GetMapping("/listar/{id}")
    public ResponseEntity<?> obtenerComidaPorId(@PathVariable String id) {
        try {
            Map<String, Object> datosComida = comidaService.findById(id); 
            return ResponseEntity.ok(datosComida);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<Map<String, String>> actualizarComida(
            @PathVariable String id,
            @RequestBody Comida comidaDetalles) {

        Map<String, String> response = new HashMap<>();
        try {
            comidaService.actualizarComida(id, comidaDetalles);
            response.put("mensaje", "Comida actualizada correctamente");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Map<String, String>> eliminarComida(@PathVariable String id) {
        Map<String, String> response = new HashMap<>();
        try {
            comidaService.eliminarComida(id);
            response.put("mensaje", "Comida eliminada correctamente");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

}
