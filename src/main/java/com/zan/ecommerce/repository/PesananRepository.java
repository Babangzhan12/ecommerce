package com.zan.ecommerce.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zan.ecommerce.entity.Pesanan;

public interface PesananRepository extends JpaRepository<Pesanan, String> {

    List<Pesanan> findByPenggunaId(String userId, Pageable pageable);

    @Query("SELECT p FROM Pesanan p WHERE LOWER(p.nomor) LIKE %:filterText% OR LOWER(p.pengguna.name) LIKE %:filterText%")
    List<Pesanan> search(@Param("filterText") String filterText, Pageable pageable);
    
}
