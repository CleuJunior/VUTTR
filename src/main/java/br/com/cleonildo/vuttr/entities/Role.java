package br.com.cleonildo.vuttr.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;


public enum Role {

    ADMIN(Authority.ADMIN_AUTHORITIES),
    USER(Authority.USER_AUTHORITIES);

    private final List<GrantedAuthority> grantedAuthorities;

    Role(List<GrantedAuthority> grantedAuthorities) {
        this.grantedAuthorities = grantedAuthorities;
    }

    public List<GrantedAuthority> getGrantedAuthorities() {
        return grantedAuthorities;
    }

    private static final class Authority {
        private static final GrantedAuthority ADMIN = new SimpleGrantedAuthority("ADMIN");
        private static final GrantedAuthority USER = new SimpleGrantedAuthority("USER");
        private static final List<GrantedAuthority> ADMIN_AUTHORITIES = List.of(ADMIN, USER);
        private static final List<GrantedAuthority> USER_AUTHORITIES = List.of(USER);
    }

}
