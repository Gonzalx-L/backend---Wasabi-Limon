package backend.controller;
import backend.dao.OrdenRepository;
import backend.dto.OrdenResumenDTO;
import backend.modelo.Orden;
import backend.service.OrdenService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/ordenes")
@CrossOrigin(origins = "http://localhost:4200")
public class OrdenController {

    private final OrdenService ordenService;
    private final OrdenRepository ordenRepository;

    public OrdenController(OrdenService ordenService, OrdenRepository ordenRepository) {
        this.ordenService = ordenService;
        this.ordenRepository = ordenRepository;
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

}
