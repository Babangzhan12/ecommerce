package com.zan.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zan.ecommerce.entity.Keranjang;
import com.zan.ecommerce.model.KeranjangRequest;
import com.zan.ecommerce.security.service.UserDetailsImpl;
import com.zan.ecommerce.service.KeranjangService;

@RestController
@RequestMapping("/api")
@PreAuthorize("isAuthenticated()")
public class KeranjangController {

    @Autowired
    private KeranjangService keranjangService;
    
    @GetMapping("/keranjangs")
    public List<Keranjang> findByPenggunaId(@AuthenticationPrincipal UserDetailsImpl user){
        return keranjangService.findByPenggunaId(user.getUsername());
    }

    @PostMapping("/keranjangs")
    public Keranjang create(@AuthenticationPrincipal UserDetailsImpl user, @RequestBody KeranjangRequest request){
        return keranjangService.addKeranjang(user.getUsername(), request.getProductId(), request.getQuantity());
    }

    @PatchMapping("/keranjangs/{productId}")
    public Keranjang update(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable("productId") String productId, @RequestParam("quantity") Double quantity){
        return keranjangService.updateQuantity(user.getUsername(), productId, quantity);
    }

    @DeleteMapping("/keranjangs/{productId}")
    public void delete(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable("productId") String productId){
        keranjangService.delete(user.getUsername(), productId);
    }
}
