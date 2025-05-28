package backend.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "mozo")
public class Mozo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_moz", length = 4)
    private String codMoz;

    @Column(name = "nom_moz", length = 50, nullable = false)
    private String nomMoz;

    @Column(name = "correo_moz", length = 50, nullable = false)
    private String correoMoz;

    @Column(name = "contra_moz", length = 50, nullable = false)
    private String contraMoz;

    @Lob
    @Column(name = "img1_moz", columnDefinition = "BLOB")
    private byte[] img1Moz;
    
    @Transient
    private String img1Moz_base64;

    @ManyToOne
    @JoinColumn(name = "cod_adm", insertable = false, updatable = false)
    @JsonIgnore
    private Administrador administrador;


    @OneToMany(mappedBy = "mozo", cascade = CascadeType.ALL)
    private List<Boleta> boletas;
    
    @OneToMany(mappedBy = "mozo", cascade = CascadeType.ALL)
    private List<Orden> orden;
}