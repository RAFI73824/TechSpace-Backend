package com.techspace.course_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techspace.course_service.entity.ContentComment;
import com.techspace.course_service.service.ContentCommentService;

@RestController
@RequestMapping("/api/comments")
public class ContentCommentController {

    @Autowired
    private ContentCommentService commentService;

    @PostMapping
    public ContentComment addComment(@RequestBody ContentComment comment) {
        return commentService.addComment(comment);
    }

    @GetMapping("/content/{contentId}")
    public List<ContentComment> getComments(@PathVariable Long contentId) {
        return commentService.getCommentsByContent(contentId);
    }
}