package backend.dao;

import backend.modelo.Orden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdenRepository extends JpaRepository<Orden, String> {
    List<Orden> findByCodMoz(String codMoz);
    List<Orden> findByCodMozAndEstado(String codMoz, String estado);

}
