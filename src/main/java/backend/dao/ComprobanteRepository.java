package backend.dao;

import backend.modelo.Comprobante;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ComprobanteRepository extends JpaRepository<Comprobante, Object> {

    @Query("""
    SELECT new map(
        c.nomCompro as nom_compro, 
        COUNT(b.coboleta) as veces_usado
    )
    FROM Boleta b
    JOIN b.coboleta c
    WHERE (:year IS NULL OR FUNCTION('YEAR', b.fecha) = :year)
    AND (:month IS NULL OR FUNCTION('MONTH', b.fecha) = :month)
    AND (:day IS NULL OR FUNCTION('DAY', b.fecha) = :day)
    GROUP BY c.nomCompro
    ORDER BY veces_usado DESC
    """)
    List<Map<String, Object>> ObtenerComprobantesReporte(
            @Param("year") Integer year,
            @Param("month") Integer month,
            @Param("day") Integer day
    );
    
    @Query("""
    SELECT new map(
        c.nomCompro as nomCompro,
        MONTH(b.fecha) as mes,
        COUNT(b.coboleta) as cantidad_pedida
    )
    FROM Boleta b
    JOIN b.coboleta c
    WHERE (:year IS NULL OR YEAR(b.fecha) = :year)
    AND c.codCompro = :codCompro
    GROUP BY MONTH(b.fecha)
    ORDER BY mes
    """)
    List<Map<String, Object>> reporteMensualPorComprobante(
            @Param("codCompro") String codCom,
            @Param("year") Integer year
    );
}
