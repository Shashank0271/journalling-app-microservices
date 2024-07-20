package com.usermicroservice.UserMicroService;

import com.usermicroservice.UserMicroService.entities.User;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Id> {
    User findById(long userId);
}
