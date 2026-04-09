package com.techspace.course_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    

    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @Column(length = 10000)
    private String videoUrl;

    @Column(length = 10000)
    private String pdfUrl;

  
    private Integer duration;

    private Integer position;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;
    
    
}