package com.zan.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zan.ecommerce.entity.Keranjang;

import java.util.List;
import java.util.Optional;

public interface KeranjangRepository extends JpaRepository<Keranjang, String>{
    
    Optional<Keranjang> findByPenggunaIdAndProductId(String username, String productId);

    List<Keranjang> findByPenggunaId(String username);
}
