package backend.modelo;

import jakarta.persistence.*;
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(name = "tipo_pago")
public class TipoPago {

    @Id
    @Column(name = "cod_tipopago", length = 1)
    private String codTipopago;

    @Column(name = "nom_tipopago", length = 30)
    private String nomTipopago;
    
    @OneToMany(mappedBy = "tipboleta",  cascade = CascadeType.ALL) 
    private List<Boleta> tipboleta;
}
