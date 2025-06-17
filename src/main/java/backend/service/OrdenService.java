package backend.service;

import backend.dao.*;
import backend.dto.OrdenResumenDTO;
import backend.modelo.Comida;
import backend.modelo.DetalleOrden;
import backend.modelo.DetalleOrden.DetalleOrdenPK;
import backend.modelo.Mozo;
import backend.modelo.Orden;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalTime;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class OrdenService {

    private final PedidoTemporalService pedidoTemporalService;
    private final OrdenRepository ordenRepository;
    private final DetalleOrdenRepository detalleOrdenRepository;
    private final ComidaRepository comidaRepository;
    private final MozoRepository mozoRepository;

    public OrdenService(PedidoTemporalService pedidoTemporalService,
                        OrdenRepository ordenRepository,
                        DetalleOrdenRepository detalleOrdenRepository,
                        ComidaRepository comidaRepository,
                        MozoRepository mozoRepository) {
        this.pedidoTemporalService = pedidoTemporalService;
        this.ordenRepository = ordenRepository;
        this.comidaRepository = comidaRepository;
        this.detalleOrdenRepository = detalleOrdenRepository;
        this.mozoRepository = mozoRepository;
    }

    public void confirmarPedido() {
        // 1. Obtener los platos del pedido temporal
        Map<String, Integer> pedido = pedidoTemporalService.obtenerPedidoCrudo();
        if (pedido.isEmpty()) return;

        // 2. Crear orden
        Orden orden = new Orden();
        String codOr = UUID.randomUUID().toString().substring(0, 10); // código aleatorio
        orden.setCodOr(codOr);
        orden.setMesa(1); // puedes ajustar esto dinámicamente
        orden.setHora(Time.valueOf(LocalTime.now()));

        // 3. Asociar mozo "0001"
        Mozo mozo = mozoRepository.findById("0001").orElse(null);
        orden.setMozo(mozo);
        orden.setCodMoz("0001");

        // 4. Crear detalles
        List<DetalleOrden> detalles = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : pedido.entrySet()) {
            String codCom = entry.getKey();
            int cantidad = entry.getValue();

            Comida comida = comidaRepository.findById(codCom).orElse(null);
            if (comida != null) {
                DetalleOrden detalle = new DetalleOrden();

                // Establecer clave compuesta correctamente
                DetalleOrdenPK pk = new DetalleOrdenPK(codCom, codOr);
                detalle.setId(pk);

                detalle.setOrden(orden);
                detalle.setComida(comida);
                detalle.setCantidad(cantidad);
                detalles.add(detalle);
            }
        }

        orden.setDetalles(detalles);

        // 5. Guardar orden y detalles (
        ordenRepository.save(orden);

        // 6. Limpiar pedido temporal
        pedidoTemporalService.limpiarPedido();
    }

    public List<OrdenResumenDTO> listarOrdenesPorMozo(String codMoz) {
        List<Orden> ordenes = ordenRepository.findByCodMoz(codMoz);
        List<OrdenResumenDTO> resumenes = new ArrayList<>();

        for (Orden orden : ordenes) {
            List<OrdenResumenDTO.DetalleDTO> detalles = new ArrayList<>();
            float total = 0;

            for (DetalleOrden det : orden.getDetalles()) {
                Comida comida = det.getComida();
                float precio = comida.getPrecNom();
                int cantidad = det.getCantidad();
                float subtotal = precio * cantidad;
                total += subtotal;

                detalles.add(new OrdenResumenDTO.DetalleDTO(
                        comida.getCodCom(),
                        comida.getNomCom(),
                        precio,
                        cantidad,
                        subtotal
                ));
            }

            resumenes.add(new OrdenResumenDTO(
                    orden.getCodOr(),
                    orden.getMesa(),
                    orden.getHora().toString(),
                    total,
                    detalles
            ));
        }

        return resumenes;
    }


}
