package com.zan.ecommerce.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Pengguna implements Serializable {
    
    @Id
    private String id;
    private String password;
    private String name;
    private String address;
    private String email;
    private String hp;
    private String roles;
    private Boolean isAktif;
}
