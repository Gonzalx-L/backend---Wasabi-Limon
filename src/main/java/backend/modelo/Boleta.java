package backend.modelo;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Data;

@Data
@Entity
@Table(name = "boleta")
public class Boleta {

    @Id
    @Column(name = "cod_bol", length = 11)
    private String codBol;

    @Temporal(TemporalType.DATE)
    @Column(name = "fec_bol")
    private Date fecha;
    
    @Column(name = "propina")
    private Float propina;

    @Column(name = "total_bol")
    private Float total;

    @Column(name = "dni_cli", length = 8)
    private String dniCli;

    @Column(name = "ruc_cli", length = 20)
    private String rucCli;

    @Column(name = "nom_cli", length = 50)
    private String nomCli;

    @Column(name = "num_cli", length = 9)
    private String numCli;

    @Column(name = "correo_cli", length = 50)
    private String correoCli;

    @ManyToOne
    @JoinColumn(name = "cod_moz", insertable = false, updatable = false)
    private Mozo mozo;

    @ManyToOne
    @JoinColumn(name = "cod_or", insertable = false, updatable = false)
    private Orden orden;

    @ManyToOne
    @JoinColumn(name = "comprobante", insertable = false, updatable = false)
    private Comprobante coboleta;

    @ManyToOne
    @JoinColumn(name = "tipopago", insertable = false, updatable = false)
    private TipoPago tipboleta;
}
