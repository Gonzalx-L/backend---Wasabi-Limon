package backend.service;

import backend.dao.MozoRepository;
import java.util.List;
import backend.modelo.Mozo;
import ch.qos.logback.core.model.Model;
import java.util.Base64;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MozoService {

    @Autowired
    private MozoRepository mozoDAO;

    public List<Mozo> findMozos(Model model) {
        return mozoDAO.findAll().stream().map(mozo -> {
            if (mozo.getImg1Moz() != null) {
                String img1Base64 = Base64.getEncoder().encodeToString(mozo.getImg1Moz());
                mozo.setImg1Moz_base64(img1Base64);
            }
            return mozo;
        }).collect(Collectors.toList());
    }


    /*public List<Map<String, String>> findMozos() {
        return mozoDAO.findAll().stream().map(mozo -> {
            Map<String, String> orderedMap = new LinkedHashMap<>();
            orderedMap.put("cod_moz", mozo.getCodMoz());
            orderedMap.put("nom_moz", mozo.getNomMoz());
            orderedMap.put("correo_moz", mozo.getCorreoMoz());
            orderedMap.put("contra_moz", mozo.getContraMoz());
            return orderedMap;
        }).collect(Collectors.toList());
    }*/
    
    public Mozo login(String correo, String password) {
        return mozoDAO.findAll().stream()
            .filter(m -> m.getCorreoMoz().equalsIgnoreCase(correo) && m.getContraMoz().equals(password))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Credenciales inválidas para mozo"));
    }
}
