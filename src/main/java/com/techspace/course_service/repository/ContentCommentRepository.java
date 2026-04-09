package com.techspace.course_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techspace.course_service.entity.ContentComment;

public interface ContentCommentRepository extends JpaRepository<ContentComment, Long> {

    List<ContentComment> findByContentId(Long contentId);

}