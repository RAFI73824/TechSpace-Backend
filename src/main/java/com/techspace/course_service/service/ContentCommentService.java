package com.techspace.course_service.service;

import java.util.List;

import com.techspace.course_service.entity.ContentComment;

public interface ContentCommentService {

    ContentComment addComment(ContentComment comment);

    List<ContentComment> getCommentsByContent(Long contentId);
}