package com.zan.ecommerce.model;

import java.io.Serializable;
import lombok.Data;

@Data
public class JwtResponse implements Serializable{
    private String token;
    private String refreshToken;
    private String type = "Bearer";
    private String username;
    private String email;
    private String role;
    private String nama;

    public JwtResponse(
        String accesToken,
        String refreshToken,
        String username,
        String email,
        String role,
        String nama
    ){
        this.username = username;
        this.email = email;
        this.token = accesToken;
        this.refreshToken = refreshToken;
        this.role = role;
        this.nama = nama;
    }
}
