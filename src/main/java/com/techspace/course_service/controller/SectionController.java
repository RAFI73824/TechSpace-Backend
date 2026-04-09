package com.techspace.course_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techspace.course_service.dto.SectionResponse;
import com.techspace.course_service.entity.Section;
import com.techspace.course_service.service.SectionService;

@RestController
@RequestMapping("/api/sections")
public class SectionController {

    @Autowired
    private SectionService sectionService;

    @PostMapping
    public Section createSection(@RequestBody Section section) {
        return sectionService.createSection(section);
    }

    @GetMapping("/course/{courseId}")
    public List<SectionResponse> getSectionsByCourse(@PathVariable Long courseId) {
        return sectionService.getSectionsByCourse(courseId);
    }
}