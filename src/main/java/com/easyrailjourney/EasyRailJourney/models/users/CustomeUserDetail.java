package com.easyrailjourney.EasyRailJourney.models.users;

import java.util.Collection;
import java.util.stream.Stream;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;



@Data
public class CustomeUserDetail implements UserDetails {

    private final Users user;

    public CustomeUserDetail(Users user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    
        return user.getRoles()
                .stream()
                .flatMap(role -> {
                    Stream<GrantedAuthority> roleAuthority =
                            Stream.of(
                                    new SimpleGrantedAuthority(role.getName())
                            );
    
                    Stream<GrantedAuthority> permissions =
                            role.getRoleAuthorities()
                                    .stream()
                                    .map(ra ->
                                            new SimpleGrantedAuthority(
                                                    ra.getAuthority().getName()
                                            )
                                    );
    
                    return Stream.concat(roleAuthority, permissions);
                })
                .toList();
    }
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getProfileName();
    }
}