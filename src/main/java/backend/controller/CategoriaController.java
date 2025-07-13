package backend.controller;

import backend.dto.CategoriaNombreDTO;
import backend.dto.ComidaNombreDTO;
import backend.service.CategoriaService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping("/nombres")
    public List<CategoriaNombreDTO> listarSoloNombres() {
        return categoriaService.listarNombres();
    }

    @GetMapping("/{codCat}/comidas")
    public List<ComidaNombreDTO> listarComidas(@PathVariable String codCat) {
        return categoriaService.listarComidasPorCategoria(codCat);
    }
    
    @GetMapping("/buscar")
    public List<Map<String, Object>> buscar(@RequestParam String term) {
        return categoriaService.buscarPorNombre(term);
    }
}
