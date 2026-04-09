package com.techspace.course_service.controller;

import com.techspace.course_service.service.SubtitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "*")
public class FileController {

    private static final String UPLOAD_DIR = "C:/temper/uploads/videos/";

    @Autowired
    private SubtitleService subtitleService;

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {

        try {
            // ✅ create directory
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) dir.mkdirs();

            if (!dir.canWrite()) {
                return ResponseEntity.internalServerError().body("No write permission!");
            }

            // ✅ unique file name
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String filePath = UPLOAD_DIR + fileName;

            System.out.println("Saving file to: " + filePath);

            File targetFile = new File(filePath);
            file.transferTo(targetFile);

            // 🔥 generate subtitle
            subtitleService.generateSubtitle(filePath);

            // ✅ return video URL
            String videoUrl = "http://localhost:8081/videos/" + fileName;

            Map<String, String> response = new HashMap<>();
            response.put("videoUrl", videoUrl);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Upload failed!");
        }
    }
}