package backend.controller;

import backend.modelo.*;
import backend.service.TipoPagoCmprobService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tipCompro")
public class TipoPagoCmproController {

    @Autowired
    private TipoPagoCmprobService tipoPagoCmprobService;
    
    @GetMapping("/tipopago-listar")
    public List<Map<String, Object>> findTiposPago() {
        return tipoPagoCmprobService.findTipoPago();
    }
    
    @GetMapping("/comprobante-listar")
    public List<Map<String, Object>> findComprobante() {
        return tipoPagoCmprobService.findComprobante();
    }


}
