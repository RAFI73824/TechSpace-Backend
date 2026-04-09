package com.techspace.course_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techspace.course_service.dto.ContentResponse;
import com.techspace.course_service.entity.Content;
import com.techspace.course_service.exception.ResourceNotFoundException;
import com.techspace.course_service.repository.ContentRepository;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private ContentRepository contentRepository;

    @Override
    public Content createContent(Content content) {
        return contentRepository.save(content);
    }

    @Override
    public List<ContentResponse> getContentsBySection(Long sectionId) {

        List<Content> contents = contentRepository.findBySectionId(sectionId);

        return contents.stream()
                .map(content -> ContentResponse.builder()
                        .id(content.getId())
                        .title(content.getTitle())
                        .videoUrl(content.getVideoUrl())
                        .notes(content.getNotes())
                        .pdfUrl(content.getPdfUrl())
                        .duration(content.getDuration())
                        .position(content.getPosition())
                        .build())
                .toList();
    }
    
    public Content getContentById(Long id) {

        return contentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Content not found"));
    }
    
    
}