package backend.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(name = "mozo")
public class Mozo {

    @Id
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
    @JoinColumn(name = "cod_adm")
    private Administrador administrador;

    @OneToMany(mappedBy = "mozo", cascade = CascadeType.ALL)
    private List<Boleta> boletas;
    
    @OneToMany(mappedBy = "mozo", cascade = CascadeType.ALL)
    private List<Orden> orden;
}