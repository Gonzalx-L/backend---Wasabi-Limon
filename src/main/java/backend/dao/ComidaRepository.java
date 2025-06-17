package backend.dao;

import backend.modelo.Comida;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ComidaRepository extends JpaRepository<Comida, String> {

    @Query("""
    SELECT new map(
        c.nomCom as nom_com, 
        COUNT(do.id.codCom) as cantidad_pedida
    )
    FROM Comida c
    JOIN c.detalles do
    JOIN do.orden o
    JOIN o.boletas b
    WHERE (:year IS NULL OR YEAR(b.fecha) = :year)
    AND (:month IS NULL OR MONTH(b.fecha) = :month)
    AND (:day IS NULL OR DAY(b.fecha) = :day)
    GROUP BY c.nomCom
    ORDER BY cantidad_pedida DESC
    """)
    List<Map<String, Object>> ComidaReporte(
            @Param("year") Integer year,
            @Param("month") Integer month,
            @Param("day") Integer day
    );
}
