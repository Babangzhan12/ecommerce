package com.zan.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zan.ecommerce.entity.Product;
import com.zan.ecommerce.service.ProductService;

@RestController
@RequestMapping("/api")
@PreAuthorize("isAuthenticated()")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public List<Product> findall(){
        return productService.findAll();
    }

    @GetMapping("/products/{id}")
    public Product findById(@PathVariable("id") String id){
        return productService.findById(id);
    }

    @PostMapping("/products")
    public Product create(@RequestBody Product Product){
        return productService.create(Product);
    }

    @PutMapping("/products")
    public Product edit(@RequestBody Product Product) {
        return productService.edit(Product);
    }

    @DeleteMapping("/products/{id}")
    public void deleteById(@PathVariable("id") String id){
        productService.deleteById(id);
    }
}
