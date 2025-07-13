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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MozoService {

    @Autowired
    private MozoRepository mozoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    public Page<Map<String, Object>> obtenerMozosConFiltro(String nomMoz, Pageable pageable) {
        Page<Map<String, Object>> mozos = mozoRepository.findMozosConFiltro(nomMoz, pageable);
        mozos.forEach(mo -> {
            byte[] imgBytes = (byte[]) mo.get("img1_moz");
            if (imgBytes != null && imgBytes.length > 0) {
                String base64 = Base64.getEncoder().encodeToString(imgBytes);
                mo.put("img1Moz_base64", "data:image/jpeg;base64," + base64);
            }
        });

        return mozos;
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

    public Mozo editarMozo(String id, Mozo mozoDetalles) {
        if (!mozoRepository.existsById(id)) {
            throw new RuntimeException("El mozo con el id " + id + " No existe");
        }
        return mozoRepository.findById(id)
                .map(mozo -> {
                    mozo.setNomMoz(mozoDetalles.getNomMoz());
                    mozo.setCorreoMoz(mozoDetalles.getCorreoMoz());
                    String encryptedPassword = passwordEncoder.encode(mozoDetalles.getContraMoz());
                    mozo.setContraMoz(encryptedPassword);
                    if (mozoDetalles.getImg1Moz() != null) {
                        mozo.setImg1Moz(mozoDetalles.getImg1Moz());
                        String imgBase64 = Base64.getEncoder().encodeToString(mozoDetalles.getImg1Moz());
                        mozo.setImg1Moz_base64(imgBase64);
                    }   
                    return mozoRepository.save(mozo);
                })
                .orElseThrow(() -> new RuntimeException("Mozo no encontrado"));
    }

    @Transactional
    public void agregarMozo(String nom_moz, String correo_moz, String contra_moz, byte[] img1_moz, String cod_adm) {
        String cod_moz = mozoRepository.obtenerSiguienteCodigoMozoSumado1();
        String encryptedPassword = passwordEncoder.encode(contra_moz);
        System.out.println("Cod_moz nuevo: " + cod_moz);
        mozoRepository.insertarMozo(cod_moz, nom_moz, correo_moz, encryptedPassword, img1_moz, cod_adm);
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
