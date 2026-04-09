package com.techspace.course_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentResponse {

    private Long id;

    private String title;

    private String videoUrl;

    private String notes;

    private String pdfUrl;

    private Integer duration;

    private Integer position;

}