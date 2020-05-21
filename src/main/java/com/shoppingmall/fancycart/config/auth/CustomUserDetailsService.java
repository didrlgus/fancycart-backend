package com.shoppingmall.fancycart.config.auth;

import com.shoppingmall.fancycart.config.auth.dto.UserPrincipal;
import com.shoppingmall.fancycart.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<com.shoppingmall.fancycart.domain.user.User> userOpt = userRepository.findByEmail(email);
        com.shoppingmall.fancycart.domain.user.User user = userOpt.orElseThrow(NoSuchElementException::new);

        return UserPrincipal.create(user);
    }
}
