package com.deliverytech.delivery.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery.api.dto.requests.LoginRequestDTO;
import com.deliverytech.delivery.api.dto.responses.LoginResponseDTO;
import com.deliverytech.delivery.api.enums.Role;
import com.deliverytech.delivery.api.exception.EntityNotFoundException;
import com.deliverytech.delivery.api.model.Usuario;
import com.deliverytech.delivery.api.repository.UsuarioRepository;
import com.deliverytech.delivery.api.security.JwtUtill;

import org.springframework.web.bind.annotation.GetMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Endpoints para registro e login de usuários.")
public class AuthController {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtill jwtUtill;

    public AuthController(UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtUtill jwtUtill) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtill = jwtUtill;
    }

    @Operation(summary = "Cadastrar novo usuário", description = "Cria uma nova conta de usuário. Regra: Não é permitido criar ADMIN diretamente.")
    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody LoginRequestDTO request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Emaill já cadastrado");
        }

        if (request.getRole() == Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Não é permitido cadastrar um usuário com a role ADMIN");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(request.getEmail());
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setRole(request.getRole());
        usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso");
    }

    @Operation(summary = "Login de usuário", description = "Autentica um usuário e retorna um token JWT.")
    @PostMapping("/login")
    public ResponseEntity<?> Login(@RequestBody LoginRequestDTO login) {
        Usuario usuario = usuarioRepository.findByEmail(login.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Credenciais inválidas"));

        if (!passwordEncoder.matches(login.getSenha(), usuario.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = jwtUtill.generateToken(usuario.getEmail());
        return ResponseEntity.ok(new LoginResponseDTO(token, usuario.getEmail(), null, usuario.getRole()));
    }

    @Operation(summary = "Obter dados do usuário logado", description = "Retorna os detalhes do usuário baseados no token JWT fornecido.")
    @GetMapping("/me")
    public ResponseEntity<Usuario> me(Authentication auth) {
        String email = auth.getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow();
        return ResponseEntity.ok(usuario);

    }

}
