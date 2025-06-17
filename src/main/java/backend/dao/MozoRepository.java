package backend.dao;

import backend.modelo.Mozo;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MozoRepository extends JpaRepository<Mozo, String> {

    @Query("""
    SELECT new map(
        o.nomMoz as nom_moz,
        SUM(b.propina) as propina
    )
    FROM Boleta b
    JOIN b.mozo o
    WHERE (:codMoz IS NULL OR o.codMoz = :codMoz)
    AND (:year IS NULL OR YEAR(b.fecha) = :year)
    AND (:month IS NULL OR MONTH(b.fecha) = :month)
    AND (:day IS NULL OR DAY(b.fecha) = :day)
    GROUP BY o.nomMoz
    """)
    List<Map<String, Object>> ObtenerPropinaReporteFiltro(
            @Param("codMoz") String codMoz,
            @Param("year") Integer year,
            @Param("month") Integer month,
            @Param("day") Integer day
    );
}
