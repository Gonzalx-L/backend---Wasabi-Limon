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
    
    @Query("""
    SELECT new map(
        tp.nomTipopago as nomTipopago,
        MONTH(b.fecha) as mes,
        COUNT(b.tipboleta) as cantidad_pedida
    )
    FROM Boleta b
    JOIN b.tipboleta tp
    WHERE (:year IS NULL OR YEAR(b.fecha) = :year)
    AND tp.codTipopago = :codTipopago
    GROUP BY MONTH(b.fecha)
    ORDER BY mes
    """)
    List<Map<String, Object>> reporteMensualPorTipoPago(
            @Param("codTipopago") String codCom,
            @Param("year") Integer year
    );
}
