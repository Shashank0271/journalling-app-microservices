package com.image.ImageMicroservice.configs;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Value("${CLOUD_NAME}")
    private String CLOUD_NAME;
    @Value("${API_KEY}")
    private String APIKEY;
    @Value("${API_SECRET}")
    private String API_SECRET;

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CLOUD_NAME,
                "api_key", APIKEY,
                "api_secret", API_SECRET,
                "secure", true));
    }
}
