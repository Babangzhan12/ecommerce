package com.zan.ecommerce.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zan.ecommerce.entity.Category;
import com.zan.ecommerce.exception.ResourceNotFoundException;
import com.zan.ecommerce.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category findById(String id){
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Kategori dengan id" + id + "tidak ditemukan"));
    }
    
    public List<Category> findAll(){
        return categoryRepository.findAll();
    }

    public Category create(Category category){
        category.setId(UUID.randomUUID().toString());
        return categoryRepository.save(category);
    }

    public Category edit(Category category){
        return categoryRepository.save(category);
    }

    public void deleteById(String id){
        categoryRepository.deleteById(id);
    }
}
