package com.techspace.course_service.dto;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResponse {

	private Long id;
	private String title;
	private String description;
	private String category;
	@PositiveOrZero(message = "Price must be 0 or greater")
	private Double price;
	private Boolean active;
}