package com.techspace.course_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techspace.course_service.entity.ContentRating;

public interface ContentRatingRepository extends JpaRepository<ContentRating, Long> {

    Optional<ContentRating> findByUserIdAndContentId(Long userId, Long contentId);

}