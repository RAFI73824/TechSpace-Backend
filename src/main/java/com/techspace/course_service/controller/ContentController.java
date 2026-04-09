package com.techspace.course_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techspace.course_service.dto.ContentResponse;
import com.techspace.course_service.entity.Content;
import com.techspace.course_service.service.ContentService;

@RestController
@RequestMapping("/api/contents")
public class ContentController {

    @Autowired
    private ContentService contentService;

    @PostMapping
    public Content createContent(@RequestBody Content content) {
        return contentService.createContent(content);
    }

    @GetMapping("/section/{sectionId}")
    public List<ContentResponse> getContentsBySection(@PathVariable Long sectionId) {
        return contentService.getContentsBySection(sectionId);
    }
}