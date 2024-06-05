package com.zan.ecommerce.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

import com.zan.ecommerce.exception.BadRequestException;
import com.zan.ecommerce.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.zan.ecommerce.entity.Pengguna;
import com.zan.ecommerce.entity.Pesanan;
import com.zan.ecommerce.entity.PesananItem;
import com.zan.ecommerce.entity.Product;
import com.zan.ecommerce.model.KeranjangRequest;
import com.zan.ecommerce.model.PesananRequest;
import com.zan.ecommerce.model.PesananResponse;
import com.zan.ecommerce.model.StatusPesanan;
import com.zan.ecommerce.repository.PesananItemRepository;
import com.zan.ecommerce.repository.PesananRepository;
import com.zan.ecommerce.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class PesananService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PesananRepository pesananRepository;
    @Autowired
    private PesananItemRepository pesananItemRepository;
    @Autowired
    private KeranjangService keranjangService;
    @Autowired
    private PesananLogService pesananLogService;

    @Transactional
    public PesananResponse create(String username, PesananRequest request) {
        Pesanan pesanan = new Pesanan();
        pesanan.setId(UUID.randomUUID().toString());
        pesanan.setTanggal(new Date());
        pesanan.setNomor(genarateNomorPesanan());
        pesanan.setPengguna(new Pengguna(username));
        pesanan.setAddress(request.getAlamatPengiriman());
        pesanan.setStatusPesanan(StatusPesanan.DRAFT);
        pesanan.setWaktuPesanan(new Date());

        List<PesananItem> items = new ArrayList<>();
        for (KeranjangRequest k : request.getItems()) {
            Product product = productRepository.findById(k.getProductId())
                    .orElseThrow(() -> new BadRequestException("Product Id " + k.getProductId() + " tidak ditemukan"));
            if (product.getStock() < k.getQuantity()) {
                throw new BadRequestException("Stock tidak mencukupi");
            }
            PesananItem pi = new PesananItem();
            pi.setId(UUID.randomUUID().toString());
            pi.setProduct(product);
            pi.setDescription(product.getName());
            pi.setQuantity(k.getQuantity());
            pi.setHarga(product.getPrice());
            pi.setJumlah(new BigDecimal(pi.getHarga().doubleValue() * pi.getQuantity()));
            pi.setPesanan(pesanan);
            items.add(pi);
        }

        BigDecimal jumlah = BigDecimal.ZERO;
        for (PesananItem pesananItem : items) {
            jumlah = jumlah.add(pesananItem.getJumlah());
        }
        pesanan.setJumlah(jumlah);
        pesanan.setOngkir(request.getOngkir());
        pesanan.setTotal(pesanan.getJumlah().add(pesanan.getOngkir()));
        Pesanan saved = pesananRepository.save(pesanan);
        for (PesananItem pesananItem : items) {
            pesananItemRepository.save(pesananItem);
            Product product = pesananItem.getProduct();
            product.setStock(product.getStock() - pesananItem.getQuantity());
            productRepository.save(product);
            keranjangService.delete(username, product.getId());
        }
        // catat log
        pesananLogService.createLog(username, pesanan, PesananLogService.DRAFT, "Pesanan sukses dibuat");
        PesananResponse pesananResponse = new PesananResponse(saved, items);
        return pesananResponse;
    }

    @Transactional
    public Pesanan cancelPesanan(String pesananId, String userId) {
        Pesanan pesanan = pesananRepository.findById(pesananId)
                .orElseThrow(() -> new ResourceNotFoundException("Pesanan ID " + pesananId + " tidak ditemukan"));

        if (!userId.equals(pesanan.getPengguna().getId())) {
            throw new BadRequestException("Pesanan ini hanya dapat dibatalkan oleh yang bersangkutan");
        }

        if (!StatusPesanan.DRAFT.equals(pesanan.getStatusPesanan())) {
            throw new BadRequestException("Pesanan ini tidak dapat dibatalkan karena sudah dibatalkan sebelumnya");
        }

        pesanan.setStatusPesanan(StatusPesanan.DIBATALKAN);
        Pesanan saved = pesananRepository.save(pesanan);
        pesananLogService.createLog(userId, saved, PesananLogService.DIBATALKAN, "Pesanan sukses dibatalkan");
        return saved;
    }

    @Transactional
    public Pesanan terimaPesanan(String pesananId, String userId) {
        Pesanan pesanan = pesananRepository.findById(pesananId)
                .orElseThrow(() -> new ResourceNotFoundException("Pesanan ID " + pesananId + " tidak ditemukan"));
        if (!userId.equals(pesanan.getPengguna().getId())) {
            throw new BadRequestException("Pesanan ini hanya dapat dibatalkan oleh yang bersangkutan");
        }
        if (!StatusPesanan.PENGIRIMAN.equals(pesanan.getStatusPesanan())) {
            throw new BadRequestException(
                    "Penerimaan gagal, status pesanan saat ini adalah " + pesanan.getStatusPesanan().name());
        }
        pesanan.setStatusPesanan(StatusPesanan.DIBATALKAN);
        Pesanan saved = pesananRepository.save(pesanan);
        pesananLogService.createLog(userId, saved, PesananLogService.DIBATALKAN, "Pesanan sukses dibatalkan");
        return saved;
    }

    public List<Pesanan> findAllPesananUser(String userId, int page, int limit) {
        return pesananRepository.findByPenggunaId(userId,
                PageRequest.of(page, limit, Sort.by("waktuPesanan").descending()));
    }

    public List<Pesanan> seacrh(String filterText, int page, int limit) {
        return pesananRepository.search(filterText.toLowerCase(),
                PageRequest.of(page, limit, Sort.by("waktuPesanan").descending()));
    }

    @Transactional
    public Pesanan konfirmasiPembayaran(String pesananId, String userId) {
        Pesanan pesanan = pesananRepository.findById(pesananId)
                .orElseThrow(() -> new ResourceNotFoundException("Pesanan ID " + pesananId + " tidak ditemukan"));
        if (!StatusPesanan.DRAFT.equals(pesanan.getStatusPesanan())) {
            throw new BadRequestException(
                    "Konfirmasi pesanan gagal, status pesanan saat ini adalah " + pesanan.getStatusPesanan().name());
        }
        pesanan.setStatusPesanan(StatusPesanan.PEMBAYARAN);
        Pesanan saved = pesananRepository.save(pesanan);
        pesananLogService.createLog(userId, saved, PesananLogService.PEMBAYARAN, "Pembayaran sukses dikonfirmasi");
        return saved;
    }

    @Transactional
    public Pesanan packing(String pesananId, String userId) {
        Pesanan pesanan = pesananRepository.findById(pesananId)
                .orElseThrow(() -> new ResourceNotFoundException("Pesanan ID " + pesananId + " tidak ditemukan"));
        if (!StatusPesanan.PEMBAYARAN.equals(pesanan.getStatusPesanan())) {
            throw new BadRequestException(
                    "Packing pesanan gagal, status pesanan saat ini adalah " + pesanan.getStatusPesanan().name());
        }
        pesanan.setStatusPesanan(StatusPesanan.PACKING);
        Pesanan saved = pesananRepository.save(pesanan);
        pesananLogService.createLog(userId, saved, PesananLogService.PACKING, "Pesanan sedang disiapkan");
        return saved;
    }

    @Transactional
    public Pesanan kirim(String pesananId, String userId) {
        Pesanan pesanan = pesananRepository.findById(pesananId)
                .orElseThrow(() -> new ResourceNotFoundException("Pesanan ID " + pesananId + " tidak ditemukan"));
        if (!StatusPesanan.PACKING.equals(pesanan.getStatusPesanan())) {
            throw new BadRequestException(
                    "Pengiriman pesanan gagal, status pesanan saat ini adalah " + pesanan.getStatusPesanan().name());
        }
        pesanan.setStatusPesanan(StatusPesanan.PENGIRIMAN);
        Pesanan saved = pesananRepository.save(pesanan);
        pesananLogService.createLog(userId, saved, PesananLogService.PENGIRIMAN, "Pesanan sedang dikirim");
        return saved;
    }

    private String genarateNomorPesanan() {
        String uuidPart = UUID.randomUUID().toString().replace("-", "").substring(0, 13);
        String nanoTimePart = String.format("%03d", System.nanoTime() % 1000);
        return uuidPart + nanoTimePart;
    }
}
