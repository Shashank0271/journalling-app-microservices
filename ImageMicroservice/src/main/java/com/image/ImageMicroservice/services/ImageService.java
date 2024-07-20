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
import java.util.*;

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

    public List<Image> createJournalEntryImages(List<MultipartFile> images,
                                                String journalEntryId,
                                                long userId
    ) {
        List<Image> storedImages = new ArrayList<>();
        images.forEach(imageFile -> {
            try {
                Map<String, Object> uploadResponse = objectMapper.convertValue(cloudinary.uploader()
                        .upload(convertMultipartFileToFile(imageFile), ObjectUtils.emptyMap()), new TypeReference<Map<String, Object>>() {
                });

                Image image = Image.builder()
                        .imageCategory(ImageCat.JOURNAL_ENTRY)
                        .journalEntryId(journalEntryId)
                        .publicId((String) uploadResponse.get("public_id"))
                        .secureUrl((String) uploadResponse.get("secure_url"))
                        .userId(userId)
                        .build();

                imageRepository.save(image);
                storedImages.add(image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return storedImages;
    }

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

    public Image createUserProfileImage(MultipartFile userProfileImage, long userId) {
        Image image = Image.builder()
                .imageCategory(ImageCat.PROFILE_PIC)
                .userId(userId)
                .build();
        imageRepository.save(image);
        return image;
    }

    public Optional<Image> getUserProfilePicture(long userId) {
//        Image userProfilePic = imageRepository.findByUserId(userId);
//        if (userProfilePic == null) {
//            return Optional.empty();
//        }
//        try {
//            byte[] uncompressedImage = ImageUtil.unCompressImage(userProfilePic.getImage());
//            userProfilePic.setImage(uncompressedImage);
//            userProfilePic.setSizeInBytes(uncompressedImage.length);
//        } catch (DataFormatException e) {
//            throw new RuntimeException(e);
//        }
//        return Optional.of(userProfilePic);
        return null;
    }


}
