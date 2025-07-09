package backend.dto;
import lombok.Data;
import java.util.Map;

@Data
public class MesaTemporalDTO {
    private String estado;
    private String codMozo;
    private Map<String, Integer> pedidoTemporal;

    public MesaTemporalDTO(String estado, String codMozo, Map<String, Integer> pedidoTemporal) {
        this.estado = estado;
        this.codMozo = codMozo;
        this.pedidoTemporal = pedidoTemporal;
    }
}
