package com.zan.ecommerce.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Pengguna implements Serializable {

    @Id
    private String id;
    @JsonIgnore
    private String password;
    private String name;
    @JsonIgnore
    private String address;
    @JsonIgnore
    private String email;
    @JsonIgnore
    private String hp;
    @JsonIgnore
    private String roles;
    @JsonIgnore
    private Boolean isAktif;
    private int failedLoginAttempts = 0;
    @Column(name = "account_non_locked_until")
    private LocalDateTime accountNonLockedUntil;

    public Pengguna(String username) {
        this.id = username;
    }
}
