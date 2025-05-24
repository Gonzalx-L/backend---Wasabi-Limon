package Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "comprobante")
public class Comprobante {

    @Id
    @Column(name = "cod_compro", length = 1)
    private String codCompro;

    @Column(name = "nom_compro", length = 30)
    private String nomCompro;
}