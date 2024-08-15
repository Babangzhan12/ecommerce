package com.zan.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.zan.ecommerce.model.RefreshTokenRequest;
import com.zan.ecommerce.model.SignupRequest;
import com.zan.ecommerce.security.jwt.JwtUtils;
import com.zan.ecommerce.security.service.UserDetailsImpl;
import com.zan.ecommerce.security.service.UserDetailsServiceImpl;
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

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(
        @RequestBody LoginRequest request){
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtUtils.generateJwtToken(authentication);

            String refreshToken = jwtUtils.generateRefreshJwtToken(authentication);

            UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();

            return ResponseEntity.ok().body(new JwtResponse(token, refreshToken,principal.getUsername(), principal.getEmail(), principal.getRoles(), principal.getName()));
        }

        @PostMapping("/signup")
        public Pengguna signup(@RequestBody SignupRequest request){
            Pengguna pengguna = new Pengguna();
            pengguna.setId(request.getUsername());
            pengguna.setEmail(request.getEmail());
            pengguna.setPassword(passwordEncoder.encode(request.getPassword()));
            pengguna.setName(request.getName());
            pengguna.setRoles("user");
            Pengguna created = penggunaService.create(pengguna);
            return created;
        }

        @PostMapping("/refreshToken")
        public ResponseEntity<JwtResponse> refreshToken(@RequestBody RefreshTokenRequest request){
            String token = request.getRefreshToken();
            boolean valid = jwtUtils.validateJwtToken(token);
            if (!valid){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            String username = jwtUtils.getUserNameFromJwtToken(token);
            UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetailsServiceImpl.loadUserByUsername(username);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsImpl, null, userDetailsImpl.getAuthorities());
            String newToken = jwtUtils.generateJwtToken(authentication);
            String refreshToken = jwtUtils.generateRefreshJwtToken(authentication);
            return ResponseEntity.ok(new JwtResponse(newToken, refreshToken, username, userDetailsImpl.getEmail(), userDetailsImpl.getRoles(), userDetailsImpl.getName()));
        }

        
}
