package com.example.authservice.config;

import com.example.authservice.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {

    private final RestTemplate restTemplate;

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        ResponseEntity<User> userResponseEntity = restTemplate.getForEntity("http://localhost:8080/user/" + email, User.class);
        if (!userResponseEntity.getStatusCode()
                .is2xxSuccessful()) {
            throw new RuntimeException("user with emailId " + email + " does not exist");
        }
        return userResponseEntity.getBody();
    }
}
