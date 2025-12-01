package com.levelup.gestionusuarios.controller;

import com.levelup.gestionusuarios.dto.LoginRequest;
import com.levelup.gestionusuarios.dto.LoginResponse;
import com.levelup.gestionusuarios.entity.UsuarioEntity;
import com.levelup.gestionusuarios.repository.UsuarioRepository;
import com.levelup.gestionusuarios.security.CustomUserDetailsService;
import com.levelup.gestionusuarios.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Autenticar usuario
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), 
                    loginRequest.getPassword()
                )
            );

            // Cargar detalles del usuario
            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());

            // Obtener usuario de la BD
            UsuarioEntity usuario = usuarioRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Generar token
            final String jwt = jwtUtil.generateToken(userDetails);

            // Extraer roles
            var roles = userDetails.getAuthorities().stream()
                    .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                    .collect(Collectors.toList());

            // Respuesta
            LoginResponse response = new LoginResponse(
                jwt,
                usuario.getEmail(),
                usuario.getNombreCompleto(),
                roles
            );

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales incorrectas");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en el servidor: " + e.getMessage());
        }
    }
}
