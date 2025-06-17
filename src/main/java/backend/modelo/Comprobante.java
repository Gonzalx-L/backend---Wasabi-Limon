package backend.modelo;

import jakarta.persistence.*;
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(name = "comprobante")
public class Comprobante {

    @Id
    @Column(name = "cod_compro", length = 1)
    private String codCompro;

    @Column(name = "nom_compro", length = 30)
    private String nomCompro;
    
    @OneToMany(mappedBy = "coboleta", cascade = CascadeType.ALL) 
    private List<Boleta> coboleta;
}