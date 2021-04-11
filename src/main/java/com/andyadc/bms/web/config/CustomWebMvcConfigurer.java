package com.andyadc.bms.web.config;

import com.andyadc.bms.file.FileStorageConstants;
import com.andyadc.bms.file.FileStorageSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CustomWebMvcConfigurer implements WebMvcConfigurer {

    private FileStorageSettings fileStorageSettings;

    @Autowired
    public void setFileStorageSettings(FileStorageSettings fileStorageSettings) {
        this.fileStorageSettings = fileStorageSettings;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String path = fileStorageSettings.getPath().getPath();
        registry.addResourceHandler(FileStorageConstants.RESOURCE_PATH_PATTERNS).addResourceLocations("file:" + path);
    }
}
