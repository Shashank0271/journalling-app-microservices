package com.usermicroservice.UserMicroService;

import com.usermicroservice.UserMicroService.entities.User;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Id> {
    User findById(String userId);

    User findByEmail(String email);
}
