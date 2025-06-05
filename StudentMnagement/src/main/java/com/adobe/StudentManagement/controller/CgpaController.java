package com.adobe.StudentManagement.controller;

import com.adobe.StudentManagement.model.Marks;
import com.adobe.StudentManagement.model.Subject;
import com.adobe.StudentManagement.repository.MarksRepository;
import com.adobe.StudentManagement.repository.SubjectRepository;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cgpa")
public class CgpaController {

    private final MarksRepository marksRepository;
    private final SubjectRepository subjectRepository;

    public CgpaController(MarksRepository marksRepository, SubjectRepository subjectRepository) {
        this.marksRepository = marksRepository;
        this.subjectRepository = subjectRepository;
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<?> calculateCGPA(@PathVariable String studentId) {
        try {
            ObjectId studentObjectId = new ObjectId(studentId);
            List<Marks> marksList = marksRepository.findByStudentId(studentObjectId);

            if (marksList.isEmpty()) {
                return ResponseEntity.status(404).body("No marks found for student");
            }

            double totalCredits = 0;
            double totalWeightedScore = 0;

            for (Marks mark : marksList) {
                ObjectId subjectId = mark.getSubjectId();

                Optional<Subject> subjectOpt = subjectRepository.findById(String.valueOf(subjectId));
                if (subjectOpt.isEmpty()) {
                    continue;
                }

                Subject subject = subjectOpt.get();
                double credits = subject.getCredits();

                int marksValue = mark.getMarks();
                int gradePoint = calculateGradePoint(marksValue);

                totalCredits += credits;
                totalWeightedScore += gradePoint * credits;
            }

            if (totalCredits == 0) {
                return ResponseEntity.status(400).body("Total credits is zero, cannot calculate CGPA");
            }

            double cgpa = totalWeightedScore / totalCredits;

            return ResponseEntity.ok(new CGPAResponse(studentId, cgpa));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid studentId format");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error calculating CGPA: " + e.getMessage());
        }
    }

    private int calculateGradePoint(int marks) {
        if (marks >= 90) return 10;
        else if (marks >= 80) return 9;
        else if (marks >= 70) return 8;
        else if (marks >= 60) return 7;
        else if (marks >= 50) return 6;
        else return 0;
    }


    private static class CGPAResponse {
        private String studentId;
        private double cgpa;

        public CGPAResponse(String studentId, double cgpa) {
            this.studentId = studentId;
            this.cgpa = Math.round(cgpa * 100.0) / 100.0;
        }

        public String getStudentId() { return studentId; }
        public double getCgpa() { return cgpa; }
    }
}
