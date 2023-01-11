package com.example.normalboard.dto.security;

import com.example.normalboard.dto.UserAccountDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class BoardPrincipal implements UserDetails {
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private String email;
    private String nickname;
    private String memo;


    public static BoardPrincipal of(String username, String password, String email, String nickname, String memo) {
            Set<RoleType> roleTypes = Set.of(RoleType.USER);
            return new BoardPrincipal(
                    username,
                    password,
                    roleTypes.stream()
                            .map(RoleType::getName)
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toUnmodifiableSet()),
                    email,
                    nickname, memo);
    }

    public static BoardPrincipal from(UserAccountDto dto) {
        return BoardPrincipal.of(
                dto.getUserId(),
                dto.getUserPassword(),
                dto.getEmail(),
                dto.getNickname(),
                dto.getMemo());
    }

    public UserAccountDto toDto() {
            return UserAccountDto.of(
            username, password, email, nickname, memo);
    }


    @Override public String getUsername() { return username; }
    @Override public String getPassword() { return password; }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }


    public enum RoleType {
        USER("ROLE_USER");

        @Getter
        private final String name;

        RoleType(String name) {
            this.name = name;
        }
    }

}
