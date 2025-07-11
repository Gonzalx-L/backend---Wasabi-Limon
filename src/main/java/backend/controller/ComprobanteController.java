/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend.controller;

import backend.dao.ComprobanteRepository;
import backend.dto.ComprobanteDTO;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comprobante")
public class ComprobanteController {
        private final ComprobanteRepository comprobanteRepository;

    public ComprobanteController(ComprobanteRepository comprobanteRepository) {
        this.comprobanteRepository = comprobanteRepository;
    }

@GetMapping
public List<ComprobanteDTO> listar() {
    return comprobanteRepository.findAll()
        .stream()
        .map(tp -> new ComprobanteDTO(tp.getCodCompro(), tp.getNomCompro()))
        .toList();
}
}
