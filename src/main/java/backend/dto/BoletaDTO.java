
package backend.dto;

import lombok.Data;

@Data
public class BoletaDTO {
    private String codOr;
    private Integer tipoPago; // 1, 2, 3
    private Float propina;
    private String nomCli;
    private String dniCli;
    private String rucCli;
    private String numCli;
    private String correoCli;  
}
