package com.techspace.course_service.service;

import java.util.List;

import com.techspace.course_service.entity.Certificate;

public interface CertificateService {

    Certificate generateCertificate(Long userId, Long courseId);

    List<Certificate> getUserCertificates(Long userId);

    Certificate verifyCertificate(String code);

	Certificate getCertificateByCode(String code);
	
}