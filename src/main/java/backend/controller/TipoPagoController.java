/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend.controller;

import backend.dao.TipopagoRepository;
import backend.dto.TipoPagoDTO;
import backend.modelo.TipoPago;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tipopago")
public class TipoPagoController {
    private final TipopagoRepository tipoPagoRepository;

    public TipoPagoController(TipopagoRepository tipoPagoRepository) {
        this.tipoPagoRepository = tipoPagoRepository;
    }

@GetMapping
public List<TipoPagoDTO> listar() {
    return tipoPagoRepository.findAll()
        .stream()
        .map(tp -> new TipoPagoDTO(tp.getCodTipopago(), tp.getNomTipopago()))
        .toList();
}
}
