package backend.service;

import backend.dao.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportesService {

    @Autowired
    private ComprobanteRepository comprobanteRepository;

    @Autowired
    private TipopagoRepository tipopagoRepository;

    @Autowired
    private ComidaRepository comidaRepository;
    
    @Autowired
    private BoletaRepository boletaRepository;
    
    @Autowired
    private MozoRepository mozoRepository;

    public List<Map<String, Object>> obtenerComprobantesReporte(Integer year, Integer month, Integer day) {
        return comprobanteRepository.ObtenerComprobantesReporte(year, month, day);
    }

    public List<Map<String, Object>> obtenerTipoPagoReporte(Integer year, Integer month, Integer day) {
        return tipopagoRepository.ObtenerTipoPagoReporte(year, month, day);
    }

    public List<Map<String, Object>> obtenerComidaReporte(Integer year, Integer month, Integer day) {
        return comidaRepository.ComidaReporte(year, month, day).stream()
                .limit(10) 
                .collect(Collectors.toList());
    }
    
    public List<Map<String, Object>> ObtenerIngresosReporteFiltro(Integer year, Integer month, Integer day) {
        return boletaRepository.ObtenerIngresosReporteFiltro(year, month, day);
    }
    
    public List<Map<String, Object>> ObtenerPropinaReporteFiltro(String codMoz,Integer year, Integer month, Integer day) {
        return mozoRepository.ObtenerPropinaReporteFiltro(codMoz ,year, month, day);
    }

}
