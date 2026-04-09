package com.techspace.course_service.service;

import java.util.List;

import com.techspace.course_service.dto.SectionResponse;
import com.techspace.course_service.entity.Section;

public interface SectionService {

    Section createSection(Section section);

    List<SectionResponse> getSectionsByCourse(Long courseId);

}