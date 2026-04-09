package com.techspace.course_service.service;

import java.util.List;
import com.techspace.course_service.dto.ContentResponse;
import com.techspace.course_service.entity.Content;

public interface ContentService {

    Content createContent(Content content);

    List<ContentResponse> getContentsBySection(Long sectionId);
    
}