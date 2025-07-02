package backend.controller;

import backend.dto.ResumenPedidoDTO;
import backend.service.PedidoTemporalService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedido-temporal")
public class PedidoTemporalController {

    private final PedidoTemporalService pedidoService;

    public PedidoTemporalController(PedidoTemporalService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping("/agregar/{codCom}")
    public void agregarPlato(@PathVariable String codCom) {
        pedidoService.agregarPlato(codCom);
    }

    @PostMapping("/quitar/{codCom}")
    public void quitarPlato(@PathVariable String codCom) {
        pedidoService.quitarPlato(codCom);
    }

    @GetMapping
    public Map<String, Integer> verPedidoActual() {
        return pedidoService.obtenerPedidoCrudo();
    }


    @DeleteMapping
    public void limpiarPedido() {
        pedidoService.limpiarPedido();
    }

    @GetMapping("/resumen")
    public List<ResumenPedidoDTO> verResumenDetallado() {
        return pedidoService.obtenerResumenPedido();
    }

    /**
     * Devuelve total de ítems en el carrito
     */
    @GetMapping("/count")
    public int contarItems() {
        return pedidoService.obtenerPedidoCrudo()
                .values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

}
