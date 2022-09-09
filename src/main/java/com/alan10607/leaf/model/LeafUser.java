package com.alan10607.leaf.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeafUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String userName;
    private String email;
    private String pw;

    @ManyToMany(fetch = FetchType.EAGER)//EAGER: 關聯的資料同時取出放入內存, LAZY: 關聯的資料不即時取出, 等要使用再處理
    private List<LeafRole> leafRole = new ArrayList<>();

    private LocalDateTime updatedDate;

    public LeafUser(String userName,
                    String email,
                    String pw,
                    List<LeafRole> leafRole,
                    LocalDateTime updatedDate) {
        this.userName = userName;
        this.email = email;
        this.pw = pw;
        this.leafRole = leafRole;
        this.updatedDate = updatedDate;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = leafRole
                .stream()
                .map(leafRole -> new SimpleGrantedAuthority(leafRole.getRoleName()))
                .collect(Collectors.toList());

        return authorities;
    }

    @Override
    public String getPassword() {
        return pw;
    }

    @Override
    public String getUsername() {
        return userName;
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