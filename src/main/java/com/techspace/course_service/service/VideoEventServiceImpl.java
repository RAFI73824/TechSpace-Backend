package com.techspace.course_service.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techspace.course_service.entity.VideoEvent;
import com.techspace.course_service.repository.VideoEventRepository;

@Service
public class VideoEventServiceImpl implements VideoEventService {

    @Autowired
    private VideoEventRepository videoEventRepository;

    @Override
    public VideoEvent saveEvent(VideoEvent event) {

        event.setCreatedAt(LocalDateTime.now());

        return videoEventRepository.save(event);
    }
}