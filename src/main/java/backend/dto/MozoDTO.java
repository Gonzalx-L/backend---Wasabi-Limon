/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend.dto;

import backend.modelo.Mozo;
import java.util.Base64;


public class MozoDTO {
    private String codMoz;
    private String nomMoz;
    private String correoMoz;
    private String img1Moz_base64;

    public MozoDTO(Mozo mozo) {
        this.codMoz = mozo.getCodMoz();
        this.nomMoz = mozo.getNomMoz();
        this.correoMoz = mozo.getCorreoMoz();
        if (mozo.getImg1Moz() != null) {
            this.img1Moz_base64 = Base64.getEncoder().encodeToString(mozo.getImg1Moz());
        }
    }

    public String getCodMoz() {
        return codMoz;
    }

    public void setCodMoz(String codMoz) {
        this.codMoz = codMoz;
    }

    public String getNomMoz() {
        return nomMoz;
    }

    public void setNomMoz(String nomMoz) {
        this.nomMoz = nomMoz;
    }

    public String getCorreoMoz() {
        return correoMoz;
    }

    public void setCorreoMoz(String correoMoz) {
        this.correoMoz = correoMoz;
    }

    public String getImg1Moz_base64() {
        return img1Moz_base64;
    }

    public void setImg1Moz_base64(String img1Moz_base64) {
        this.img1Moz_base64 = img1Moz_base64;
    }
}
