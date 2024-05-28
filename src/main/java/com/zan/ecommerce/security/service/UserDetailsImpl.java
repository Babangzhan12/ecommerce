package com.zan.ecommerce.security.service;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zan.ecommerce.entity.Pengguna;

import lombok.Data;

@Data
public class UserDetailsImpl implements UserDetails {

    private String username;
    private String email;
    private String name;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private String roles;

    public UserDetailsImpl(String username, 
    String email, 
    String name, 
    String password, 
    String roles) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.password = password;
        this.roles = roles;
    }

    public static UserDetailsImpl build(Pengguna pengguna){
        return new UserDetailsImpl(
        pengguna.getId(), 
        pengguna.getEmail(),
        pengguna.getName(), 
        pengguna.getPassword(), 
        pengguna.getRoles());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities =  new ArrayList<>();
        if (StringUtils.hasText(roles)){
            String[] splits = roles.replaceAll(" ", "").split(",");
            for (String string : splits) {
                authorities.add(new SimpleGrantedAuthority(string));
            }
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }
    
}
