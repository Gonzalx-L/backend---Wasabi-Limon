package backend.dao;

import backend.modelo.Comida;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Query(value = """
    SELECT CONCAT('C', LPAD(CAST(COALESCE(MAX(CAST(SUBSTRING(cod_com, 2) AS UNSIGNED)), 0) + 1 AS CHAR), 3, '0'))
    FROM comida
    """, nativeQuery = true)
    String obtenerCodigoComidaSumado1();

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO comida (cod_com, nom_com, prec_nom, desc_com)
        VALUES (:cod_com, :nom_com, :prec_nom, :desc_com)
        """, nativeQuery = true)
    public void insertarCom(
            @Param("cod_com") String cod_com,
            @Param("nom_com") String nom_com,
            @Param("prec_nom") float prec_nom,
            @Param("desc_com") String desc_com
    );

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO categoria_comida (cod_cat, cod_com)
        VALUES (:cod_cat, :cod_com)
        """, nativeQuery = true)
    public void insertarCatCom(
            @Param("cod_cat") String cod_cat,
            @Param("cod_com") String cod_com
    );

}
