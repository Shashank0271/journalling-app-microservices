package com.example.authservice.services;

import com.example.authservice.dtos.AuthResponseDTO;
import com.example.authservice.dtos.LoginDTO;
import com.example.authservice.dtos.SignupDTO;
import com.example.authservice.dtos.UserDTO;
import com.example.authservice.entities.User;
import com.example.authservice.utils.ImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RestTemplate restTemplate;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public AuthResponseDTO login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
        );
        User currentUser = (User) authentication.getPrincipal();

        UserDTO currentUserDTO = modelMapper.map(currentUser, UserDTO.class);
        String jwtToken = jwtService.generateAccessToken(currentUserDTO);
        return AuthResponseDTO.builder()
                .jwtToken(jwtToken)
                .user(currentUserDTO)
                .build();
    }

    public AuthResponseDTO signUp(SignupDTO signupDTO, MultipartFile profilePic) {

        signupDTO.setPassword(passwordEncoder.encode(signupDTO.getPassword()));

        //headers
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        //body
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("username", signupDTO.getUsername());
        body.add("password", signupDTO.getPassword());
        body.add("email", signupDTO.getEmail());
        if (!profilePic.isEmpty()) {
            try {
                body.add("file", new FileSystemResource(ImageUtil.convertMultipartFileToFile(profilePic)));
            } catch (IOException e) {
                throw new RuntimeException("cant add image to request body");
            }
        }

        //request-entity
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<Object> registeredUserResponse = restTemplate.postForEntity("http://localhost:8080/user", httpEntity, Object.class);

        if (!registeredUserResponse.getStatusCode()
                .is2xxSuccessful()) {
            throw new RuntimeException("error in signing up the user");
        }


        UserDTO registeredUserDTO = modelMapper.map(registeredUserResponse.getBody(), UserDTO.class);

        String jwtToken = jwtService.generateAccessToken(registeredUserDTO);
        return AuthResponseDTO.builder()
                .jwtToken(jwtToken)
                .user(registeredUserDTO)
                .build();
    }

}

/*

the login and signup apis actually depend on how the auth UI on the client side is structured
if on both login and signup the same interface is presented : eg. email/password or otp
then while using firebase auth we were calling signUpWithCred function on firebase
which changes the authentication state of the user to logged in
and returns a new FirebaseUser with firebase auth credentials
and on the very next screen we would prompt the user to enter the required details like
username , age , address , etc so we can store the profile in DB.

here the login endpoint uses the authenticate method on the authenticationManager instance
which essentially uses the existing user (it fetches from the UserDetailsService)
and if details match then returns jwt -> so there has to be an existing user to authenticate it with

presently the signup api expects all the information of the new user to be
sent at once.

This is suitable for the interfaces in which the signup screen requires ALL
the user profile params along with the authentication credentials to be passed
at the same time . So the signup api will send the data to the user-service to store it
and create a jwt token and return it to the client , so there is no separate screen
shown to the client to upload the data.

Therefore the current apis work fine when the interface for signup and login on the client
side are DIFFERENT !

 */