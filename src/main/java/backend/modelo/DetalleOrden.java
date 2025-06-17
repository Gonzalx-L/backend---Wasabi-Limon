package backend.modelo;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "detalle_orden",
       uniqueConstraints = @UniqueConstraint(columnNames = {"cod_com", "cod_or"})
)
public class DetalleOrden {

    @EmbeddedId
    private DetalleOrdenPK id;

    @ManyToOne
    @MapsId("codCom")
    @JoinColumn(name = "cod_com", nullable = false)
    private Comida comida;

    @ManyToOne
    @MapsId("codOr")
    @JoinColumn(name = "cod_or", nullable = false)
    private Orden orden;

    @Column(name = "can_dor", nullable = false)
    private int cantidad;

    // Clase embebida para la clave compuesta
    @Embeddable
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetalleOrdenPK implements java.io.Serializable {

        @Column(name = "cod_com")
        private String codCom;

        @Column(name = "cod_or")
        private String codOr;
    }
}
