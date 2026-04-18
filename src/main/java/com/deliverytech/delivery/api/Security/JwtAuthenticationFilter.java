package com.deliverytech.delivery.api.Security;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import com.deliverytech.delivery.api.model.Usuario;
import com.deliverytech.delivery.api.repository.UsuarioRepository;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtill jwtUtill;
    private final UsuarioRepository usuarioRepository;

    public JwtAuthenticationFilter(JwtUtill jwtUtill, @Lazy UsuarioRepository usuarioRepository) {
        this.jwtUtill = jwtUtill;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain chain
    ) throws IOException, ServletException {

        String token = extractToken((HttpServletRequest) request);
        if (token == null) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String email = jwtUtill.extractEmail(token);
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
                if (usuario != null && jwtUtill.isTokenValid(token, email)) {
                    UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("[JWT] Autenticado: " + email + " | Roles: " + usuario.getAuthorities());
                } else {
                    System.out.println("[JWT] Token invalido ou usuario nao encontrado. Email: " + email + " | Usuario: " + usuario);
                }
            }
        } catch (Exception e) {
            System.out.println("[JWT] Erro ao validar token: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }

        chain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest r) {
        String authHeader = r.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7);
    }

}