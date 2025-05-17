package com.online.exam.config.customAuthorities;

import com.online.exam.enums.Authorities;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
@AllArgsConstructor
public class CustomAuthorityMapper implements GrantedAuthority {
    private Authorities authority;
    @Override
    public String getAuthority() {
        return "ROLE_"+authority.name();
    }
}
