package backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import backend.modelo.*;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TipopagoRepository extends JpaRepository<TipoPago, Object>{
    @Query("""
    SELECT new map(
        c.nomTipopago as nom_tipopago, 
        COUNT(b.tipboleta) as veces_usado
    )
    FROM Boleta b
    JOIN b.tipboleta c
    WHERE (:year IS NULL OR FUNCTION('YEAR', b.fecha) = :year)
    AND (:month IS NULL OR FUNCTION('MONTH', b.fecha) = :month)
    AND (:day IS NULL OR FUNCTION('DAY', b.fecha) = :day)
    GROUP BY c.nomTipopago
    ORDER BY veces_usado DESC
    """)
    List<Map<String, Object>> ObtenerTipoPagoReporte(
            @Param("year") Integer year,
            @Param("month") Integer month,
            @Param("day") Integer day
    );
}
