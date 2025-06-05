package com.adobe.StudentManagement.controller;

import com.adobe.StudentManagement.model.Marks;
import com.adobe.StudentManagement.repository.MarksRepository;
import com.adobe.StudentManagement.repository.StudentRepository;
import com.adobe.StudentManagement.repository.SubjectRepository;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/marks")
public class MarksController {

    private final MarksRepository marksRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;

    public MarksController(MarksRepository marksRepository, StudentRepository studentRepository, SubjectRepository subjectRepository) {
        this.marksRepository = marksRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
    }

    @PostMapping
    public ResponseEntity<?> createMarks(@RequestBody Map<String, Object> requestBody) {
        try {
            ObjectId studentId = new ObjectId((String) requestBody.get("studentId"));
            ObjectId subjectId = new ObjectId((String) requestBody.get("subjectId"));
            int semester = (int) requestBody.get("semester");
            int marksValue = (int) requestBody.get("marks");

            if (!studentRepository.existsById(studentId.toHexString())) {
                return ResponseEntity.badRequest().body("Invalid studentId");
            }
            if (!subjectRepository.existsById(subjectId.toHexString())) {
                return ResponseEntity.badRequest().body("Invalid subjectId");
            }

            Marks marks = Marks.builder()
                    .studentId(studentId)
                    .subjectId(subjectId)
                    .semester(semester)
                    .marks(marksValue)
                    .build();

            return ResponseEntity.ok(marksRepository.save(marks));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid input format: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Marks>> getAllMarks() {
        List<Marks> marksList = marksRepository.findAll();
        return ResponseEntity.ok(marksList);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<?> getMarksByStudentId(@PathVariable String studentId) {
        try {
            ObjectId studentObjId = new ObjectId(studentId);
            List<Marks> marksList = marksRepository.findByStudentId(studentObjId);
            if (marksList.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(marksList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid ObjectId format: " + e.getMessage());
        }
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<?> updateMarksByStudentId(@PathVariable String studentId, @RequestBody Map<String, Object> requestBody) {
        try {
            ObjectId studentObjId = new ObjectId(studentId);
            List<Marks> marksList = marksRepository.findByStudentId(studentObjId);

            if (marksList.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            for (Marks marks : marksList) {
                if (requestBody.containsKey("marks")) {
                    marks.setMarks((int) requestBody.get("marks"));
                }
                if (requestBody.containsKey("semester")) {
                    marks.setSemester((int) requestBody.get("semester"));
                }
                if (requestBody.containsKey("subjectId")) {
                    ObjectId subjectId = new ObjectId((String) requestBody.get("subjectId"));
                    if (subjectRepository.existsById(subjectId.toHexString())) {
                        marks.setSubjectId(subjectId);
                    } else {
                        return ResponseEntity.badRequest().body("Invalid subjectId");
                    }
                }
            }

            marksRepository.saveAll(marksList);
            return ResponseEntity.ok(marksList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid ObjectId format: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }
}
