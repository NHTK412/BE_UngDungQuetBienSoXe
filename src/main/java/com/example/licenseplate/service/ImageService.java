package com.example.licenseplate.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    @Value("${app.image.accident}")
    private String imageAccidentPath;

    public String saveFile(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        Path filePath = Paths.get(imageAccidentPath, fileName);

        Files.createDirectories(filePath.getParent());

        file.transferTo(filePath.toFile());

        return filePath.toString();

    }

}
