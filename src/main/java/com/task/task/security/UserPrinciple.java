package com.task.task.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.task.task.Roles;
import com.task.task.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Created by 1 on 04.06.2019.
 */
public class UserPrinciple implements UserDetails {

    private static final long serialVersionUID = 1574386194951783012L;

    private Long id;

    private String username;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserPrinciple(Long id, String username, String password,
                         Collection<? extends GrantedAuthority> authorities) {
        super();
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public static UserPrinciple build(User userEntity) {
          List<GrantedAuthority> authorities = new ArrayList<>();
          authorities.add(new SimpleGrantedAuthority(Roles.USER.getValue()));
        return new UserPrinciple(userEntity.getId(), userEntity.getEmail(), userEntity.getPassword(), authorities);
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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

    @Override
    public boolean equals(Object obj) {
        UserPrinciple user = (UserPrinciple) obj;
        return Objects.equals(id, user.id);
    }

}
