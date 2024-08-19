package com.zan.ecommerce.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zan.ecommerce.entity.Pesanan;
import com.zan.ecommerce.model.PesananRequest;
import com.zan.ecommerce.model.PesananResponse;
import com.zan.ecommerce.security.service.UserDetailsImpl;
import com.zan.ecommerce.service.PesananService;

@RestController
@RequestMapping("/api")
@PreAuthorize("isAuthenticated()")
public class PesananController {
    
    @Autowired
    private PesananService pesananService;

    @PostMapping("/pesanans")
    @PreAuthorize("hasAuthority('User')")
    public PesananResponse create(
        @AuthenticationPrincipal UserDetailsImpl user, @RequestBody PesananRequest request
    ){
        return pesananService.create(user.getUsername(), request);
    }

    @PatchMapping("/pesanans/{pesananId}/cancel")
    @PreAuthorize("hasAuthority('User')")
    public Pesanan cancelPesananUser(
        @AuthenticationPrincipal UserDetailsImpl user, @PathVariable("pesananId") String pesananId
    ){
        return pesananService.cancelPesanan(pesananId, user.getUsername());
    }

    @PatchMapping("/pesanans/{pesananId}/terima")
    @PreAuthorize("hasAuthority('User')")
    public Pesanan terimaPesanan(
        @AuthenticationPrincipal UserDetailsImpl user, @PathVariable("pesananId") String pesananId
    ){
        return pesananService.terimaPesanan(pesananId, user.getUsername());
    }

    @PatchMapping("/pesanans/{pesananId}/konfirmasi")
    @PreAuthorize("hasAuthority('Admin')")
    public Pesanan konfirmasi(
        @AuthenticationPrincipal UserDetailsImpl user, @PathVariable("pesananId") String pesananId
    ){
        return pesananService.konfirmasiPembayaran(pesananId, user.getUsername());
    }

    @PatchMapping("/pesanans/{pesananId}/packing")
    @PreAuthorize("hasAuthority('Admin')")
    public Pesanan packing(
        @AuthenticationPrincipal UserDetailsImpl user, @PathVariable("pesananId") String pesananId
    ){
        return pesananService.packing(pesananId, user.getUsername());
    }
    @PatchMapping("/pesanans/{pesananId}/kirim")
    @PreAuthorize("hasAuthority('Admin')")
    public Pesanan kirim(
        @AuthenticationPrincipal UserDetailsImpl user, @PathVariable("pesananId") String pesananId
    ){
        return pesananService.kirim(pesananId, user.getUsername());
    }

    @GetMapping("/pesanans")
    @PreAuthorize("hasAuthority('User')")
    public List<Pesanan> findAllPesananUser(@AuthenticationPrincipal UserDetailsImpl user, @RequestParam(name = "page", defaultValue = "0", required = false) int page, @RequestParam(name = "limit", defaultValue = "25", required = false) int limit){
        return pesananService.findAllPesananUser(user.getUsername(), page, limit);
    }

    @GetMapping("/pesanans/admin")
    @PreAuthorize("hasAuthority('Admin')")
    public List<Pesanan> searchh(@AuthenticationPrincipal UserDetailsImpl user, @RequestParam(name = "page", defaultValue = "0", required = false) int page,
    @RequestParam(name = "filterText", defaultValue = "", required = false) String filterText, @RequestParam(name = "limit", defaultValue = "25", required = false) int limit){
        return pesananService.seacrh(filterText, page, limit);
    }
    
}
