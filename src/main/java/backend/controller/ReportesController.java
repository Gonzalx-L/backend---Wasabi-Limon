package backend.controller;

import backend.dao.ComprobanteRepository;
import backend.service.ReportesService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reportes")
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

    @GetMapping("/comprobantemesesfiltro/{codCompro}/{year}")
    public List<Map<String, Object>> obtenerComprobanteReportePorMeses(
            @PathVariable String codCompro,
            @PathVariable Integer year
    ) {
        return reportesService.reporteMensualPorComprobante(codCompro, year);
    }

    @GetMapping("/tipopagofiltro")
    public List<Map<String, Object>> obtenerTipoPagoReporte(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer day
    ) {
        return reportesService.obtenerTipoPagoReporte(year, month, day);
    }

    @GetMapping("/tipopagomesesfiltro/{codTipopago}/{year}")
    public List<Map<String, Object>> obtenerTipoPagoReportePorMeses(
            @PathVariable String codTipopago,
            @PathVariable Integer year
    ) {
        return reportesService.reporteMensualPorTipoPago(codTipopago, year);
    }

    @GetMapping("/comidafiltromayor")
    public List<Map<String, Object>> obtenerComidaReporteMayor(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer day
    ) {
        return reportesService.obtenerComidaReporteMayor(year, month, day);
    }

    @GetMapping("/comidafiltromenor")
    public List<Map<String, Object>> obtenerComidaReporteMenor(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer day
    ) {
        return reportesService.obtenerComidaReporteMenor(year, month, day);
    }

    @GetMapping("/comidamesesfiltro/{codCom}/{year}")
    public List<Map<String, Object>> obtenerComidaReportePorMeses(
            @PathVariable String codCom,
            @PathVariable Integer year
    ) {
        return reportesService.reporteMensualPorComida(codCom, year);
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
        return reportesService.ObtenerPropinaReporteFiltro(codMoz, year, month, day);
    }
}
