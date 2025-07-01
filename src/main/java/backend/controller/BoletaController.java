package backend.controller;

import backend.modelo.*;
import backend.service.*;
import java.sql.Time;
import java.time.LocalDate;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

@RestController
@RequestMapping("/api/boleta")
@CrossOrigin(origins = "http://localhost:4200")
public class BoletaController {

    @Autowired
    private BoletaService boletaService;

    //http://localhost:8080/api/boleta/filtrar?codMoz=0003&horaInicio=10:00:00&horaFin=20:00:00&total1=50&total2=180&fechaInicio=2025-05-20&fechaFin=2025-05-21
    
    @GetMapping("/listar")
    public Page<Map<String, Object>> listarBoletasConFiltros(
            @RequestParam(required = false) String codMoz,
            @RequestParam(required = false) Float total1,
            @RequestParam(required = false) Float total2,
            @RequestParam(required = false) String horaInicio,
            @RequestParam(required = false) String horaFin,
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return boletaService.findBoletasConFiltros(codMoz, total1, total2, horaInicio, horaFin, fechaInicio, fechaFin, pageable);
    }

    @GetMapping("/detallebol/{codBol}")
    public List<Map<String, Object>> BoletaDetallePorCodigo(@PathVariable String codBol) {
        return boletaService.BoletaDetallePorCodigo(codBol);
    }

    @GetMapping("/detallecom/{codOr}")
    public List<Map<String, Object>> BoletaDetalleComidasCodigoOr(@PathVariable String codOr) {
        return boletaService.BoletaDetalleComidasCodigoOr(codOr);
    }

    @GetMapping("/contarfecha")
    public int obtenerTotalPorFecha(@RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return boletaService.contarBoletasPorFecha(fecha);
    }

}
