package backend.service;

import backend.dao.*;
import backend.modelo.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TipoPagoCmprobService {
    
    @Autowired
    private TipopagoRepository tipopagoRepository;
    
    @Autowired
    private ComprobanteRepository comprobanteRepository;
    
    public List<Map<String, Object>> findTipoPago() {
        return tipopagoRepository.findAll().stream()
                .map(tipopago -> {
                    Map<String, Object> datostipopago = new LinkedHashMap<>();
                    datostipopago.put("cod_tipopago", tipopago.getCodTipopago());
                    datostipopago.put("nom_tipopago", tipopago.getNomTipopago());
                    return datostipopago;
                })
                .collect(Collectors.toList());
    }
    
    public List<Map<String, Object>> findComprobante() {
        return comprobanteRepository.findAll().stream()
                .map(comprobante -> {
                    Map<String, Object> datosComprobante = new LinkedHashMap<>();
                    datosComprobante.put("cod_compro", comprobante.getCodCompro());
                    datosComprobante.put("nom_compro", comprobante.getNomCompro());
                    return datosComprobante;
                })
                .collect(Collectors.toList());
    }
}
