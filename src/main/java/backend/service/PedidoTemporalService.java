package backend.service;

import backend.dao.ComidaRepository;
import backend.dto.ResumenPedidoDTO;
import backend.modelo.Comida;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PedidoTemporalService {

    private final Map<String, Integer> pedidoTemporal = new HashMap<>();
    private final ComidaRepository comidaRepository;

    public PedidoTemporalService(ComidaRepository comidaRepository) {
        this.comidaRepository = comidaRepository;
    }

    public void agregarPlato(String codCom) {
        pedidoTemporal.merge(codCom, 1, Integer::sum);
    }

    public void quitarPlato(String codCom) {
        if (pedidoTemporal.containsKey(codCom)) {
            int cantidad = pedidoTemporal.get(codCom);
            if (cantidad <= 1) {
                pedidoTemporal.remove(codCom);
            } else {
                pedidoTemporal.put(codCom, cantidad - 1);
            }
        }
    }

    public Map<String, Integer> obtenerPedidoCrudo() {
        return pedidoTemporal;
    }

    // Nuevo método para obtener un resumen detallado
    public List<ResumenPedidoDTO> obtenerResumenPedido() {
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

    public void limpiarPedido() {
        pedidoTemporal.clear();
    }
}
