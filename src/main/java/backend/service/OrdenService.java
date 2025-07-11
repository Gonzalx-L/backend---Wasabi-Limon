package backend.service;

import backend.dao.*;
import backend.dto.OrdenResumenDTO;
import backend.dto.ResumenPedidoDTO;
import backend.modelo.Comida;
import backend.modelo.DetalleOrden;
import backend.modelo.DetalleOrden.DetalleOrdenPK;
import backend.modelo.Mozo;
import backend.modelo.Orden;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class OrdenService {

    private final PedidoTemporalService pedidoTemporalService;
    private final OrdenRepository ordenRepository;
    private final DetalleOrdenRepository detalleOrdenRepository;
    private final ComidaRepository comidaRepository;
    private final MozoRepository mozoRepository;
    private final BoletaRepository boletaRepository;

    public OrdenService(PedidoTemporalService pedidoTemporalService,
                        OrdenRepository ordenRepository,
                        DetalleOrdenRepository detalleOrdenRepository,
                        ComidaRepository comidaRepository,
                        MozoRepository mozoRepository, 
                        BoletaRepository boletaRepository) {
        this.pedidoTemporalService = pedidoTemporalService;
        this.ordenRepository = ordenRepository;
        this.comidaRepository = comidaRepository;
        this.detalleOrdenRepository = detalleOrdenRepository;
        this.mozoRepository = mozoRepository;
        this.boletaRepository = boletaRepository;
    }

public void confirmarPedido(int numeroMesa, String codMozo) {
    Map<String, Integer> pedido = pedidoTemporalService.obtenerPedidoCrudoPorMesa(numeroMesa);
    if (pedido.isEmpty()) return;

    Orden orden = new Orden();
    String codOr = UUID.randomUUID().toString().substring(0, 10);
    orden.setCodOr(codOr);
    orden.setMesa(numeroMesa);
    orden.setHora(Time.valueOf(LocalTime.now()));
    orden.setEstado("PENDIENTE");

    Mozo mozo = mozoRepository.findById(codMozo).orElse(null);
    orden.setMozo(mozo);
    orden.setCodMoz(codMozo);

    List<DetalleOrden> detalles = new ArrayList<>();
    for (Map.Entry<String, Integer> entry : pedido.entrySet()) {
        String codCom = entry.getKey();
        int cantidad = entry.getValue();

        Comida comida = comidaRepository.findById(codCom).orElse(null);
        if (comida != null) {
            DetalleOrden detalle = new DetalleOrden();
            DetalleOrdenPK pk = new DetalleOrdenPK(codCom, codOr);
            detalle.setId(pk);
            detalle.setOrden(orden);
            detalle.setComida(comida);
            detalle.setCantidad(cantidad);
            detalles.add(detalle);
        }
    }

    orden.setDetalles(detalles);
    ordenRepository.save(orden);

    // Limpiar solo el pedido de esa mesa
    pedidoTemporalService.limpiarPedidoPorMesa(numeroMesa);
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
    
    public List<OrdenResumenDTO> listarOrdenesPorMozoYEstado(String codMoz, String estado) {
    List<Orden> ordenes = ordenRepository.findByCodMozAndEstado(codMoz, estado);
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

    public void editarOrden(String codOr, List<ResumenPedidoDTO> nuevosDetalles) {
        Orden orden = ordenRepository.findById(codOr)
            .orElseThrow(() -> new RuntimeException("Orden no encontrada."));

        if (!orden.getEstado().equals("PENDIENTE")) {
            throw new RuntimeException("No se puede editar esta orden.");
        }

        // Eliminar detalles actuales
        detalleOrdenRepository.deleteAll(orden.getDetalles());
        orden.getDetalles().clear();

        // Agregar nuevos detalles
        List<DetalleOrden> nuevos = nuevosDetalles.stream().map(dto -> {
            DetalleOrdenPK pk = new DetalleOrdenPK(dto.getCodCom(), codOr);

            DetalleOrden det = new DetalleOrden();
            det.setId(pk);
            det.setCantidad(dto.getCantidad());
            det.setOrden(orden);
            det.setComida(comidaRepository.findById(dto.getCodCom())
                .orElseThrow(() -> new RuntimeException("Comida no encontrada: " + dto.getCodCom())));

            return det;
        }).collect(Collectors.toList());
        orden.setDetalles(nuevos);
        ordenRepository.save(orden);
        }

    
    public void marcarComoPagado(String codOr) {
        Orden orden = ordenRepository.findById(codOr).orElseThrow();
        orden.setEstado("PAGADO");
        ordenRepository.save(orden);
    }

    public void anularOrden(String codOr) {
        Orden orden = ordenRepository.findById(codOr).orElseThrow();
        orden.setEstado("ANULADO");
        ordenRepository.save(orden);
    }
    
    public float calcularTotalOrden(Orden orden) {
        float total = 0;
        for (DetalleOrden det : orden.getDetalles()) {
            float precio = det.getComida().getPrecNom();
            int cantidad = det.getCantidad();
            total += precio * cantidad;
        }
        return total;
    }
}
