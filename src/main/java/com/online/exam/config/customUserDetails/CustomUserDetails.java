package com.online.exam.config.customUserDetails;


import com.online.exam.config.customAuthorities.CustomAuthorityMapper;
import com.online.exam.model.UserModel;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final UserModel userModel;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userModel.getRoles().stream()
                .map(role -> new CustomAuthorityMapper(role.getAuthorityName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return userModel.getPassword();
    }

    @Override
    public String getUsername() {
        return userModel.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
