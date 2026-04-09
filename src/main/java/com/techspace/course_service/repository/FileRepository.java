package com.techspace.course_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.techspace.course_service.entity.File;

public interface FileRepository extends JpaRepository<File, Long> {
}