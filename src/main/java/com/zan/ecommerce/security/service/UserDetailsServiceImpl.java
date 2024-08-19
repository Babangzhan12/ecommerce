package com.zan.ecommerce.security.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.zan.ecommerce.entity.Pengguna;
import com.zan.ecommerce.repository.PenggunaRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PenggunaRepository penggunaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final int LOCK_TIME_DURATION = 30;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Pengguna pengguna = penggunaRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " tidak ditemukan"));

        if (pengguna.getAccountNonLockedUntil() != null
                && pengguna.getAccountNonLockedUntil().isAfter(LocalDateTime.now())) {
            throw new LockedException("Account is locked due to multiple failed login attempts.");
        }

        return UserDetailsImpl.build(pengguna);
    }

    public void authenticate(String username, String password) {
        Pengguna pengguna = penggunaRepository.findById(username)
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password."));

        // Log debug
        System.out.println("User found: " + pengguna.getId());

        if (pengguna.getAccountNonLockedUntil() != null
                && pengguna.getAccountNonLockedUntil().isAfter(LocalDateTime.now())) {
            throw new LockedException("Account is locked due to multiple failed login attempts.");
        }

        if (!passwordEncoder.matches(password, pengguna.getPassword())) {
            handleFailedLogin(pengguna);
            throw new BadCredentialsException("Invalid username or password.");
        }

        // Log debug
        System.out.println("Password matches. Resetting failed login attempts.");

        resetFailedLoginAttempts(pengguna);
    }

    private void handleFailedLogin(Pengguna pengguna) {
        int failedAttempts = pengguna.getFailedLoginAttempts() + 1;
        pengguna.setFailedLoginAttempts(failedAttempts);

        System.out.println("Failed attempts: " + failedAttempts);

        if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
            pengguna.setAccountNonLockedUntil(LocalDateTime.now().plusMinutes(LOCK_TIME_DURATION));
        }

        penggunaRepository.save(pengguna);
    }

    private void resetFailedLoginAttempts(Pengguna pengguna) {
        pengguna.setFailedLoginAttempts(0);
        pengguna.setAccountNonLockedUntil(null);
        penggunaRepository.save(pengguna);
        System.out.println("Failed attempts reset.");
    }

}
