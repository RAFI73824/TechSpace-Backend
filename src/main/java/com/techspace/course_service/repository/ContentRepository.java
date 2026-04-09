package com.techspace.course_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techspace.course_service.entity.Content;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {

    List<Content> findBySectionId(Long sectionId);

    long countBySection_Course_Id(Long courseId);

}