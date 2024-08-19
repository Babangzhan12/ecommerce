package com.zan.ecommerce.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtResponse implements Serializable{
    private String token;
    private String refreshToken;
    private String type = "Bearer";
    private String username;
    private String email;
    private String role;
    private String nama;
    private boolean success;
    private String errorMessage;

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
        this.success = true;
        this.errorMessage = null;
    }

    public JwtResponse(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public String getToken() {
        return success ? token : null;
    }

    public String getRefreshToken() {
        return success ? refreshToken : null;
    }

    public String getType() {
        return success ? type : null;
    }

    public String getUsername() {
        return success ? username : null;
    }

    public String getEmail() {
        return success ? email : null;
    }

    public String getRole() {
        return success ? role : null;
    }

    public String getNama() {
        return success ? nama : null;
    }
}
