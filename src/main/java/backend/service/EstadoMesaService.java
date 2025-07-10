package backend.service;

import backend.modelo.MesaTemporal;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class EstadoMesaService {

    private final Map<Integer, MesaTemporal> mesas = new HashMap<>();

    public EstadoMesaService() {
        for (int i = 1; i <= 10; i++) {
            mesas.put(i, new MesaTemporal());
        }
    }

    public Map<Integer, MesaTemporal> obtenerTodas() {
        return mesas;
    }

    public MesaTemporal obtenerMesa(int numero) {
        return mesas.get(numero);
    }

    public void atenderMesa(int numero, String codMozo) {
        MesaTemporal mesa = mesas.get(numero);
        mesa.setEstado("ATENDIDA");
        mesa.setCodMozo(codMozo);
    }

    public void liberarMesa(int numero) {
        MesaTemporal mesa = mesas.get(numero);
        mesa.setEstado("LIBRE");
        mesa.setCodMozo(null);
        mesa.getPedidoTemporal().clear();
    }

    public void agregarPlato(int numero, String codCom) {
        Map<String, Integer> pedido = mesas.get(numero).getPedidoTemporal();
        pedido.merge(codCom, 1, Integer::sum);
    }

    public void quitarPlato(int numero, String codCom) {
        Map<String, Integer> pedido = mesas.get(numero).getPedidoTemporal();
        if (pedido.containsKey(codCom)) {
            int cantidad = pedido.get(codCom);
            if (cantidad <= 1) {
                pedido.remove(codCom);
            } else {
                pedido.put(codCom, cantidad - 1);
            }
        }
    }

    public void limpiarPedido(int numero) {
        mesas.get(numero).getPedidoTemporal().clear();
    }
}
