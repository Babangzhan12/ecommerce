package com.zan.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zan.ecommerce.entity.Pengguna;


public interface PenggunaRepository extends JpaRepository<Pengguna, String>{

    boolean existsByEmail(String email);
   
}
