package com.example.authservice.config;

import com.example.authservice.dtos.UserDTO;
import com.example.authservice.entities.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerUserDetailsService implements UserDetailsService {

    private final RestTemplate restTemplate;
    private final ModelMapper modelMapper ;
    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        ResponseEntity<UserDTO> userResponseEntity = restTemplate.getForEntity("http://localhost:8080/user/" + email, UserDTO.class);
        log.debug(userResponseEntity.getBody().getUsername());
        if (!userResponseEntity.getStatusCode()
                .is2xxSuccessful()) {
            throw new RuntimeException("user with emailId " + email + " does not exist");
        }
        return modelMapper.map(userResponseEntity.getBody() , User.class);
    }
}
