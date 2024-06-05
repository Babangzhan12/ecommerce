package com.zan.ecommerce.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import com.zan.ecommerce.entity.Pesanan;
import com.zan.ecommerce.entity.PesananItem;

import lombok.Data;

@Data
public class PesananResponse implements Serializable{
    private String id;
    private String nomorPesanan;
    private Date tanggal;
    private String namaPelanggan;
    private String alamatPengiriman;
    private Date waktuPesan;
    private BigDecimal jumlah;
    private BigDecimal ongkir;
    private BigDecimal total;
    private List<PesananResponse.Item> items;

    public PesananResponse(Pesanan pesanan, List<PesananItem> pesananItems){
        this.id = pesanan.getId();
        this.nomorPesanan = pesanan.getNomor();
        this.tanggal = pesanan.getTanggal();
        this.namaPelanggan = pesanan.getPengguna().getName();
        this.alamatPengiriman = pesanan.getAddress();
        this.waktuPesan = pesanan.getWaktuPesanan();
        this.jumlah = pesanan.getJumlah();
        this.ongkir = pesanan.getOngkir();
        this.total = pesanan.getTotal();
        items = new ArrayList<>();
        for (PesananItem pesananItem : pesananItems) {
            Item item = new Item();
            item.setProductId(pesananItem.getProduct().getId());
            item.setNamaProduct(pesananItem.getDescription());
            item.setQuantity(pesananItem.getQuantity());
            item.setHarga(pesananItem.getHarga());
            item.setJumlah(pesananItem.getJumlah());
            items.add(item);
        }
    }

    @Data
    public static class Item implements Serializable{
        private String productId;
        private String namaProduct;
        private Double quantity;
        private BigDecimal harga;
        private BigDecimal jumlah;
    }
}
