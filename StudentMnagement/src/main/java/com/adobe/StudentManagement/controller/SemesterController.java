package com.adobe.StudentManagement.controller;

import com.adobe.StudentManagement.model.Semester;
import com.adobe.StudentManagement.model.Subject;
import com.adobe.StudentManagement.repository.SemesterRepository;
import com.adobe.StudentManagement.repository.SubjectRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/semesters")
public class SemesterController {

    private final SemesterRepository semesterRepository;
    private final SubjectRepository subjectRepository;

    public SemesterController(SemesterRepository semesterRepository, SubjectRepository subjectRepository) {
        this.semesterRepository = semesterRepository;
        this.subjectRepository = subjectRepository;
    }

    // Create Semester
    @PostMapping
    public ResponseEntity<?> createSemester(@RequestBody Map<String, Object> requestBody) {
        int semesterNumber = (int) requestBody.get("semesterNumber");
        String name = (String) requestBody.get("name");
        List<String> subjectIds = (List<String>) requestBody.get("subjectIds");

        List<Subject> subjects = subjectRepository.findAllById(subjectIds);

        Semester semester = new Semester();
        semester.setSemesterNumber(semesterNumber);
        semester.setName(name);
        semester.setSubjects(subjects);

        return ResponseEntity.ok(semesterRepository.save(semester));
    }

    // Get all semesters
    @GetMapping
    public ResponseEntity<List<Semester>> getAllSemesters() {
        return ResponseEntity.ok(semesterRepository.findAll());
    }

    // Get semester by ID
    @GetMapping("/{id}")
    public ResponseEntity<Semester> getSemesterById(@PathVariable String id) {
        return semesterRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update semester by ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSemester(@PathVariable String id, @RequestBody Map<String, Object> requestBody) {
        Optional<Semester> optionalSemester = semesterRepository.findById(id);
        if (optionalSemester.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Semester semester = optionalSemester.get();

        if (requestBody.containsKey("semesterNumber")) {
            semester.setSemesterNumber((int) requestBody.get("semesterNumber"));
        }

        if (requestBody.containsKey("name")) {
            semester.setName((String) requestBody.get("name"));
        }

        if (requestBody.containsKey("subjectIds")) {
            List<String> subjectIds = (List<String>) requestBody.get("subjectIds");
            List<Subject> subjects = subjectRepository.findAllById(subjectIds);
            semester.setSubjects(subjects);
        }

        return ResponseEntity.ok(semesterRepository.save(semester));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSemester(@PathVariable String id) {
        if (!semesterRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        semesterRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
