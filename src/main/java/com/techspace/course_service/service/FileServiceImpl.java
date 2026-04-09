package com.techspace.course_service.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {

    // Directory where files will be saved
    private static final String UPLOAD_DIR = "C:/Users/srafi2/Downloads/"; // Change to your directory

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            // Get the original filename
            String fileName = file.getOriginalFilename();
            Path targetPath = Paths.get(UPLOAD_DIR + "certificate-" + fileName);  // Adding "certificate-" as a prefix

            // Save the file to the target location
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }

            return "File uploaded successfully: " + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload file", e);
        }
    }
}