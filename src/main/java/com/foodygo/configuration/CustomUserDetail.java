package com.foodygo.configuration;

import com.foodygo.entity.Role;
import com.foodygo.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Getter
@Setter
@Builder
public class CustomUserDetail implements UserDetails {
    private int userID;
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String accessToken;
    private String refreshToken;
    private boolean nonLocked;
    private boolean enabled;
    private Collection<GrantedAuthority> grantedAuthorities;

    public static CustomUserDetail mapUserToUserDetail(User user) {
        Role role = user.getRole();
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role.getRoleName().name());
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(simpleGrantedAuthority);

        return CustomUserDetail.builder()
                .userID(user.getUserID())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phone(user.getPhone())
                .nonLocked(user.isNonLocked())
                .accessToken(user.getAccessToken())
                .refreshToken(user.getRefreshToken())
                .enabled(user.isEnabled())
                .grantedAuthorities(roles)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
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
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return nonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
