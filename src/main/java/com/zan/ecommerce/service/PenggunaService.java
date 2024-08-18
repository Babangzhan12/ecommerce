package com.zan.ecommerce.service;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.zan.ecommerce.entity.Pengguna;
import com.zan.ecommerce.exception.BadRequestException;
import com.zan.ecommerce.exception.ResourceNotFoundException;
import com.zan.ecommerce.repository.PenggunaRepository;

@Service
public class PenggunaService {

    @Autowired
    private PenggunaRepository penggunaRepository;

    public Pengguna findById(String id) {
        return penggunaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pengguna dengan id " + id + " tidak ditemukan"));
    }

    public List<Pengguna> findAll() {
        return penggunaRepository.findAll();
    }

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    private boolean isValidEmail(String email) {
            return EMAIL_PATTERN.matcher(email).matches();
        }

    public Pengguna create(Pengguna pengguna) {
        if (!StringUtils.hasText(pengguna.getId())) {
            throw new BadRequestException("Username harus diisi");
        }
        if (penggunaRepository.existsById(pengguna.getId())) {
            throw new BadRequestException("Username " + pengguna.getId() + " sudah terdaftar");
        }
        if (!StringUtils.hasText(pengguna.getEmail())) {
            throw new BadRequestException("Email harus diisi");
        }
        if(!isValidEmail(pengguna.getEmail())){
            throw new BadRequestException("Email tidak valid");
        }
        if (penggunaRepository.existsByEmail(pengguna.getEmail())) {
            throw new BadRequestException("Email " + pengguna.getEmail() + " sudah terdaftar");
        }

        pengguna.setIsAktif(true);
        return penggunaRepository.save(pengguna);
    }

    public Pengguna edit(Pengguna pengguna) {
        if (!StringUtils.hasText(pengguna.getId())) {
            throw new BadRequestException("Username harus diisi");
        }
        if (!StringUtils.hasText(pengguna.getEmail())) {
            throw new BadRequestException("Email harus diisi");
        }
        return penggunaRepository.save(pengguna);
    }

    public void deleteById(String id) {
        penggunaRepository.deleteById(id);
    }
}
