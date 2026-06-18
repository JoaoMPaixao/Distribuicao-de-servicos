package com.example.backend.Security;

import com.example.backend.Entity.Utilizador;
import com.example.backend.Enums.EstadoUtilizador;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final Integer id;
    private final String email;
    private final String password;
    private final boolean ativo;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Integer departamentoId;
    private final String nome;

    public CustomUserDetails(Utilizador utilizador) {
        this.id = utilizador.getId();
        this.nome = utilizador.getNome();
        this.email = utilizador.getEmail();
        this.password = utilizador.getPassword();
        this.departamentoId = utilizador.getDepartamento() != null ? utilizador.getDepartamento().getId() : null;
        this.ativo = utilizador.getEstado() == EstadoUtilizador.ATIVO;
        if (utilizador.getCargo() != null) {
            this.authorities = Collections.singletonList(
                    new SimpleGrantedAuthority(utilizador.getCargo().getTipo())
            );
        } else {
            this.authorities = Collections.emptyList();
        }
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Integer getDepartamentoId() {
        return departamentoId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return ativo; // Se for false, o Spring Security bloqueia o login
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }

}
