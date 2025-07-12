package backend.dao;

import backend.modelo.Mozo;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Query(value = """
        SELECT LPAD(CAST(MAX(CAST(cod_moz AS UNSIGNED)) + 1 AS CHAR), 4, '0') 
        FROM mozo
        """, nativeQuery = true)
    String obtenerSiguienteCodigoMozoSumado1();

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO mozo (cod_moz, nom_moz, correo_moz, contra_moz, img1_moz, cod_adm)
        VALUES (:cod_moz, :nom_moz, :correo_moz, :contra_moz, :img1_moz, :cod_adm)
        """, nativeQuery = true)
    public void insertarMozo(
            @Param("cod_moz") String cod_moz,
            @Param("nom_moz") String nom_moz,
            @Param("correo_moz") String correo_moz,
            @Param("contra_moz") String contra_moz,
            @Param("img1_moz") byte[] img1_moz,
            @Param("cod_adm") String cod_adm
    );

    @Query(value = "SELECT new map( "
            + "m.codMoz as cod_moz, "
            + "m.nomMoz as nom_moz, "
            + "m.correoMoz as correo_moz, "
            + "m.img1Moz as img1_moz "
            + ") "
            + "FROM Mozo m "
            + "WHERE (:nomMoz IS NULL OR LOWER(m.nomMoz) LIKE LOWER(CONCAT('%', :nomMoz, '%')))",
            countQuery = "SELECT COUNT(m) FROM Mozo m "
            + "WHERE (:nomMoz IS NULL OR LOWER(m.nomMoz) LIKE LOWER(CONCAT('%', :nomMoz, '%')))"
    )
    Page<Map<String, Object>> findMozosConFiltro(
            @Param("nomMoz") String nomMoz,
            Pageable pageable
    );
}
