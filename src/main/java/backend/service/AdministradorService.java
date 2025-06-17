package backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import backend.dao.AdministradorRepository;
import backend.modelo.*;
import ch.qos.logback.core.model.Model;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdministradorService {
    
    @Autowired
    private AdministradorRepository administradorRepository;
    
    public List<Administrador> findAdmi(Model model) {
        return administradorRepository.findAll().stream().map(mozo -> {
            if (mozo.getImg1Adm()!= null) {
                String img1Base64 = Base64.getEncoder().encodeToString(mozo.getImg1Adm());
                mozo.setImg1Adm_base64(img1Base64);
            }
            return mozo;
        }).collect(Collectors.toList());
    }
}
