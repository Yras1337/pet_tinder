package com.dream.pet_tinder.model.user;


import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


@Entity
@Table(name = "usr")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    private Status status;
    @Enumerated(value = EnumType.STRING)
    private Role role;
    private String email;
    private String password;
    private String name;

    private boolean isActive() {
        return status.equals(Status.ACTIVE);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getRole().getAuthorities();
    }

    public UserDetails getUserDetails() {
        return new org.springframework.security.core.userdetails.User(
                this.getUsername(),
                this.getPassword(),
                this.isAccountNonExpired(),
                this.isCredentialsNonExpired(),
                this.isAccountNonLocked(),
                this.isEnabled(),
                this.getRole().getAuthorities()
        );
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive();
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }
}
