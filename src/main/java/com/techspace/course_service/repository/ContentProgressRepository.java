package com.techspace.course_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techspace.course_service.entity.ContentProgress;

public interface ContentProgressRepository extends JpaRepository<ContentProgress, Long> {

    // Existing method
    Optional<ContentProgress> findByUserIdAndContentId(Long userId, Long contentId);

    // This is the method you need to add for fetching progress by userId
    List<ContentProgress> findByUserId(Long userId);  // Fetch all content progress for a specific user

    // Additional method to count completed progress
    long countByUserIdAndCompletedTrueAndContent_Section_Course_Id(Long userId, Long courseId);

//	List<ContentProgress> findByUserId(Long userId);

	
}