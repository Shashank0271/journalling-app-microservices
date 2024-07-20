package com.usermicroservice.UserMicroService.services;

import com.usermicroservice.UserMicroService.Exceptions.UserNotFoundException;
import com.usermicroservice.UserMicroService.UserRepository;
import com.usermicroservice.UserMicroService.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository ;

    public User saveUserInDb(User user){
       return userRepository.save(user);
    }

    public User getUserByUserId(long userId){
        User requiredUser = userRepository.findById(userId);
        if(requiredUser == null){
            throw new UserNotFoundException(userId) ;
        }
        return requiredUser ;
    }

}
