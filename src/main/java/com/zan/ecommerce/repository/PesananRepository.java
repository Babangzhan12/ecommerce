package com.zan.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zan.ecommerce.entity.Pesanan;

public interface PesananRepository extends JpaRepository<Pesanan, String> {
    
}
