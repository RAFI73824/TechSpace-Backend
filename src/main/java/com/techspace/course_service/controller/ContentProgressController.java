package com.techspace.course_service.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techspace.course_service.entity.ContentProgress;
import com.techspace.course_service.service.ContentProgressService;

@RestController
@RequestMapping("/api/progress")
public class ContentProgressController {

    @Autowired
    private ContentProgressService progressService;

    @PostMapping
    public ContentProgress saveProgress(@RequestBody ContentProgress progress) {
        return progressService.saveProgress(progress);
    }

    @GetMapping
    public ContentProgress getProgress(@RequestParam Long userId,
                                       @RequestParam Long contentId) {

        return progressService.getProgress(userId, contentId);
    }
    
    @GetMapping("/course/{courseId}/user/{userId}")
    public Map<String, Object> getCourseProgress(
            @PathVariable Long courseId,
            @PathVariable Long userId) {

        return progressService.getCourseProgress(courseId, userId);
    }
}