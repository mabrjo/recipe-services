package com.mcccodeschool.recipeservices.security;

import com.mcccodeschool.recipeservices.model.User;
import com.mcccodeschool.recipeservices.model.User2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class User2Principal implements OAuth2User {
    private Long id;
    private String email;
    private String username;
    private String provider;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public User2Principal(Long id, String email, String provider, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.provider = provider;
        this.authorities = authorities;
    }

    public static User2Principal create(User2 user) {
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        return new User2Principal(
                user.getId(),
                user.getEmail(),
                user.getProvider(),
                authorities
        );
    }

    public static User2Principal create(User2 user, Map<String, Object> attributes) {
        User2Principal userPrincipal = User2Principal.create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
    public String getUsername() {
        return email;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }
}
