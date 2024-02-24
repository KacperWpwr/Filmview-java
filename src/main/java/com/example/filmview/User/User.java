package com.example.filmview.User;

import com.example.filmview.Rating.Rating;
import com.fasterxml.jackson.annotation.JsonTypeId;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "AppUser")
@Getter@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    private String login;

    @Column
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false)
    private Boolean is_enabled;

    @Column(nullable = false)
    private Boolean is_blocked;

    @OneToMany(orphanRemoval = true, mappedBy = "rating_user")
    private List<Rating> ratings;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.toString()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !is_blocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return is_enabled;
    }
}
