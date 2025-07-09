package backend.jwt;

import backend.security.UsuarioDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.userdetails.UserDetailsService; // ✅ correcto

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private Jwt jwtUtil;

    @Autowired
    private UsuarioDetailsService userDetailsService;
        
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, java.io.IOException {
        
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        
        System.out.println("JwtAuthFilter - " + method + " " + requestURI);
        
        final String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + authHeader);
        
        // Mostrar todos los headers para debugging
        System.out.println("Todos los headers:");
        request.getHeaderNames().asIterator().forEachRemaining(headerName -> {
            System.out.println("    " + headerName + ": " + request.getHeader(headerName));
        });
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println(" No hay token Bearer válido - permitiendo continuar sin autenticación JWT");
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        final String username = jwtUtil.extractUserName(jwt);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        System.out.println("JWT encontrado: " + jwt.substring(0, Math.min(30, jwt.length())) + "...");
        System.out.println(" Username del token: " + username);
        System.out.println("¿Ya hay Authentication?: " + (authentication != null));

        if (username != null && authentication == null) {
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                System.out.println("UserDetails cargado: " + userDetails.getUsername());
                System.out.println("Authorities del UserDetails: " + userDetails.getAuthorities());
                
                if (jwtUtil.isTokenValid(jwt, userDetails)) {
                    // Extraer roles del JWT
                    List<String> roles = jwtUtil.extractRoles(jwt);
                    System.out.println("Roles extraídos del JWT: " + roles);
                    
                    List<SimpleGrantedAuthority> authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                    
                    System.out.println("Authorities finales asignadas: " + authorities);

                    UsernamePasswordAuthenticationToken authToken = 
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    
                    System.out.println("Usuario autenticado exitosamente en SecurityContext");
                } else {
                    System.out.println("Token JWT inválido");
                }
            } catch (Exception e) {
                System.out.println("Error en autenticación JWT: " + e.getMessage());
                e.printStackTrace();
            }
        } else if (authentication != null) {
            System.out.println(" Usuario ya autenticado: " + authentication.getName());
            System.out.println("Authorities existentes: " + authentication.getAuthorities());
        }
        
        // Mostrar el estado final antes de continuar
        Authentication finalAuth = SecurityContextHolder.getContext().getAuthentication();
        if (finalAuth != null) {
            System.out.println("Estado final - Usuario: " + finalAuth.getName());
            System.out.println("Estado final - Authorities: " + finalAuth.getAuthorities());
            System.out.println("Estado final - Authenticated: " + finalAuth.isAuthenticated());
        } else {
            System.out.println("Estado final - NO HAY AUTENTICACIÓN");
        }
        
        filterChain.doFilter(request, response);
    }
}