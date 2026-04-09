package com.techspace.course_service.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String uploadFile(MultipartFile file);
    // Add methods for file retrieval if necessary
}