package com.image.ImageMicroservice.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.image.ImageMicroservice.entities.Image;
import com.image.ImageMicroservice.entities.ImageCat;
import com.image.ImageMicroservice.repository.ImageRepository;
import jakarta.validation.constraints.AssertFalse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class CloudinaryService {
    @Autowired
    private Cloudinary cloudinary ;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ImageRepository imageRepository ;


    @Async
    public CompletableFuture<Image> uploadImageAndSaveInDB(File fileToUpload , String journalEntryId , long userId){
        try {
            log.trace("Uploading an image to cloudinary : {}" , Thread.currentThread());
            Map<String , Object> uploadResponse =  objectMapper.convertValue(cloudinary.uploader()
                    .upload(fileToUpload, ObjectUtils.emptyMap()), new TypeReference<Map<String, Object>>() {});

            Image image = Image.builder()
                    .imageCategory(ImageCat.JOURNAL_ENTRY)
                    .journalEntryId(journalEntryId)
                    .publicId((String) uploadResponse.get("public_id"))
                    .secureUrl((String) uploadResponse.get("secure_url"))
                    .userId(userId)
                    .build();

            Image savedImage = imageRepository.save(image);

            return CompletableFuture.completedFuture(savedImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
