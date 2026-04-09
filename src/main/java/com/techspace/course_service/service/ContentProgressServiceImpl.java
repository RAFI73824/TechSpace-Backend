package com.techspace.course_service.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.techspace.course_service.entity.ContentProgress;
import com.techspace.course_service.exception.ResourceNotFoundException;
import com.techspace.course_service.repository.ContentProgressRepository;
import com.techspace.course_service.repository.ContentRepository;

@Service
public class ContentProgressServiceImpl implements ContentProgressService {

    @Autowired
    private ContentProgressRepository progressRepository;

    @Autowired
    private ContentRepository contentRepository;

    // 🔥 FIX: Use ApplicationContext instead of direct injection
    @Autowired
    private ApplicationContext context;

    @Override
    public ContentProgress saveProgress(ContentProgress request) {

        Long userId = request.getUser().getId();
        Long contentId = request.getContent().getId();

        Optional<ContentProgress> existingOpt =
                progressRepository.findByUserIdAndContentId(userId, contentId);

        ContentProgress progress;

        if (existingOpt.isPresent()) {
            // 🔁 UPDATE EXISTING
            progress = existingOpt.get();

            // update only if progress increased
            if (request.getLastWatchedSeconds() > progress.getLastWatchedSeconds()) {
                progress.setLastWatchedSeconds(request.getLastWatchedSeconds());
            }

            progress.setDuration(request.getDuration());

        } else {
            // 🆕 CREATE NEW
            progress = request;
        }

        // 🔥 AUTO COMPLETION LOGIC
        if (progress.getDuration() > 0) {
            double percent =
                    (progress.getLastWatchedSeconds() / progress.getDuration()) * 100;

            if (percent >= 90) {
                progress.setCompleted(true);
            }
        }

        // 🔥 UPDATE TIME
        progress.setUpdatedAt(java.time.LocalDateTime.now());

        ContentProgress savedProgress = progressRepository.save(progress);

        // 🎓 COURSE COMPLETION CHECK
        Long courseId = savedProgress.getContent()
                .getSection()
                .getCourse()
                .getId();

        boolean completed = isCourseCompleted(userId, courseId);

        // 🔥 FIXED: NO circular dependency
        if (completed) {
            try {
                context.getBean(CertificateService.class)
                       .generateCertificate(userId, courseId);
            } catch (Exception e) {
                System.out.println("Certificate generation skipped: " + e.getMessage());
            }
        }

        return savedProgress;
    }

    @Override
    public ContentProgress getProgress(Long userId, Long contentId) {

        return progressRepository
                .findByUserIdAndContentId(userId, contentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Progress not found"));
    }

    @Override
    public boolean isCourseCompleted(Long userId, Long courseId) {

        long totalContent =
                contentRepository.countBySection_Course_Id(courseId);

        long completedContent =
                progressRepository
                        .countByUserIdAndCompletedTrueAndContent_Section_Course_Id(userId, courseId);

        return totalContent > 0 && totalContent == completedContent;
    }

    @Override
    public Map<String, Object> getCourseProgress(Long courseId, Long userId) {

        long totalVideos =
                contentRepository.countBySection_Course_Id(courseId);

        long completedVideos =
                progressRepository
                        .countByUserIdAndCompletedTrueAndContent_Section_Course_Id(userId, courseId);

        double percentage =
                totalVideos == 0 ? 0 : (completedVideos * 100.0) / totalVideos;

        Map<String, Object> result = new java.util.HashMap<>();

        result.put("totalVideos", totalVideos);
        result.put("completedVideos", completedVideos);
        result.put("percentage", Math.round(percentage));

        return result;
    }
}
