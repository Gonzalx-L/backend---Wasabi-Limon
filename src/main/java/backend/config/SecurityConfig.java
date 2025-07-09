package backend.config;

import backend.jwt.JwtAuthFilter;
import backend.security.UsuarioDetailsService;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
       
@Autowired
private JwtAuthFilter jwtAuthFilter;

        @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200")); // frontend Angular
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true); // si usas Authorization: Bearer

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }    

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(withDefaults()) // habilita CORS
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                    //libre acceso
                .requestMatchers("/api/auth/**").permitAll()
                    //solo ADMIN  
                .requestMatchers("/api/admi/**").hasAnyAuthority("ROLE_ADMIN")
                .requestMatchers("/api/reportes/**").hasAnyAuthority("ROLE_ADMIN")
                    //acceso para mozo y admin
                .requestMatchers("/api/boleta/**").hasAnyAuthority("ROLE_MOZO", "ROLE_ADMIN")
                .requestMatchers("/api/mozo/**").hasAnyAuthority("ROLE_MOZO", "ROLE_ADMIN")
                .requestMatchers("/api/pedido-temporal/**").hasAnyAuthority("ROLE_MOZO", "ROLE_ADMIN")
                .requestMatchers("/api/ordenes/**").hasAnyAuthority("ROLE_MOZO", "ROLE_ADMIN")
                .requestMatchers("/api/categorias/**").hasAnyAuthority("ROLE_MOZO", "ROLE_ADMIN")
                .requestMatchers("/api/comida/**").hasAnyAuthority("ROLE_MOZO", "ROLE_ADMIN")
                .requestMatchers("/api/mesas/**").hasAnyAuthority("ROLE_MOZO", "ROLE_ADMIN")

                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
        @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
