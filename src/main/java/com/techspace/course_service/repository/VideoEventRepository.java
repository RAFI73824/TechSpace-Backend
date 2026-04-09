package com.techspace.course_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techspace.course_service.entity.VideoEvent;

public interface VideoEventRepository extends JpaRepository<VideoEvent, Long> {
}