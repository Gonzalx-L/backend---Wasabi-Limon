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
    @Column(name = "cod_moz", length = 4)
    private String codMoz;

    @Column(name = "nom_moz", length = 50, nullable = false)
    private String nomMoz;

    @Column(name = "correo_moz", length = 50, nullable = false)
    private String correoMoz;

    @Column(name = "contra_moz", length = 50, nullable = false)
    private String contraMoz;
    
    @JsonIgnore
    @Lob
    @Column(name = "img1_moz", columnDefinition = "BLOB")
    private byte[] img1Moz;
    
    @Transient
    private String img1Moz_base64;
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "cod_adm")
    private Administrador administrador;

    @JsonIgnore
    @OneToMany(mappedBy = "mozo", cascade = CascadeType.ALL)
    private List<Boleta> boletas;
    
    @JsonIgnore
    @OneToMany(mappedBy = "mozo", cascade = CascadeType.ALL)
    private List<Orden> orden;
}