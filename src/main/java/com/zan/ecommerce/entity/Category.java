package com.zan.ecommerce.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Category implements Serializable {
    
    @Id
    private String id;

    private String name;
}
