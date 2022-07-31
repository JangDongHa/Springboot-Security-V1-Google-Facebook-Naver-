package com.example.securitydemo.config.auth;

import com.example.securitydemo.model.User;
import com.example.securitydemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어짐
@Service
public class PrincipalDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(IllegalArgumentException::new);
        PrincipalDetails principalDetails = new PrincipalDetails();
        principalDetails.setUser(user);
        return principalDetails;
    }
}
