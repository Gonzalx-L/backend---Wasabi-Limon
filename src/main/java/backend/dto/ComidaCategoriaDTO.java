package backend.dto;

import lombok.Data;

@Data
public class ComidaCategoriaDTO {
    private String codCom;
    private String nomCom;
    private float precNom;
    private String descCom;
    private String codCat;
    private String nomCat;
    
    public ComidaCategoriaDTO(String codCom, String nomCom, float precNom, String descCom, String codCat, String nomCat) {
        this.codCom = codCom;
        this.nomCom = nomCom;
        this.precNom = precNom;
        this.descCom = descCom;
        this.codCat = codCat;
        this.nomCat = nomCat;
    }
}
