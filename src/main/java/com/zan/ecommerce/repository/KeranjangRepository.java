package com.zan.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zan.ecommerce.entity.Keranjang;

public interface KeranjangRepository extends JpaRepository<Keranjang, String>{
    
}
