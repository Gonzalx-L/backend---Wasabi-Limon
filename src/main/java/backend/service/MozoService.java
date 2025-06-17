package backend.service;

import backend.dao.MozoRepository;
import java.util.List;
import backend.modelo.Mozo;
import ch.qos.logback.core.model.Model;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MozoService {

    @Autowired
    private MozoRepository mozoRepository;

    public List<Mozo> findMozos() {
        return mozoRepository.findAll().stream()
                .map(mozo -> {
                    if (mozo.getImg1Moz() != null) {
                        String imgBase64 = Base64.getEncoder().encodeToString(mozo.getImg1Moz());
                        mozo.setImg1Moz_base64(imgBase64);
                    }
                    return mozo;
                })
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> findMozoSinImg() {
        return mozoRepository.findAll().stream().map(mozo -> {
            Map<String, Object> datosMozo = new LinkedHashMap<>();
            datosMozo.put("cod_moz", mozo.getCodMoz());
            datosMozo.put("nom_moz", mozo.getNomMoz());
            datosMozo.put("correo_moz", mozo.getCorreoMoz());
            datosMozo.put("cod_adm", mozo.getAdministrador().getCodAdm());
            datosMozo.put("nom_adm", mozo.getAdministrador().getNomAdm());
            return datosMozo;
        }).collect(Collectors.toList());
    }

    public Mozo findById(String id) {
        return mozoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El mozo con ID " + id + " no existe."));
    }

    public Mozo actualizarMozo(String id, Mozo mozoDetalles) {
        if (mozoRepository.existsById(id)) {
            throw new RuntimeException("El mozo con el id " + id + "No existe");
        }
        return mozoRepository.findById(id)
                .map(mozo -> {
                    mozo.setNomMoz(mozoDetalles.getNomMoz());
                    mozo.setCorreoMoz(mozoDetalles.getCorreoMoz());
                    if (mozoDetalles.getImg1Moz() != null) {
                        mozo.setImg1Moz(mozoDetalles.getImg1Moz());
                        String imgBase64 = Base64.getEncoder().encodeToString(mozoDetalles.getImg1Moz());
                        mozo.setImg1Moz_base64(imgBase64);
                    }
                    return mozoRepository.save(mozo);
                })
                .orElseThrow(() -> new RuntimeException("Mozo no encontrado"));
    }

    public Mozo guardarMozo(Mozo mozo) {
        if (mozoRepository.existsById(mozo.getCodMoz())) {
            throw new RuntimeException("Ya existe un mozo con el ID " + mozo.getCodMoz());
        }
        if (mozo.getImg1Moz() != null) {
            String imgBase64 = Base64.getEncoder().encodeToString(mozo.getImg1Moz());
            mozo.setImg1Moz_base64(imgBase64);
        }
        return mozoRepository.save(mozo);
    }

    public void eliminarMozo(String id) {
        if (!mozoRepository.existsById(id)) {
            throw new RuntimeException("El mozo con ID " + id + " no existe.");
        }
        mozoRepository.deleteById(id);
    }

}
