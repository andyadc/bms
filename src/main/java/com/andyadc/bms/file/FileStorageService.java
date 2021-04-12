package com.andyadc.bms.file;

import com.andyadc.bms.file.exception.FileSizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);
    private static final String separator = File.separator;
    private FileStorageSettings fileStorageSettings;

    private static String genFileName(String name) {
        String time = LocalTime.now().toString().replace(":", "");
        int i = ThreadLocalRandom.current().nextInt(100, 999);
        return time + "." + i + "." + name;
    }

    private void check(MultipartFile file) {
        Long limit = fileStorageSettings.getMaxSize();
        if (limit != null && limit > 0) {
            long size = file.getSize() / 1024L;
            if (size > limit) {
                throw new FileSizeLimitExceededException("");
            }
        }
    }

    public FileStorageDTO store(MultipartFile file) {
        logger.info("Filename: {}, filesize: {} kb", file.getOriginalFilename(), file.getSize() / 1024F);

        check(file);

        String date = LocalDate.now().toString().replace("-", "");
        String dir = fileStorageSettings.getPath().getPath() + date + separator;
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        String newName = genFileName(file.getOriginalFilename());
        Path path = Paths.get(dir + newName);
        try {
            file.transferTo(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileStorageDTO dto = new FileStorageDTO();
        String view = FileStorageConstants.RESOURCE_PATH + date + "/" + newName;
        dto.setLocalStoragePath(dir + newName);
        dto.setResourcePath(view);

        return dto;
    }

    @Autowired
    public void setFileStorageSettings(FileStorageSettings fileStorageSettings) {
        this.fileStorageSettings = fileStorageSettings;
    }
}
