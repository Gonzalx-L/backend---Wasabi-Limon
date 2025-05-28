package backend.controller;

import backend.dao.OrdenRepository;
import backend.dto.OrdenResumenDTO;
import backend.dto.ResumenPedidoDTO;
import backend.modelo.Orden;
import backend.service.OrdenService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/ordenes")
@CrossOrigin(origins = "http://localhost:4200")
public class OrdenController {

    private final OrdenService ordenService;
    private final OrdenRepository ordenRepository;

    //  Esto permite que podamos usar los métodos de ambos tsnto de repository y service
    // dentro de esta clase para manejar las solicitudes relacionadas con órdenes.
    public OrdenController(OrdenService ordenService, OrdenRepository ordenRepository) {
        this.ordenService = ordenService;             // Servicio para para confirmar y listar órdenes con detalle
        this.ordenRepository = ordenRepository;       // Repositorio cxnsulta la base de datos para consultar órdenes
    }

    // Confirmar el pedido
    @PostMapping("/confirmar")
    public void confirmarOrden() {
        ordenService.confirmarPedido();
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
}
