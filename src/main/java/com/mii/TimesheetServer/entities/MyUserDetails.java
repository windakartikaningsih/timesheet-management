/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author Fahri
 */
public class MyUserDetails implements UserDetails {

    private User user;

    @Autowired
    public MyUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        user.getUserRoleList().forEach((role) -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRole().getRoleName().toUpperCase()));
        });
        user.getUserPermissionList().forEach((permission) -> {
            authorities.add(new SimpleGrantedAuthority(permission.getPermission().getPermissionName().toUpperCase()));
        });
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.getIsVerified();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getIsVerified();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.getIsVerified();
    }

    @Override
    public boolean isEnabled() {
        return user.getIsVerified();
    }

}
