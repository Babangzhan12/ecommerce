package com.zan.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zan.ecommerce.entity.Pengguna;
import com.zan.ecommerce.model.JwtResponse;
import com.zan.ecommerce.model.LoginRequest;
import com.zan.ecommerce.model.SignupRequest;
import com.zan.ecommerce.security.jwt.JwtUtils;
import com.zan.ecommerce.security.service.UserDetailsImpl;
import com.zan.ecommerce.service.PenggunaService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PenggunaService penggunaService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(
        @RequestBody LoginRequest request){
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();

            return ResponseEntity.ok().body(new JwtResponse(token, principal.getUsername(), principal.getEmail()));
        }

        @PostMapping("/signup")
        public Pengguna signup(@RequestBody SignupRequest request){
            Pengguna pengguna = new Pengguna();
            pengguna.setId(request.getUsername());
            pengguna.setEmail(request.getEmail());
            pengguna.setPassword(passwordEncoder.encode(request.getPassword()));
            pengguna.setName(request.getName());
            pengguna.setRoles("User");
            Pengguna created = penggunaService.create(pengguna);
            return created;
        }
}
