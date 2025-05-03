package com.online.exam.config.userDetailsService;


import com.online.exam.config.customUserDetails.CustomUserDetails;
import com.online.exam.repository.UserModelRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class CustomUserDetailsService implements UserDetailsService {
    private UserModelRepository userModelRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userModelRepository.findByUsername(username)
                .map(CustomUserDetails::new)
                .orElseThrow( () -> new UsernameNotFoundException("User not found !!!"));
    }
}
