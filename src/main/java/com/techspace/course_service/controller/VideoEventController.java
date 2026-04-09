package com.techspace.course_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techspace.course_service.entity.VideoEvent;
import com.techspace.course_service.service.VideoEventService;

@RestController
@RequestMapping("/api/video-events")
public class VideoEventController {

    @Autowired
    private VideoEventService videoEventService;

    @PostMapping
    public VideoEvent saveEvent(@RequestBody VideoEvent event) {
        return videoEventService.saveEvent(event);
    }
}