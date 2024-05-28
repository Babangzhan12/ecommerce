package com.zan.ecommerce.service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zan.ecommerce.entity.Keranjang;
import com.zan.ecommerce.entity.Pengguna;
import com.zan.ecommerce.entity.Product;
import com.zan.ecommerce.repository.KeranjangRepository;
import com.zan.ecommerce.repository.ProductRepository;
import com.zan.ecommerce.exception.BadRequestException;

@Service
public class KeranjangService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private KeranjangRepository keranjangRepository;

    @Transactional
    public Keranjang addKeranjang(String username, String productId, Double quantity) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException(
                        "Product ID " + productId + " tidak ditemukan"));

        Optional<Keranjang> optional = keranjangRepository.findByPenggunaIdAndProductId(username, productId);
        Keranjang keranjang;
        if (optional.isPresent()) {
            keranjang = optional.get();
            keranjang.setQuantity(keranjang.getQuantity() + quantity);
            keranjang.setJumlah(new BigDecimal(keranjang.getHarga().doubleValue() * keranjang.getQuantity()));
            keranjangRepository.save(keranjang);
        } else {
            keranjang = new Keranjang();
            keranjang.setId(UUID.randomUUID().toString());
            keranjang.setProduct(product);
            keranjang.setQuantity(quantity);
            keranjang.setHarga(product.getPrice());
            keranjang.setJumlah(new BigDecimal(keranjang.getHarga().doubleValue() * keranjang.getQuantity()));
            keranjang.setPengguna(new Pengguna(username));
            keranjangRepository.save(keranjang);
        }
        return keranjang;
    }

    @Transactional
    public Keranjang updateQuantity(String username, String productId, Double quantity) {
        Keranjang keranjang = keranjangRepository.findByPenggunaIdAndProductId(username, productId)
                .orElseThrow(() -> new BadRequestException(
                        "Product ID " + productId + " tidak ditemukan dalam keranjang anda"));
        keranjang.setQuantity(quantity);
        keranjang.setJumlah(new BigDecimal(keranjang.getHarga().doubleValue() * keranjang.getQuantity()));
        keranjangRepository.save(keranjang);
        return keranjang;
    }

    @Transactional
    public void delete(String username, String productId){
        Keranjang keranjang = keranjangRepository.findByPenggunaIdAndProductId(username, productId)
                .orElseThrow(() -> new BadRequestException(
                        "Product ID " + productId + " tidak ditemukan dalam keranjang anda"));
        keranjangRepository.delete(keranjang);
    }

    public List<Keranjang> findByPenggunaId(String username){
        return keranjangRepository.findByPenggunaId(username);
    }
}
