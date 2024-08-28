package com.usermicroservice.UserMicroService.services;

import com.usermicroservice.UserMicroService.Exceptions.UserNotFoundException;
import com.usermicroservice.UserMicroService.UserRepository;
import com.usermicroservice.UserMicroService.dtos.SignupDTO;
import com.usermicroservice.UserMicroService.entities.Image;
import com.usermicroservice.UserMicroService.entities.User;
import com.usermicroservice.UserMicroService.utils.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger("UserService");
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final ModelMapper modelMapper;

    public User saveUserInDb(SignupDTO signupDTO, MultipartFile profilePic) {

        User user = modelMapper.map(signupDTO, User.class);

        user.setId(UUID.randomUUID()
                .toString());

        User savedUser = userRepository.save(user);

        if (profilePic != null) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            File profilePicFile;

            try {
                profilePicFile = ImageUtil.convertMultipartFileToFile(profilePic);
                body.add("file", new FileSystemResource(profilePicFile));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            body.add("userId", savedUser.getId());
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, httpHeaders);
            ResponseEntity<Image> imageUploadResponse = restTemplate.postForEntity("http://localhost:9000/image/user", httpEntity, Image.class);
            savedUser.setProfilePicture(imageUploadResponse.getBody());
            ImageUtil.deleteFile(profilePicFile);
        }

        return savedUser;
    }

    public User getUserByEmail(String email) {
        User requiredUser = userRepository.findByEmail(email);
        if (requiredUser == null) {
            throw new UserNotFoundException(email);
        }
        String userId = requiredUser.getId();
        Image userProfileImage = restTemplate.getForEntity("http://localhost:9000/image/user/" + userId, Image.class)
                .getBody();
        requiredUser.setProfilePicture(userProfileImage);
        return requiredUser;
    }

}
