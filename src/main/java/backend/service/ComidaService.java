package backend.service;

import backend.dao.*;
import backend.dto.ComidaCategoriaDTO;
import java.util.List;
import backend.modelo.*;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ComidaService {

    @Autowired
    private ComidaRepository comidaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Map<String, Object>> buscarPorNombre(String term) {
        List<Object[]> filas = comidaRepository.buscarPorNombre(term);
        List<Map<String, Object>> lista = new ArrayList<>();
        for (Object[] f : filas) {
            Map<String, Object> map = new HashMap<>();
            map.put("codCom", f[0]);
            map.put("nomCom", f[1]);
            lista.add(map);
        }
        return lista;
    }

    @Transactional
    public void agregarComida(String nom_com, float prec_nom, String desc_com, String cod_cat) {
        if (!categoriaRepository.existsById(cod_cat)) {
            throw new RuntimeException("La categoria con el id " + cod_cat + " No existe");
        }
        String cod_com = comidaRepository.obtenerCodigoComidaSumado1();
        System.out.println("Cod_com nuevo: " + cod_com);
        comidaRepository.insertarCom(cod_com, nom_com, prec_nom, desc_com);
        comidaRepository.insertarCatCom(cod_cat, cod_com);
    }

    public List<Map<String, Object>> findComida() {
        return comidaRepository.findAll().stream()
                .map(comida -> {
                    Map<String, Object> datosComida = new LinkedHashMap<>();
                    datosComida.put("cod_com", comida.getCodCom());
                    datosComida.put("nom_com", comida.getNomCom());
                    datosComida.put("prec_nom", comida.getPrecNom());
                    datosComida.put("desc_com", comida.getDescCom());
                    datosComida.put("cod_cat", comida.getCategorias().getFirst().getCod_cat());
                    datosComida.put("nom_cat", comida.getCategorias().getFirst().getNom_cat());
                    return datosComida;
                })
                .collect(Collectors.toList());
    }

    public Map<String, Object> findById(String id) {
        Comida comida = comidaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La comida con el ID " + id + " no existe."));

        Map<String, Object> datosComida = new LinkedHashMap<>();
        datosComida.put("cod_com", comida.getCodCom());
        datosComida.put("nom_com", comida.getNomCom());
        datosComida.put("prec_nom", comida.getPrecNom());
        datosComida.put("desc_com", comida.getDescCom());

        // Verifica si tiene categorías asociadas
        if (comida.getCategorias() != null && !comida.getCategorias().isEmpty()) {
            Categoria primera = comida.getCategorias().get(0);
            datosComida.put("cod_cat", primera.getCod_cat());
            datosComida.put("nom_cat", primera.getNom_cat());
        } else {
            datosComida.put("cod_cat", null);
            datosComida.put("nom_cat", null);
        }

        return datosComida;
    }

    @Transactional
    public Comida actualizarComida(String id, Comida comidaDetalles) {
        Comida comida = comidaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La comida con el ID " + id + " no existe"));
        try {
            comida.setNomCom(comidaDetalles.getNomCom());
            comida.setPrecNom(comidaDetalles.getPrecNom());
            comida.setDescCom(comidaDetalles.getDescCom());

            // Validar y obtener las nuevas categorías
            List<Categoria> nuevasCategorias = comidaDetalles.getCategorias().stream()
                    .map(cat -> categoriaRepository.findById(cat.getCod_cat())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada: " + cat.getCod_cat())))
                    .toList();

            // Actualizar relación muchos a muchos
            comida.getCategorias().clear();
            comida.getCategorias().addAll(nuevasCategorias);
            return comidaRepository.save(comida);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar la comida: " + e.getMessage());
        }
    }

    public void eliminarComida(String id) {
        if (!comidaRepository.existsById(id)) {
            throw new RuntimeException("La comida con el ID " + id + " no existe");
        }
        comidaRepository.deleteById(id);
    }

    public Page<ComidaCategoriaDTO> obtenerComidaConFiltro(String nomCom, Pageable pageable) {
        return comidaRepository.findComidaConFiltro(nomCom, pageable);
    }

}
