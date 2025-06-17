package backend.dao;

import backend.modelo.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AdministradorRepository extends JpaRepository<Administrador, String>{
    
}
