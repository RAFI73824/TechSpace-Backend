package com.techspace.course_service.service;

import java.util.Map;

import com.techspace.course_service.entity.ContentProgress;

public interface ContentProgressService {

    ContentProgress saveProgress(ContentProgress progress);

    ContentProgress getProgress(Long userId, Long contentId);

    boolean isCourseCompleted(Long userId, Long courseId);

	Map<String, Object> getCourseProgress(Long courseId, Long userId);

}