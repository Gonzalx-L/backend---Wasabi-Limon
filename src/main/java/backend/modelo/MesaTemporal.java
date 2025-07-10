package backend.modelo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
@Data
public class MesaTemporal {
    private String estado; // "LIBRE", "ATENDIDA"
    private String codMozo;
    private Map<String, Integer> pedidoTemporal;

    public MesaTemporal() {
        this.estado = "LIBRE";
        this.codMozo = null;
        this.pedidoTemporal = new HashMap<>();
    }

}
