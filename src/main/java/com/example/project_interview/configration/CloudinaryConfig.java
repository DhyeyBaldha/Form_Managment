package com.example.project_interview.configration;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary getCloudinary() {
        Map config = new HashMap();
        config.put("cloud_name", "dcnlmcvrr");
        config.put("api_key", "264847986489812");
        config.put("api_secret", "b5s_kkTn2C0XeD1eli3D92-R-Xw");
        config.put("secure", true);
        return new Cloudinary(config);
    }
}
