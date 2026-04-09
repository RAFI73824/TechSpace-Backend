package com.techspace.course_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techspace.course_service.dto.SectionResponse;
import com.techspace.course_service.entity.Section;
import com.techspace.course_service.repository.SectionRepository;

@Service
public class SectionServiceImpl implements SectionService {

    @Autowired
    private SectionRepository sectionRepository;

    @Override
    public Section createSection(Section section) {
        return sectionRepository.save(section);
    }

    @Override
    public List<SectionResponse> getSectionsByCourse(Long courseId) {

        List<Section> sections = sectionRepository.findByCourseId(courseId);

        return sections.stream()
                .map(section -> SectionResponse.builder()
                        .id(section.getId())
                        .title(section.getTitle())
                        .position(section.getPosition())
                        .build())
                .toList();
    }

}