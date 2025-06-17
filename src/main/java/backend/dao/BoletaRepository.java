package backend.dao;

import backend.modelo.Boleta;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoletaRepository extends JpaRepository<Boleta, Object> {

    @Query("""
    SELECT new map(
        b.codBol as cod_bol, 
        b.fecha as fec_bol, 
        b.total as total_bol, 
        o.hora as hora
    )
    FROM Boleta b
    JOIN b.orden o
    """)
    List<Map<String, Object>> findBoletas();

    @Query("""
    SELECT new map(
        b.codBol as cod_bol,
        b.fecha as fec_bol,
        o.hora as hora, 
        b.total as total_bol, 
        m.codMoz as cod_moz
    )
    FROM Boleta b
    JOIN b.orden o
    JOIN b.mozo m
    WHERE (:codMoz IS NULL OR m.codMoz = :codMoz)
    AND (:total1 IS NULL OR :total2 IS NULL OR b.total BETWEEN :total1 AND :total2)
    AND (:horaInicio IS NULL OR :horaFin IS NULL OR o.hora BETWEEN :horaInicio AND :horaFin)
    AND (:fechaInicio IS NULL OR :fechaFin IS NULL OR b.fecha BETWEEN :fechaInicio AND :fechaFin)
    """)
    List<Map<String, Object>> findBoletasFiltradas(
            @Param("codMoz") String codMoz,
            @Param("total1") Float total1,
            @Param("total2") Float total2,
            @Param("horaInicio") Time horaInicio,
            @Param("horaFin") Time horaFin,
            @Param("fechaInicio") Date fechaInicio,
            @Param("fechaFin") Date fechaFin
    );

    @Query("""
    SELECT new map(
        b.codBol as cod_bol, b.fecha as fec_bol, o.hora as hora,b.propina as propina, b.total as total_bol, 
        b.dniCli as dni_cli, b.rucCli as ruc_cli, b.nomCli as nom_cli, b.numCli as num_cli, 
        b.correoCli as correo_cli, m.codMoz as cod_moz, m.nomMoz as nom_moz, o.codOr as cod_or, 
        c.nomCompro as nom_compro,t.nomTipopago as nom_tipopago
    )
    FROM Boleta b
        JOIN b.mozo m
        JOIN b.orden o
        JOIN b.coboleta c
        JOIN b.tipboleta t
        WHERE b.codBol = :codBol
    """)
    List<Map<String, Object>> BoletaDetallePorCodigo(@Param("codBol") String codBol);

    @Query("""
    SELECT new map(
        o.codOr as cod_or, o.mesa as mesa, o.hora as hora, c.codCom as cod_com, 
        c.nomCom as nom_com, c.precNom as prec_nom, do.cantidad as cantidad
    )
    FROM Orden o
        JOIN o.detalles do
        JOIN do.comida c
        WHERE o.codOr = :codOr
    """)
    List<Map<String, Object>> BoletaDetalleComidasCodigoOr(@Param("codOr") String codOr);

    @Query("""
    SELECT new map(
        SUM(b.total) as ingreso
    )
    FROM Boleta b
    WHERE (:year IS NULL OR YEAR(b.fecha) = :year)
    AND (:month IS NULL OR MONTH(b.fecha) = :month)
    AND (:day IS NULL OR DAY(b.fecha) = :day)
    """)
    List<Map<String, Object>> ObtenerIngresosReporteFiltro(
            @Param("year") Integer year,
            @Param("month") Integer month,
            @Param("day") Integer day
    );
}
