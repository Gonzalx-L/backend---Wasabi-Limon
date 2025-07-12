package backend.service;

import backend.dao.MozoRepository;
import java.util.List;
import backend.modelo.Mozo;
import jakarta.transaction.Transactional;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MozoService {

    @Autowired
    private MozoRepository mozoRepository;

    public List<Map<String, Object>> findMozos() {
        return mozoRepository.findAll().stream().map(mozo -> {
            Map<String, Object> datosMozo = new LinkedHashMap<>();
            datosMozo.put("cod_moz", mozo.getCodMoz());
            datosMozo.put("nom_moz", mozo.getNomMoz());
            datosMozo.put("correo_moz", mozo.getCorreoMoz());
            datosMozo.put("cod_adm", mozo.getAdministrador().getCodAdm());
            datosMozo.put("nom_adm", mozo.getAdministrador().getNomAdm());

            if (mozo.getImg1Moz() != null) {
                String imgBase64 = Base64.getEncoder().encodeToString(mozo.getImg1Moz());
                datosMozo.put("img_base64", imgBase64);
            } else {
                datosMozo.put("img_base64", null);
            }

            return datosMozo;
        }).collect(Collectors.toList());
    }

    public Map<String, Object> findById(String id) {
        Mozo mozo = mozoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El mozo con ID " + id + " no existe."));

        Map<String, Object> datosMozo = new LinkedHashMap<>();
        datosMozo.put("cod_moz", mozo.getCodMoz());
        datosMozo.put("nom_moz", mozo.getNomMoz());
        datosMozo.put("correo_moz", mozo.getCorreoMoz());
        datosMozo.put("cod_adm", mozo.getAdministrador().getCodAdm());
        datosMozo.put("contra_moz", mozo.getContraMoz());
        datosMozo.put("nom_adm", mozo.getAdministrador().getNomAdm());

        if (mozo.getImg1Moz() != null) {
            String base64 = Base64.getEncoder().encodeToString(mozo.getImg1Moz());
            datosMozo.put("img_base64", base64);
        } else {
            datosMozo.put("img_base64", null);
        }
        return datosMozo;
    }
    
    //sin editar imagen
    public Mozo editarMozo(String id, Mozo mozoDetalles) {
        if (!mozoRepository.existsById(id)) {
            throw new RuntimeException("El mozo con el id " + id + " No existe");
        }
        return mozoRepository.findById(id)
                .map(mozo -> {
                    mozo.setNomMoz(mozoDetalles.getNomMoz());
                    mozo.setCorreoMoz(mozoDetalles.getCorreoMoz());
                    mozo.setContraMoz(mozoDetalles.getContraMoz());
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
    
    @Transactional
    public void agregarMozo(String nom_moz, String correo_moz, String contra_moz, byte[] img1_moz, String cod_adm ) {
        String cod_moz = mozoRepository.obtenerSiguienteCodigoMozoSumado1();
        System.out.println("Cod_moz nuevo: " + cod_moz );
        mozoRepository.insertarMozo(cod_moz, nom_moz, correo_moz, contra_moz, img1_moz, cod_adm);
    }

    public void eliminarMozo(String id) {
        if (!mozoRepository.existsById(id)) {
            throw new RuntimeException("El mozo con ID " + id + " no existe.");
        }
        mozoRepository.deleteById(id);
    }
    
        public Mozo login(String correo, String password) {
        return mozoRepository.findAll().stream()
            .filter(m -> m.getCorreoMoz().equalsIgnoreCase(correo) && m.getContraMoz().equals(password))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Credenciales inválidas para mozo"));
    }
    public List<Map<String, Object>> ObtenerPropinaReporteFiltro(
            String codMoz, Integer year, Integer month, Integer day
    ) {
        return mozoRepository.ObtenerPropinaReporteFiltro(codMoz, year, month, day);
    }


}
