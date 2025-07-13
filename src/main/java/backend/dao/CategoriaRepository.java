package backend.dao;

import backend.modelo.Categoria;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoriaRepository extends JpaRepository<Categoria, String> {
    
    @Query(value = """
        SELECT LPAD(CAST(MAX(CAST(cod_cat AS UNSIGNED)) + 1 AS CHAR), 3, '0') 
        FROM categoria
        """, nativeQuery = true)
    String obtenerSiguienteCodigoCategoriasumado1();
    
    @Query("SELECT c.cod_cat, c.nom_cat "
            + "FROM Categoria c "
            + "WHERE LOWER(c.nom_cat) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Object[]> buscarPorNombre(@Param("term") String term);
}
