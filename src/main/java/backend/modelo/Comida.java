package backend.modelo;

import jakarta.persistence.*;
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(name = "comida")
public class Comida {

    @Id
    @Column(name = "cod_com", length = 4)
    private String codCom;

    @Column(name = "nom_com", length = 50, nullable = false)
    private String nomCom;

    @Column(name = "prec_nom", nullable = false)
    private float precNom;

    @Column(name = "desc_com", length = 1000, nullable = false)
    private String descCom;

    @OneToMany(mappedBy = "comida")
    private List<DetalleOrden> detalles;

    @ManyToMany(mappedBy = "comidas")
    private List<Categoria> categorias;
}