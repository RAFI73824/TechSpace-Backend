package com.techspace.course_service.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techspace.course_service.entity.ContentRating;
import com.techspace.course_service.repository.ContentRatingRepository;

@Service
public class ContentRatingServiceImpl implements ContentRatingService {

    @Autowired
    private ContentRatingRepository ratingRepository;

    @Override
    public ContentRating rateContent(ContentRating rating) {

        Long userId = rating.getUser().getId();
        Long contentId = rating.getContent().getId();

        Optional<ContentRating> existing =
                ratingRepository.findByUserIdAndContentId(userId, contentId);

        if (existing.isPresent()) {

            ContentRating cr = existing.get();
            cr.setRating(rating.getRating());
            cr.setLiked(rating.getLiked());

            return ratingRepository.save(cr);
        }

        return ratingRepository.save(rating);
    }
}