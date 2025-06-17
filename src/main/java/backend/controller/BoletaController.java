package backend.controller;

import backend.modelo.*;
import backend.service.*;
import java.sql.Time;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/boleta")
@CrossOrigin(origins = "http://http://localhost:4200")
public class BoletaController {

    @Autowired
    private BoletaService boletaService;

    @GetMapping("/listar")
    public List<Map<String, Object>> findBoletas() {
        return boletaService.findBoletas(null);
    }

    //http://localhost:8080/api/boleta/filtrar?codMoz=0003&horaInicio=10:00:00&horaFin=20:00:00&total1=50&total2=180&fechaInicio=2025-05-20&fechaFin=2025-05-21
    @GetMapping("/filtrar")
    public List<Map<String, Object>> findBoletasFiltradas(
            @RequestParam(required = false) String codMoz,
            @RequestParam(required = false) Float total1,
            @RequestParam(required = false) Float total2,
            @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm:ss") String horaInicio,
            @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm:ss") String horaFin,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") String fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") String fechaFin
    ) {
        return boletaService.findBoletasFiltradas(codMoz, total1, total2, horaInicio, horaFin, fechaInicio, fechaFin);
    }

    @GetMapping("/detallebol/{codBol}")
    public List<Map<String, Object>> BoletaDetallePorCodigo(@PathVariable String codBol) {
        return boletaService.BoletaDetallePorCodigo(codBol);
    }

    @GetMapping("/detallecom/{codOr}")
    public List<Map<String, Object>> BoletaDetalleComidasCodigoOr(@PathVariable String codOr) {
        return boletaService.BoletaDetalleComidasCodigoOr(codOr);
    }
}
