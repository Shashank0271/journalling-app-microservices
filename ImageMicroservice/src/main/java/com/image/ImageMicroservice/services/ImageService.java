package com.image.ImageMicroservice.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.image.ImageMicroservice.entities.Image;
import com.image.ImageMicroservice.entities.ImageCat;
import com.image.ImageMicroservice.repository.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.image.ImageMicroservice.utils.ImageUtil.convertMultipartFileToFile;

@Service
public class ImageService {
    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CloudinaryService cloudinaryService;

    public List<Image> createJournalEntryImages(List<MultipartFile> images,
                                                String journalEntryId,
                                                String userId
    ) {
        List<Image> storedImages = new ArrayList<>(); //response to be returned

        List<CompletableFuture<Image>> uploadFutures = new ArrayList<>();

        images.forEach(image -> {
            try {
                CompletableFuture<Image> uploadFuture = cloudinaryService.
                        uploadImageAndSaveInDB(convertMultipartFileToFile(image), journalEntryId, userId);
                uploadFutures.add(uploadFuture);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        //wait for all the threads to finish execution
        CompletableFuture.allOf(uploadFutures.toArray(new CompletableFuture[0]));

        uploadFutures.forEach((savedImageFuture) -> {
            try {
                storedImages.add(savedImageFuture.get());
            } catch (InterruptedException |
                     ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        return storedImages;
    }

    //over 70% improvement using multithreading

    public List<Image> getAllImagesForJournalEntry(String journalEntryId) {
        return imageRepository.findByJournalEntryId(journalEntryId);
    }

    public List<Image> getAllImagesForEntries(Set<String> journalEntryIds) {
        List<Image> requiredImages = imageRepository.findAllImagesForJournalIds((new ArrayList<>(journalEntryIds)));
        logger.debug(Integer.toString(requiredImages.size()));
        return requiredImages;
    }

    public void deleteAllImagesForJournalEntry(String journalEntryId) {
        List<String> publicIds = imageRepository.findImagePublicIdForEntryId(journalEntryId);
        publicIds.forEach(publicID -> {
            try {
                cloudinary.uploader()
                        .destroy(publicID, ObjectUtils.emptyMap());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        imageRepository.deleteImagesHavingJournalEntryId(journalEntryId);
    }

    public Image createUserProfileImage(MultipartFile userProfileImage, String userId) {
        try {
            Map<String, Object> uploadResponse = objectMapper.convertValue(cloudinary.uploader()
                    .upload(convertMultipartFileToFile(userProfileImage), ObjectUtils.emptyMap()), new TypeReference<Map<String, Object>>() {
            });
            Image image = Image.builder()
                    .imageCategory(ImageCat.PROFILE_PIC)
                    .userId(userId)
                    .secureUrl((String) uploadResponse.get("secure_url"))
                    .publicId((String) uploadResponse.get("public_id"))
                    .build();
            imageRepository.save(image);
            return image;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Image getUserProfilePicture(String userId) {
        return imageRepository.findByUserId(userId);
    }


}
