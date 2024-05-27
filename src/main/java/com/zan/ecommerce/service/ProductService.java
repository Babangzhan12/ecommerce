package com.zan.ecommerce.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.zan.ecommerce.entity.Product;
import com.zan.ecommerce.exception.BadRequestException;
import com.zan.ecommerce.exception.ResourceNotFoundException;
import com.zan.ecommerce.repository.CategoryRepository;
import com.zan.ecommerce.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product dengan id " + id + "tidak ditemukan"));
    }

    public Product create(Product product) {
        if (!StringUtils.hasText(product.getName())){
            throw new BadRequestException("Nama product tidak boleh kosong");
        }
        if (product.getCategory() == null){
            throw new BadRequestException("Category tidak boleh kosong");
        }
        if (!StringUtils.hasText(product.getCategory().getId())){
            throw new BadRequestException("Kategori ID tidak boleh kosong");
        }

        categoryRepository.findById(product.getCategory().getId())
        .orElseThrow(()-> new BadRequestException(
            "Category ID" + product.getCategory().getId() + "tidak ditemukan dalam database"));
        product.setId(UUID.randomUUID().toString());
        return productRepository.save(product);
    }

    public Product edit(Product product) {
        if (!StringUtils.hasText(product.getId())){
            throw new BadRequestException("Product Id tidak boleh kosong");
        }
        if (!StringUtils.hasText(product.getName())){
            throw new BadRequestException("Nama product tidak boleh kosong");
        }
        if (product.getCategory() == null){
            throw new BadRequestException("Category tidak boleh kosong");
        }
        if (!StringUtils.hasText(product.getCategory().getId())){
            throw new BadRequestException("Kategori ID tidak boleh kosong");
        }

        categoryRepository.findById(product.getCategory().getId())
        .orElseThrow(()-> new BadRequestException(
            "Category ID" + product.getCategory().getId() + "tidak ditemukan dalam database"));
        return productRepository.save(product);
    }

    public Product ubahGambar(String id, String gambar) {
        Product product = findById(id);
        product.setGambar(gambar);
        return productRepository.save(product);
    }

    public void deleteById(String id) {
        productRepository.deleteById(id);
    }

    // public List<Product> findByRange(String filterText, int page, int limit){

    // }
}
