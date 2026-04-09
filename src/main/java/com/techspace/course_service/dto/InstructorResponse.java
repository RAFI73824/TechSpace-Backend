package com.techspace.course_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstructorResponse {

    private Long id;
    private String name;
    private String bio;
    private String expertise;
    private Double rating;
}