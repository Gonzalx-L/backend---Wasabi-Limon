package backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import backend.dao.*;
import ch.qos.logback.core.model.Model;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BoletaService {

    @Autowired
    private BoletaRepository boletaRepository;

    public List<Map<String, Object>> findBoletas(Model model) {
        return boletaRepository.findBoletas().stream().map(boleta -> {
            Map<String, Object> datos = new LinkedHashMap<>();
            datos.put("cod_bol", boleta.get("cod_bol"));
            datos.put("hora", boleta.get("hora"));
            datos.put("fec_bol", boleta.get("fec_bol"));
            datos.put("total_bol", boleta.get("total_bol"));
            return datos;
        }).collect(Collectors.toList());
    }

    public List<Map<String, Object>> findBoletasFiltradas(
    String codMoz, Float total1, Float total2, String horaInicio, String horaFin, String fechaInicio, String fechaFin) {
    try {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
        // Convertir Strings a Time y Date (si no son nulos)
        Time inicio = (horaInicio != null && !horaInicio.isEmpty()) ? new Time(timeFormat.parse(horaInicio).getTime()) : null;
        Time fin = (horaFin != null && !horaFin.isEmpty()) ? new Time(timeFormat.parse(horaFin).getTime()) : null;
        Date fechaIni = (fechaInicio != null && !fechaInicio.isEmpty()) ? dateFormat.parse(fechaInicio) : null;
        Date fechaFinConv = (fechaFin != null && !fechaFin.isEmpty()) ? dateFormat.parse(fechaFin) : null;

        return boletaRepository.findBoletasFiltradas(codMoz, total1, total2, inicio, fin, fechaIni, fechaFinConv)
            .stream()
            .map(boleta -> {
                Map<String, Object> datos = new LinkedHashMap<>();
                datos.put("cod_bol", boleta.get("cod_bol"));
                datos.put("hora", boleta.get("hora"));
                datos.put("fec_bol", boleta.get("fec_bol"));
                datos.put("total_bol", boleta.get("total_bol"));
                datos.put("cod_moz", boleta.get("cod_moz"));
                return datos;
            })
            .collect(Collectors.toList());
    } catch (Exception e) {
        throw new RuntimeException("Error al convertir horas o fechas a formato correcto", e);
    }
}

    public List<Map<String, Object>> BoletaDetallePorCodigo(String codBol) {
        return boletaRepository.BoletaDetallePorCodigo(codBol).stream()
                .map(boleta -> {
                    Map<String, Object> datos = new LinkedHashMap<>();
                    datos.put("cod_bol", boleta.get("cod_bol"));
                    datos.put("fec_bol", boleta.get("fec_bol"));
                    datos.put("hora", boleta.get("hora"));
                    datos.put("propina", boleta.get("propina"));
                    datos.put("total_bol", boleta.get("total_bol"));
                    datos.put("dni_cli", boleta.get("dni_cli"));
                    datos.put("ruc_cli", boleta.get("ruc_cli"));
                    datos.put("nom_cli", boleta.get("nom_cli"));
                    datos.put("num_cli", boleta.get("num_cli"));
                    datos.put("correo_cli", boleta.get("correo_cli"));
                    datos.put("cod_moz", boleta.get("cod_moz"));
                    datos.put("nom_moz", boleta.get("nom_moz"));
                    datos.put("cod_or", boleta.get("cod_or"));
                    datos.put("nom_compro", boleta.get("nom_compro"));
                    datos.put("nom_tipopago", boleta.get("nom_tipopago"));
                    return datos;
                }).collect(Collectors.toList());
    }

    public List<Map<String, Object>> BoletaDetalleComidasCodigoOr(String codOr) {
        return boletaRepository.BoletaDetalleComidasCodigoOr(codOr).stream()
                .map(boleta -> {
                    Map<String, Object> datos = new LinkedHashMap<>();
                    datos.put("cod_or", boleta.get("cod_or"));
                    datos.put("mesa", boleta.get("mesa"));
                    datos.put("hora", boleta.get("hora"));
                    datos.put("cod_com", boleta.get("cod_com"));
                    datos.put("nom_com", boleta.get("nom_com"));
                    datos.put("prec_nom", boleta.get("prec_nom"));
                    datos.put("cantidad", boleta.get("cantidad"));
                    return datos;
                }).collect(Collectors.toList());
    }
}
