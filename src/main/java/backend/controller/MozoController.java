package backend.controller;

import java.util.List;
import backend.modelo.Mozo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import backend.service.MozoService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/mozo")
public class MozoController {

    @Autowired
    private MozoService mozoService;

    @GetMapping("/listar")
    public List<Mozo> findMozos() {
        return mozoService.findMozos(null);
    }
}
