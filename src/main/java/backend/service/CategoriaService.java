package backend.service;

import backend.dto.CategoriaNombreDTO;
import backend.dto.ComidaNombreDTO;
import backend.modelo.Categoria;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import backend.dao.CategoriaRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional

    public List<CategoriaNombreDTO> listarNombres() {
        return categoriaRepository.findAll()
                .stream()
                .map(c -> new CategoriaNombreDTO(c.getNom_cat(), c.getCod_cat()))

                .collect(Collectors.toList());
    }

    public List<ComidaNombreDTO> listarComidasPorCategoria(String codCat) {
        Categoria categoria = categoriaRepository.findById(codCat).orElse(null);
        if (categoria == null) return List.of();

        return categoria.getComidas().stream()
                .map(comida -> new ComidaNombreDTO(comida.getCodCom(), comida.getNomCom()))
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> buscarPorNombre(String term) {
        List<Object[]> filas = categoriaRepository.buscarPorNombre(term);
        List<Map<String, Object>> lista = new ArrayList<>();
        for (Object[] f : filas) {
            Map<String, Object> map = new HashMap<>();
            map.put("cod_cat", f[0]);
            map.put("nom_cat", f[1]);
            lista.add(map);
        }
        return lista;
    }
    
}
