package com.naren.authservice.security;

import com.naren.authservice.entity.UserAccount;
import com.naren.authservice.repository.UserAccountRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    public CustomUserDetailsService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount account = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with username: " + username
                ));

        var authorities = account.getRoles().stream()
                .flatMap(role -> {
                    var roleAuthority = new SimpleGrantedAuthority("ROLE_" + role.getName());
                    var permissionAuthorities = role.getPermissions().stream()
                            .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                            .toList();
                    var all = new java.util.ArrayList<SimpleGrantedAuthority>();
                    all.add(roleAuthority);
                    all.addAll(permissionAuthorities);
                    return all.stream();
                })
                .collect(Collectors.toList());

        return new User(
                account.getUsername(),
                account.getPassword(),
                account.getEnabled(),
                true,
                true,
                !account.getLocked(),
                authorities
        );
    }
}
