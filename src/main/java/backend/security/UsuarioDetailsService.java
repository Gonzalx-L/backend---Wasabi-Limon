package backend.security;

import backend.dao.AdministradorRepository;
import backend.dao.MozoRepository;
import backend.modelo.Administrador;
import backend.modelo.Mozo;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    @Autowired
    private MozoRepository mozoRepo;

    @Autowired
    private AdministradorRepository adminRepo;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Optional<Mozo> mozo = mozoRepo.findAll().stream()
                .filter(m -> m.getCorreoMoz().equalsIgnoreCase(correo))
                .findFirst();

        if (mozo.isPresent()) {
            return new UsuarioSecurity(
                    mozo.get().getCorreoMoz(),
                    mozo.get().getContraMoz(),
                    "MOZO"
            );
        }
        System.out.println("Intentando autenticar: " + correo);

        Optional<Administrador> admin = adminRepo.findAll().stream()
                .filter(a -> a.getCorreoAdm().equalsIgnoreCase(correo))
                .findFirst();

        if (admin.isPresent()) {
            return new UsuarioSecurity(
                    admin.get().getCorreoAdm(),
                    admin.get().getContraAdm(),
                    "ADMIN"
            );
        }

        throw new UsernameNotFoundException("Usuario no encontrado");
    }
}
