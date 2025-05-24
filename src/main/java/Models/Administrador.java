package Models;

import jakarta.persistence.*;
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(name = "administrador")
public class Administrador {

    @Id
    @Column(name = "cod_adm", length = 4)
    private String codAdm;

    @Column(name = "nom_adm", length = 50, nullable = false)
    private String nomAdm;

    @Column(name = "correo_adm", length = 60, nullable = false)
    private String correoAdm;

    @Column(name = "contra_adm", length = 50, nullable = false)
    private String contraAdm;

    @Lob
    @Column(name = "img1_adm")
    private byte[] img1Adm;

    @OneToMany(mappedBy = "administrador", cascade = CascadeType.ALL)
    private List<Mozo> mozos;
}