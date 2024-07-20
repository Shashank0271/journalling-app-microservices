package com.usermicroservice.UserMicroService.controller;

import com.usermicroservice.UserMicroService.entities.User;
import com.usermicroservice.UserMicroService.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.saveUserInDb(user);
        return new ResponseEntity<>(createdUser, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserByUserId(@PathVariable long userId) {
        User requiredUser = userService.getUserByUserId(userId);
        return new ResponseEntity<>(requiredUser, HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUserByUserId(@PathVariable long userId, @RequestBody User updatedUser) {
        User userInDb = userService.getUserByUserId(userId);
        if (updatedUser.getUserName() != null)
            userInDb.setUserName(updatedUser.getUserName());

        if (updatedUser.getEmail() != null)
            userInDb.setEmail(updatedUser.getEmail());

        if (updatedUser.getAbout() != null)
            userInDb.setAbout(updatedUser.getAbout());

        return new ResponseEntity<>(userService.saveUserInDb(userInDb), HttpStatus.OK);
    }


}
