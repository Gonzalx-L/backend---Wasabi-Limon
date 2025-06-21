package backend.dao;

import backend.modelo.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoriaRepository extends JpaRepository<Categoria, String> {
    
    @Query(value = """
        SELECT LPAD(CAST(MAX(CAST(cod_cat AS UNSIGNED)) + 1 AS CHAR), 3, '0') 
        FROM categoria
        """, nativeQuery = true)
    String obtenerSiguienteCodigoCategoriasumado1();
}
