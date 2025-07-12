package backend.controller;
import backend.dao.OrdenRepository;
import backend.dto.BoletaDTO;
import backend.dto.OrdenResumenDTO;
import backend.dto.ResumenPedidoDTO;
import backend.modelo.Orden;
import backend.service.BoletaService;
import backend.service.OrdenService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {

    private final OrdenService ordenService;
    private final OrdenRepository ordenRepository;

    @Autowired
    private final BoletaService boletaService;
    
    public OrdenController(OrdenService ordenService, OrdenRepository ordenRepository, BoletaService boletaService) {
        this.ordenService = ordenService;
        this.ordenRepository = ordenRepository;
        this.boletaService = boletaService;
    }
    // Confirmar el pedido
    @PostMapping("/confirmar")
    public void confirmarOrden(@RequestParam int numeroMesa, @RequestParam String codMozo) {
        ordenService.confirmarPedido(numeroMesa, codMozo);
    }
    // Listar por mozo
    @GetMapping("/mozo/{codMoz}")
    public List<Orden> listarOrdenesPorMozo(@PathVariable String codMoz) {
        return ordenRepository.findByCodMoz(codMoz);
    }
    @GetMapping("/resumen/mozo/{codMoz}")
    public List<OrdenResumenDTO> verResumenPorMozo(@PathVariable String codMoz) {
        return ordenService.listarOrdenesPorMozo(codMoz);
    }

    @GetMapping("/resumen/mozo/{codMoz}/estado/{estado}")
    public List<OrdenResumenDTO> listarPorMozoYEstado(@PathVariable String codMoz, @PathVariable String estado) {
        return ordenService.listarOrdenesPorMozoYEstado(codMoz, estado);
    }

    @PutMapping("/mozo/{codOr}/editar")
    public ResponseEntity<?> editarOrden(
            @PathVariable String codOr,
            @RequestBody List<ResumenPedidoDTO> nuevosDetalles) {
        ordenService.editarOrden(codOr, nuevosDetalles);
        return ResponseEntity.ok(Map.of("message", "Orden actualizada correctamente"));
    }
    @PutMapping("/pagar/{codOr}")
    public void marcarComoPagado(@PathVariable String codOr) {
        ordenService.marcarComoPagado(codOr);
    }

    @PutMapping("/anular/{codOr}")
    public void anularOrden(@PathVariable String codOr) {
        ordenService.anularOrden(codOr);
    }
    
    @PostMapping("/generar")
    public ResponseEntity<?> generarBoleta(@RequestBody BoletaDTO dto) {
        try {
            boletaService.generarBoleta(dto);
            return ResponseEntity.ok(Map.of("message", "Boleta generada con éxito"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }
}
