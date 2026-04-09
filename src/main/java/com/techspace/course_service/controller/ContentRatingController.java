package com.techspace.course_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techspace.course_service.entity.ContentRating;
import com.techspace.course_service.service.ContentRatingService;

@RestController
@RequestMapping("/api/ratings")
public class ContentRatingController {

    @Autowired
    private ContentRatingService ratingService;

    @PostMapping
    public ContentRating rateContent(@RequestBody ContentRating rating) {
        return ratingService.rateContent(rating);
    }
}