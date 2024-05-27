package com.zan.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zan.ecommerce.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {
    
}
