package com.adobe.StudentManagement.controller;


import com.adobe.StudentManagement.model.Student;
import com.adobe.StudentManagement.repository.StudentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Create
    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentRepository.save(student);
    }

    // Read all
    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Read one by id
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable String id) {
        Optional<Student> student = studentRepository.findById(id);
        return student.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update by id
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable String id, @RequestBody Student updatedStudent) {
        return studentRepository.findById(id)
                .map(student -> {
                    student.setName(updatedStudent.getName());
                    student.setRollNumber(updatedStudent.getRollNumber());
                    student.setEmail(updatedStudent.getEmail());
                    student.setDepartment(updatedStudent.getDepartment());
                    student.setAdmissionYear(updatedStudent.getAdmissionYear());
                    student.setAdmissionNo(updatedStudent.getAdmissionNo());
                    student.setContactNo(updatedStudent.getContactNo());
                    Student savedStudent = studentRepository.save(student);
                    return ResponseEntity.ok(savedStudent);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String id) {
        return studentRepository.findById(id)
                .map(student -> {
                    studentRepository.delete(student);
                    return ResponseEntity.noContent().<Void>build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
