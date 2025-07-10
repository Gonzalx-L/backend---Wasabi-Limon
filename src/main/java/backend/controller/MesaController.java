package backend.controller;

import backend.dto.MesaTemporalDTO;
import backend.dto.ResumenPedidoDTO;
import backend.modelo.Comida;
import backend.service.EstadoMesaService;
import backend.dao.ComidaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/mesas")
public class MesaController {

    private final EstadoMesaService estadoMesaService;
    private final ComidaRepository comidaRepository;

    // Constructor con ambos servicios/repositorios
    public MesaController(EstadoMesaService estadoMesaService, ComidaRepository comidaRepository) {
        this.estadoMesaService = estadoMesaService;
        this.comidaRepository = comidaRepository;
    }

    // 1. Ver estados de todas las mesas
    @GetMapping("/estados")
    public Map<Integer, MesaTemporalDTO> listarEstados() {
        Map<Integer, MesaTemporalDTO> respuesta = new HashMap<>();
        estadoMesaService.obtenerTodas().forEach((n, m) -> {
            respuesta.put(n, new MesaTemporalDTO(m.getEstado(), m.getCodMozo(), m.getPedidoTemporal()));
        });
        return respuesta;
    }

    // 2. Atender una mesa
    @PostMapping("/{numero}/atender")
    public void atenderMesa(@PathVariable int numero, @RequestParam String codMozo) {
        estadoMesaService.atenderMesa(numero, codMozo);
    }

    // 3. Liberar una mesa
    @PostMapping("/{numero}/liberar")
    public void liberarMesa(@PathVariable int numero) {
        estadoMesaService.liberarMesa(numero);
    }

    // 4. Agregar un plato al carrito de una mesa
    @PostMapping("/{numero}/carrito/agregar/{codCom}")
    public void agregarPlato(@PathVariable int numero, @PathVariable String codCom) {
        estadoMesaService.agregarPlato(numero, codCom);
    }

    // 5. Quitar un plato del carrito de una mesa
    @PostMapping("/{numero}/carrito/quitar/{codCom}")
    public void quitarPlato(@PathVariable int numero, @PathVariable String codCom) {
        estadoMesaService.quitarPlato(numero, codCom);
    }

    // 6. Ver pedido crudo (codCom - cantidad)
    @GetMapping("/{numero}/carrito")
    public Map<String, Integer> verPedido(@PathVariable int numero) {
        return estadoMesaService.obtenerMesa(numero).getPedidoTemporal();
    }

    // 7. Limpiar carrito de una mesa
    @DeleteMapping("/{numero}/carrito")
    public void limpiarCarrito(@PathVariable int numero) {
        estadoMesaService.limpiarPedido(numero);
    }

    // 8. Ver resumen detallado del carrito de una mesa
    @GetMapping("/{numero}/carrito/resumen")
    public List<ResumenPedidoDTO> getResumenPedido(@PathVariable int numero) {
        Map<String, Integer> pedidoTemporal = estadoMesaService.obtenerMesa(numero).getPedidoTemporal();
        List<ResumenPedidoDTO> resumen = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : pedidoTemporal.entrySet()) {
            String codCom = entry.getKey();
            int cantidad = entry.getValue();
            Optional<Comida> optComida = comidaRepository.findById(codCom);
            if (optComida.isPresent()) {
                Comida comida = optComida.get();
                float precio = comida.getPrecNom();
                float subtotal = precio * cantidad;
                resumen.add(new ResumenPedidoDTO(
                        codCom,
                        comida.getNomCom(),
                        precio,
                        cantidad,
                        subtotal
                ));
            }
        }
        return resumen;
    }
}
