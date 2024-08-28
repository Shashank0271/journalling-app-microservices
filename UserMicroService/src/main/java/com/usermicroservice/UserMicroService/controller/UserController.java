package com.usermicroservice.UserMicroService.controller;

import com.usermicroservice.UserMicroService.dtos.SignupDTO;
import com.usermicroservice.UserMicroService.entities.User;
import com.usermicroservice.UserMicroService.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@ModelAttribute SignupDTO signupDTO, @RequestParam(value = "file", required = false) MultipartFile profilePic) {
        User createdUser = userService.saveUserInDb(signupDTO, profilePic);
        return new ResponseEntity<>(createdUser, HttpStatus.OK);
    }

    @GetMapping("/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User requiredUser = userService.getUserByEmail(email);
        return new ResponseEntity<>(requiredUser, HttpStatus.OK);
    }

//    @PutMapping("/{userId}")
//    public ResponseEntity<User> updateUserByUserId(@PathVariable long userId, @RequestBody User updatedUser) {
//        User userInDb = userService.getUserByUserId(userId);
//        if (updatedUser.getUserName() != null)
//            userInDb.setUserName(updatedUser.getUserName());
//
//        if (updatedUser.getEmail() != null)
//            userInDb.setEmail(updatedUser.getEmail());
//
//        if (updatedUser.getAbout() != null)
//            userInDb.setAbout(updatedUser.getAbout());
//
//        return new ResponseEntity<>(userService.saveUserInDb(userInDb), HttpStatus.OK);
//    }


}
