/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend.jwt;

import backend.dao.MozoRepository;
import backend.modelo.Mozo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class Jwt {

    private String secretKey = "RqxPOuVfHoBA8Uq40MhJvfY6qEHOOWWvg6N9W9vt23s=";
    @Autowired
    private MozoRepository mozoRepo;

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        // ✅ extraer roles reales del UserDetails
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", roles);
        claims.put("username", userDetails.getUsername());

        Optional<Mozo> mozoOpt = mozoRepo.findAll().stream()
                .filter(m -> m.getCorreoMoz().equalsIgnoreCase(userDetails.getUsername()))
                .findFirst();

        if (mozoOpt.isPresent()) {
            Mozo mozo = mozoOpt.get();
            claims.put("codMozo", mozo.getCodMoz()); // 👈 Aquí lo agregas al JWT
        }

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuer("DCB")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 30 * 1000))
                .signWith(generateKey())
                .compact();
    }

    private SecretKey generateKey() {
        byte[] decode = Decoders.BASE64.decode(getSecretKey());
        return Keys.hmacShaKeyFor(decode);
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String extractUserName(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimResolver) {
        Claims claims = extractClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return userName.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    public List<String> extractRoles(String token) {
        Claims claims = extractClaims(token);
        return claims.get("roles", List.class);
    }
}
