package com.online.exam.config.customAuthorities;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
@AllArgsConstructor
public class CustomAuthorityMapper implements GrantedAuthority {
    private String authority;
    @Override
    public String getAuthority() {
        return "ROLE_"+authority;
    }
}
