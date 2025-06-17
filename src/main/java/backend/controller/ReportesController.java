package backend.controller;

import backend.dao.ComprobanteRepository;
import backend.service.ReportesService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "http://http://localhost:4200")
public class ReportesController {
    
    @Autowired
    private ReportesService reportesService;
    
    
    //http://localhost:8080/api/reportes/comprobante?year=2025&month=5&day=25
    @GetMapping("/comprobante")
    public List<Map<String, Object>> obtenerComprobantesReporte(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer day
    ) {
        return reportesService.obtenerComprobantesReporte(year, month, day);
    }
    
    @GetMapping("/tipopagofiltro")
    public List<Map<String, Object>> obtenerTipoPagoReporte(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer day
    ) {
        return reportesService.obtenerTipoPagoReporte(year, month, day);
    }
    
    @GetMapping("/comidafiltro")
    public List<Map<String, Object>> obtenerComidaReporte(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer day
    ) {
        return reportesService.obtenerComidaReporte(year, month, day);
    }
    
    @GetMapping("/ingresofiltro")
    public List<Map<String, Object>> ObtenerIngresosReporteFiltro(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer day
    ) {
        return reportesService.ObtenerIngresosReporteFiltro(year, month, day);
    }
    
    @GetMapping("/propinafiltro")
    public List<Map<String, Object>> ObtenerPropinaReporteFiltro(
            @RequestParam(required = false) String codMoz,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer day
    ) {
        return reportesService.ObtenerPropinaReporteFiltro(codMoz,year, month, day);
    }
}
