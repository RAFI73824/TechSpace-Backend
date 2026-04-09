package com.techspace.course_service.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.stereotype.Service;

@Service
public class SubtitleService {

	public void generateSubtitle(String videoPath) {
	    try {

	        ProcessBuilder processBuilder = new ProcessBuilder(
	                "C:\\Program Files\\Python313\\python.exe",   // 🔥 FULL PATH
	                "generate_subtitle.py",
	                videoPath
	        );

	        processBuilder.directory(new java.io.File(System.getProperty("user.dir")));

	        processBuilder.redirectErrorStream(true); // 🔥 VERY IMPORTANT

	        Process process = processBuilder.start();

	        BufferedReader reader = new BufferedReader(
	                new InputStreamReader(process.getInputStream())
	        );

	        String line;
	        while ((line = reader.readLine()) != null) {
	            System.out.println("PYTHON: " + line); // 🔥 LOG OUTPUT
	        }

	        int exitCode = process.waitFor();
	        System.out.println("Python exited with code: " + exitCode);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}