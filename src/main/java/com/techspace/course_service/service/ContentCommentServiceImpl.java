package com.techspace.course_service.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techspace.course_service.entity.ContentComment;
import com.techspace.course_service.repository.ContentCommentRepository;

@Service
public class ContentCommentServiceImpl implements ContentCommentService {

    @Autowired
    private ContentCommentRepository commentRepository;

    @Override
    public ContentComment addComment(ContentComment comment) {

        comment.setCreatedAt(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    @Override
    public List<ContentComment> getCommentsByContent(Long contentId) {

        return commentRepository.findByContentId(contentId);
    }
}