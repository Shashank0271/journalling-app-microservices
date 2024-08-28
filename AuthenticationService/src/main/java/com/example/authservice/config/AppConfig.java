package com.example.authservice.config;

import com.example.authservice.dtos.UserDTO;
import com.example.authservice.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(User.class, UserDTO.class).
                setConverter(
                        context -> {
                            User user = context.getSource();
                            return UserDTO.builder().
                                    username(user.getName()).
                                    id(user.getId()).
                                    email(user.getEmail()).
                                    password(user.getPassword()).
                                    profilePicture(user.getProfilePicture()).
                                    about(user.getAbout()).
                                    build();
                        }
                );
        return modelMapper;
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
