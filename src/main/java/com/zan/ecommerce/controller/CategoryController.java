package com.zan.ecommerce.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zan.ecommerce.entity.Category;
import com.zan.ecommerce.service.CategoryService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public List<Category> findall(){
        return categoryService.findAll();
    }

    @GetMapping("/categories/{id}")
    public Category findById(@PathVariable("id") String id){
        return categoryService.findById(id);
    }

    @PostMapping("/categories")
    public Category create(@RequestBody Category category){
        return categoryService.create(category);
    }

    @PutMapping("/categories")
    public Category edit(@RequestBody Category category) {
        return categoryService.edit(category);
    }

    @DeleteMapping("/categories/{id}")
    public void deleteById(@PathVariable("id") String id){
        categoryService.deleteById(id);
    }
}
